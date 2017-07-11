package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxURLConstant;
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
        List<BoxItemVo> boxItemVos = getItemsByRFID(passportId, rfids);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(boxItemVos);
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("qrCodeContent", "http://apptest.0085.com/box/"
                + BoxURLConstant.PAYMENT + "qr_scan/" + passportId);
        return basicResult;
    }

    public List<BoxItemVo> getItemsByRFID(Long passportId, String rfids) {
        String[] rfidKeys = rfids.split(",");
        if (rfidKeys.length == 0) {
            throw new PosIllegalArgumentException("未选择商品！");
        }
        List<String> rfidList = Arrays.asList(rfidKeys);
        List<BoxItemLabel> boxItemLabels = boxItemLabelMapper.findItemsByRFID(rfidList);
        Map<Long, BoxItemVo> boxItemVoMap = new HashMap<>();
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            BigDecimal itemPrice = boxItemLabel.getSalesPrice()
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            if (boxItemVoMap.containsKey(boxItemLabel.getItemId())) {
                BoxItemVo boxItemVo = boxItemVoMap.get(boxItemLabel.getItemId());
                boxItemVo.setItemQuantity(boxItemVo.getItemQuantity() + 1);
                boxItemVo.setTotalPrice(boxItemVo.getTotalPrice().add(itemPrice));
                boxItemVo.setRfids(boxItemVo.getRfids() + "," + boxItemLabel.getRfid());
                if (boxItemVo.getIsPaid().intValue() != boxItemLabel.getIsPaid().intValue()) {
                    boxItemVo.setIsPaid(3);
                    boxItemVo.setShowPaidName("包含未支付商品！");
                }
            } else {
                BoxItemVo boxItemVo = new BoxItemVo();
                boxItemVo.setItemId(boxItemLabel.getItemId());
                boxItemVo.setItemName(boxItemLabel.getItemName());
                boxItemVo.setItemQuantity(1);
                boxItemVo.setItemPrice(itemPrice);
                boxItemVo.setTotalPrice(itemPrice);
                boxItemVo.setIsPaid(boxItemLabel.getIsPaid());
                boxItemVo.setShowPaidName(boxItemLabel.getIsPaid() == 1 ? "已支付" : "未支付");
                boxItemVo.setRfids(boxItemLabel.getRfid());
                boxItemVoMap.put(boxItemLabel.getItemId(), boxItemVo);
            }
        }
        return new ArrayList<>(boxItemVoMap.values());
    }

    @Transactional
    public List<BoxItemLabel> updateItemLabelToPaid(String rfids) {
        String[] rfidKeys = rfids.split(",");
        if (rfidKeys.length == 0) {
            throw new PosIllegalArgumentException("未选择商品！");
        }
        List<String> rfidList = Arrays.asList(rfidKeys);
        List<BoxItemLabel> boxItemLabels = itemLabelJpaRepository.findByRfidIn(rfidList);
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            boxItemLabel.setIsPaid(1);
        }
        return itemLabelJpaRepository.save(boxItemLabels);
    }
}
