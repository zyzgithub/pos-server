package com.dianba.pos.payment.service;

import com.dianba.pos.payment.pojo.BarcodePayResponse;

public interface WechatPayManager {

    String getOpenId(String authCode);

    BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode
            , String deviceInfo, String spBillCreateIP);
}
