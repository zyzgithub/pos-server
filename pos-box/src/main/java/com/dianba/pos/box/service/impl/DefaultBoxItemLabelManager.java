package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxAppConfig;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.constant.BoxItemLabelPaidEnum;
import com.dianba.pos.box.mapper.BoxItemLabelMapper;
import com.dianba.pos.box.po.BoxItemLabel;
import com.dianba.pos.box.repository.BoxItemLabelJpaRepository;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.util.ScanItemsUtil;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.item.service.PosItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DefaultBoxItemLabelManager implements BoxItemLabelManager {

    @Autowired
    private BoxItemLabelMapper boxItemLabelMapper;
    @Autowired
    private BoxItemLabelJpaRepository itemLabelJpaRepository;
    @Autowired
    private PosItemManager posItemManager;
    @Autowired
    private BoxAppConfig boxAppConfig;

    @Override
    public BasicResult showItemsByRFID(Long passportId, String rfids) {
        ScanItemsUtil.writeScanItems(passportId, rfids);
        List<BoxItemVo> boxItemVos = getItemsByRFID(passportId, rfids, false);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(boxItemVos);
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("qrCodeContent", boxAppConfig.getBoxCallBackHost()
                + BoxURLConstant.PAYMENT + "qr_scan/" + passportId);
        return basicResult;
    }

    public List<BoxItemVo> getItemsByRFID(Long passportId, String rfids, boolean excludePaid) {
        List<String> rfidList = convertToRfidList(rfids);
        if (rfidList.size() == 0) {
            return new ArrayList<>();
        }
        List<BoxItemLabel> boxItemLabels = boxItemLabelMapper.findItemsByRFID(rfidList);
        Map<Long, BoxItemVo> boxItemVoMap = new HashMap<>();
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            BigDecimal itemPrice = boxItemLabel.getSalesPrice()
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            if (excludePaid && boxItemLabel.getIsPaid().intValue() == BoxItemLabelPaidEnum.PAID.getKey()) {
                continue;
            }
            if (boxItemVoMap.containsKey(boxItemLabel.getItemId())) {
                BoxItemVo boxItemVo = boxItemVoMap.get(boxItemLabel.getItemId());
                boxItemVo.setItemQuantity(boxItemVo.getItemQuantity() + 1);
                boxItemVo.setTotalPrice(boxItemVo.getTotalPrice().add(itemPrice));
                boxItemVo.setRfids(boxItemVo.getRfids() + "," + boxItemLabel.getRfid());
                if (boxItemVo.getIsPaid().intValue() != boxItemLabel.getIsPaid().intValue()) {
                    boxItemVo.setIsPaid(BoxItemLabelPaidEnum.HALF_PAID.getKey());
                    boxItemVo.setShowPaidName(BoxItemLabelPaidEnum.HALF_PAID.getValue());
                }
            } else {
                BoxItemVo boxItemVo = new BoxItemVo();
                boxItemVo.setItemId(boxItemLabel.getItemId());
                boxItemVo.setItemName(boxItemLabel.getItemName());
                boxItemVo.setItemQuantity(1);
                boxItemVo.setItemPrice(itemPrice);
                boxItemVo.setTotalPrice(itemPrice);
                BoxItemLabelPaidEnum itemLabelPaidEnum = BoxItemLabelPaidEnum
                        .getBoxItemLabelPaidEnum(boxItemLabel.getIsPaid());
                boxItemVo.setIsPaid(itemLabelPaidEnum.getKey());
                boxItemVo.setShowPaidName(itemLabelPaidEnum.getValue());
                boxItemVo.setRfids(boxItemLabel.getRfid());
                boxItemVoMap.put(boxItemLabel.getItemId(), boxItemVo);
            }
        }
        return new ArrayList<>(boxItemVoMap.values());
    }

    @Override
    public List<BoxItemLabel> getRFIDItems(String rfids) {
        List<BoxItemLabel> boxItemLabels = boxItemLabelMapper.findItemsByRFID(convertToRfidList(rfids));
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            boxItemLabel.setSalesPrice(boxItemLabel.getSalesPrice().divide(BigDecimal.valueOf(100)
                    , 2, BigDecimal.ROUND_HALF_UP));
            boxItemLabel.setShowPaidName(BoxItemLabelPaidEnum
                    .getBoxItemLabelPaidEnum(boxItemLabel.getIsPaid()).getValue());
        }
        return boxItemLabels;
    }

    @Override
    public List<BoxItemLabel> getItemsToBindingByRFID(String rfids) {
        List<BoxItemLabel> boxItemLabels = getRFIDItems(rfids);
        for (String rfid : convertToRfidList(rfids)) {
            boolean isExists = false;
            for (BoxItemLabel boxItemLabel : boxItemLabels) {
                if (rfid.equals(boxItemLabel.getRfid())) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                BoxItemLabel boxItemLabel = new BoxItemLabel();
                boxItemLabel.setRfid(rfid);
                boxItemLabel.setIsPaid(BoxItemLabelPaidEnum.NOT_PAID.getKey());
                boxItemLabel.setShowPaidName(BoxItemLabelPaidEnum.NOT_PAID.getValue());
                boxItemLabels.add(boxItemLabel);
            }
        }
        return boxItemLabels;
    }

    @Transactional
    public List<BoxItemLabel> updateItemLabelToPaid(String rfids) {
        List<BoxItemLabel> boxItemLabels = itemLabelJpaRepository.findByRfidIn(convertToRfidList(rfids));
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            boxItemLabel.setIsPaid(1);
        }
        return itemLabelJpaRepository.save(boxItemLabels);
    }

    @Transactional
    public void bindItemLabelToItems(Long itemId, String rfids) {
        List<String> rfidList = convertToRfidList(rfids);
        List<BoxItemLabel> boxItemLabels = getRFIDItems(rfids);
        //绑定且增加商品库存
        Map<Long, Integer> items = new HashMap<>();
        items.put(itemId, -rfidList.size());
        posItemManager.offsetItemRepertory(items);
        for (String rfid : rfidList) {
            boolean isExists = false;
            for (BoxItemLabel boxItemLabel : boxItemLabels) {
                if (boxItemLabel.getRfid().equals(rfid)) {
                    isExists = true;
                    boxItemLabel.setItemId(itemId);
                    boxItemLabel.setIsPaid(BoxItemLabelPaidEnum.NOT_PAID.getKey());
                    break;
                }
            }
            if (!isExists) {
                BoxItemLabel boxItemLabel = new BoxItemLabel();
                boxItemLabel.setItemId(itemId);
                boxItemLabel.setRfid(rfid);
                boxItemLabel.setIsPaid(BoxItemLabelPaidEnum.NOT_PAID.getKey());
                boxItemLabels.add(boxItemLabel);
            }
        }
        itemLabelJpaRepository.save(boxItemLabels);
    }

    @Override
    public boolean isAllPaid(String rfids) {
        List<BoxItemLabel> boxItemLabels = getItemsToBindingByRFID(rfids);
        boolean isAllPaid = true;
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            if (!BoxItemLabelPaidEnum.PAID.getKey().equals(boxItemLabel.getIsPaid())) {
                isAllPaid = false;
                break;
            }
        }
        return isAllPaid;
    }

    private List<String> convertToRfidList(String rfids) {
        String[] rfidKeys = rfids.split(",");
        if (rfidKeys.length == 0) {
            throw new PosIllegalArgumentException("未选择商品！");
        }
        return Arrays.asList(rfidKeys);
    }
}
