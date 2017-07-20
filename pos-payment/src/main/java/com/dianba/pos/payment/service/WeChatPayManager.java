package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.payment.pojo.BarcodePayResponse;

public interface WeChatPayManager {

    BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode
            , String deviceInfo, String spBillCreateIP) throws Exception;

    BasicResult jsPayment(LifeOrder lifeOrder, String openId, String deviceInfo, String spBillCreateIP
            , String notifyUrl) throws Exception;
}
