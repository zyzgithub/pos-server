package com.dianba.pos.payment.service;

import com.dianba.pos.payment.pojo.BarcodePayResponse;

public interface AliPayManager {

    BarcodePayResponse barcodePayment(Long passportId, Long orderId, String authCode) throws Exception;
}
