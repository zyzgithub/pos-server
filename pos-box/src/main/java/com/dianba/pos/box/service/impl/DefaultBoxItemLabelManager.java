package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.constant.BoxItemLabelPaidEnum;
import com.dianba.pos.box.mapper.BoxItemLabelMapper;
import com.dianba.pos.box.po.BoxItemLabel;
import com.dianba.pos.box.repository.BoxItemLabelJpaRepository;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.util.ScanItemsUtil;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.box.vo.ItemStorageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhangyong on 2017/7/4.
 */
@Service
public class DefaultBoxItemLabelManager implements BoxItemLabelManager {

    @Autowired
    private BoxItemLabelMapper boxItemLabelMapper;
    @Autowired
    private BoxItemLabelJpaRepository itemLabelJpaRepository;

    @Override
    public BasicResult itemStorage(ItemStorageVo itemStorageVo) {

        //根据code码

        return null;
    }

    @Override
    public BasicResult showItemsByRFID(Long passportId, String rfids) {
        ScanItemsUtil.writeScanItems(passportId, rfids);
        List<BoxItemVo> boxItemVos = getItemsByRFID(passportId, rfids, false);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(boxItemVos);
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("qrCodeContent", "http://apptest.0085.com/box/"
                + BoxURLConstant.PAYMENT + "qr_scan/" + passportId);
        return basicResult;
    }

    public List<BoxItemVo> getItemsByRFID(Long passportId, String rfids, boolean excludePaid) {
        List<BoxItemLabel> boxItemLabels = boxItemLabelMapper.findItemsByRFID(convertToRfidList(rfids));
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
            boxItemLabel.setShowPaidName(BoxItemLabelPaidEnum
                    .getBoxItemLabelPaidEnum(boxItemLabel.getIsPaid()).getValue());
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

    private List<String> convertToRfidList(String rfids) {
        String[] rfidKeys = rfids.split(",");
        if (rfidKeys.length == 0) {
            throw new PosIllegalArgumentException("未选择商品！");
        }
        return Arrays.asList(rfidKeys);
    }
}
