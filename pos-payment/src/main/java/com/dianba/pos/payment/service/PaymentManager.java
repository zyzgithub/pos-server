package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;
import com.xlibao.common.constant.payment.TransTypeEnum;

public interface PaymentManager {

    String BASE_URL = "paymentController/";

    String UNIFIED_ORDER = BASE_URL + "unifiedOrder";

    String OFFSET_BALANCE = BASE_URL + "offsetBalance";

    String BALANCE_PAYMENT = BASE_URL + "balancePayment";

    String PASSPORT_CURRENCY = BASE_URL + "passportCurrency";

    /**
     * 余额支付
     */
    BasicResult balancePayment(long passportId, long orderId, String paymentPassword);

    /**
     * 获取商家余额
     */
    BasicResult passportCurrency(long passportId) throws Exception;

    /**
     * 订单支付(支付宝，微信)
     */
    BasicResult payOrder(long passportId, long orderId, String paymentTypeKey, String authCode)
            throws Exception;

    /**
     * 订单余额变更
     */
    BasicResult offsetBalance(long passportId, String transSequenceNumber
            , long offsetAmount, TransTypeEnum transTypeEnum);
}
