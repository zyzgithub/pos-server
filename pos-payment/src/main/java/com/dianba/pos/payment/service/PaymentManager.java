package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;

import java.math.BigDecimal;

public interface PaymentManager {

    String BASE_URL = "paymentController/";

    String UNIFIED_ORDER = BASE_URL + "unifiedOrder";

    String OFFSET_BALANCE = BASE_URL + "offsetBalance";

    String BALANCE_PAYMENT = BASE_URL + "balancePayment";

    String PASSPORT_CURRENCY = BASE_URL + "passportCurrency";

    String PAYMENT_PASSWORD_VAILD = BASE_URL + "paymentPasswordValidation";

    /**
     * 余额支付
     */
    BasicResult balancePayment(long passportId, long orderId, String paymentPassword);

    /**
     * 校验商家支付密码
     *
     * @param passportId
     * @param paymentPassword
     * @return
     */
    BasicResult checkPayPasswordKey(Long passportId, String paymentPassword);

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
     * 商户余额变更
     */
    BasicResult offsetBalance(Long passportId, String transSequenceNumber
            , Long offsetAmount, TransTypeEnum transTypeEnum);

    /**
     * 商户会员余额变更
     */
    public void offsetVipBalance(Long passportId, String transSequenceNumber
            , BigDecimal offsetAmount, PaymentTypeEnum paymentTypeEnum);
}
