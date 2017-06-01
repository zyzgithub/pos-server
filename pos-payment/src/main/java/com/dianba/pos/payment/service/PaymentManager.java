package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;
import com.xlibao.common.constant.payment.TransTypeEnum;

public interface PaymentManager {

    String BASE_URL = "paymentController/";

    String UNIFIED_ORDER = BASE_URL + "unifiedOrder";

    String OFFSET_BALANCE = BASE_URL + "offsetBalance";

    /**
     * 订单支付
     */
    BasicResult payOrder(long passportId, long orderId, String paymentTypeKey, String authCode)
            throws Exception;

    /**
     * 订单余额变更
     */
    BasicResult offsetBalance(long passportId, String transSequenceNumber
            , long offsetAmount, TransTypeEnum transTypeEnum);
}
