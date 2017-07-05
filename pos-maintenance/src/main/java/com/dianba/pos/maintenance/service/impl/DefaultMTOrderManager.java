package com.dianba.pos.maintenance.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.maintenance.mapper.MTOrderMapper;
import com.dianba.pos.maintenance.service.MTOrderManager;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.payment.service.PaymentManager;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DefaultMTOrderManager implements MTOrderManager {

    private static Logger logger = LogManager.getLogger(DefaultMTOrderManager.class);

    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private MTOrderMapper mtOrderMapper;

    public BasicResult fixOffsetAmount() {
        List<LifeOrder> lifeOrders = mtOrderMapper.findNoneOffsetAmountOrders();
        List<LifeOrder> fixedOrders = new ArrayList<>();
        for (LifeOrder lifeOrder : lifeOrders) {
            BasicResult basicResult = paymentManager.processPaidOrder(lifeOrder.getSequenceNumber()
                    , "", PaymentTypeEnum.WEIXIN_NATIVE, false);
            if (basicResult.isSuccess()) {
                fixedOrders.add(lifeOrder);
            } else {
                logger.info("余额偏移处理失败！订单ID:" + lifeOrder.getId()
                        + "，错误消息：" + basicResult.getMsg());
            }
        }
        return BasicResult.createSuccessResultWithDatas("成功处理未偏移余额单据" + fixedOrders.size() + "条！"
                , fixedOrders);
    }
}
