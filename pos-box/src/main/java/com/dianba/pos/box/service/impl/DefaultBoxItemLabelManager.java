package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.mapper.BoxItemLabelMapper;
import com.dianba.pos.box.po.BoxItemLabel;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.box.vo.ItemStorageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/7/4.
 */
@Service
public class DefaultBoxItemLabelManager implements BoxItemLabelManager {

    @Autowired
    private BoxItemLabelMapper boxItemLabelMapper;

    @Override
    public BasicResult itemStorage(ItemStorageVo itemStorageVo) {

        //根据code码

        return null;
    }

    @Override
    public BasicResult getItemsByRFID(Long passportId, List<String> rfids) {
        List<BoxItemLabel> boxItemLabels = boxItemLabelMapper.findItemsByRFID(rfids);
        Map<Long, BoxItemVo> boxItemVoMap = new HashMap<>();
        for (BoxItemLabel boxItemLabel : boxItemLabels) {
            BigDecimal itemPrice = boxItemLabel.getSalesPrice()
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            if (boxItemVoMap.containsKey(boxItemLabel.getItemId())) {
                BoxItemVo boxItemVo = boxItemVoMap.get(boxItemLabel.getItemId());
                boxItemVo.setItemQuantity(boxItemVo.getItemQuantity() + 1);
                boxItemVo.setItemPrice(boxItemVo.getItemPrice().add(itemPrice));
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
                boxItemVo.setIsPaid(boxItemLabel.getIsPaid());
                boxItemVo.setShowPaidName(boxItemLabel.getIsPaid() == 1 ? "已支付" : "未支付");
                boxItemVo.setRfids(boxItemLabel.getRfid());
                boxItemVoMap.put(boxItemLabel.getItemId(), boxItemVo);
            }
        }
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(new ArrayList<>(boxItemVoMap.values()));
        JSONObject jsonObject = basicResult.getResponse();
        jsonObject.put("qrCodeContent", "xxx");
        return basicResult;
    }
}
