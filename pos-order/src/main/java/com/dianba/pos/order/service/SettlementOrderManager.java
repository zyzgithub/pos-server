package com.dianba.pos.order.service;

import com.dianba.pos.order.po.LifeOrder;
import com.xlibao.common.constant.payment.PaymentTypeEnum;

import java.math.BigDecimal;

public interface SettlementOrderManager {

    /**
     * 生成结算订单
     */
    LifeOrder generateSettlementOrder(Long passportId, PaymentTypeEnum paymentType, BigDecimal amount);

    /**
     * 更新收银员结算后，订单的状态(payment_type)
     * @param passportId
     * @param date
     */
    void updateSettlementCashOrderByUserAndDate(Long passportId, String date);
}
