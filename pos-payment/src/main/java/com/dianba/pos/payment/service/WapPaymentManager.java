package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;

import javax.servlet.http.HttpServletResponse;

public interface WapPaymentManager {

    BasicResult wechatPay(String sequenceNumber, String spBillCreateIP, String notifyUrl) throws Exception;

    void aliPayByOutHtml(HttpServletResponse response, String sequenceNumber
            , String returnUrl, String notifyUrl) throws Exception;
}
