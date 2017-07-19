package com.dianba.pos.payment.service;

public interface PaymentLogManager {

    void savePaidPaymentLog(Long orderId, String sequenceNumber, boolean isSuccess, String msg);

    void saveFailPaymentLog(Long orderId, String sequenceNumber, String msg);
}
