package com.dianba.pos.order.service;

import com.dianba.pos.order.po.LifeOrder;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.metadata.order.OrderEntry;

import java.math.BigDecimal;

public interface SettlementOrderManager {

    /**
     * 生成结算订单
     */
    LifeOrder generateSettlementOrder(Long passportId, PaymentTypeEnum paymentType, BigDecimal amount);

    /**
     * 查询结算订单
     */
    OrderEntry findSettlementOrderByDay(Long passportId);
}
