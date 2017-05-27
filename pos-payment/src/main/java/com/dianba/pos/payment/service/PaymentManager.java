package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;

public interface PaymentManager {

    String BASE_URL = "paymentController/";

    String UNIFIED_ORDER = BASE_URL + "unifiedOrder";

    /**
     * 订单支付
     */
    BasicResult payOrder(long passportId, long orderId
            , String paymentType, int transType, long transTotalAmount, String transTitle);
}
