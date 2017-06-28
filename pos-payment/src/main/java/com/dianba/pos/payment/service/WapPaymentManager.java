package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;

import java.math.BigDecimal;

public interface WapPaymentManager {

    BasicResult wechatPay(Long passportId, String openId, String spBillCreateIP, BigDecimal amount)
            throws Exception;
}
