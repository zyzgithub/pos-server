package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;

import javax.servlet.http.HttpServletResponse;

public interface WapPaymentManager {

    BasicResult wechatPay(String sequenceNumber, String spBillCreateIP) throws Exception;

    void aliPayByOutHtml(HttpServletResponse response, String sequenceNumber) throws Exception;
}
