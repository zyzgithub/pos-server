package com.dianba.pos.payment.service.impl;

import com.dianba.pos.common.constant.payment.PaymentLogTypeEnum;
import com.dianba.pos.payment.po.PosPaymentLog;
import com.dianba.pos.payment.repository.PosPaymentLogJpaRepository;
import com.dianba.pos.payment.service.PaymentLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DefaultPaymentLogManager implements PaymentLogManager {

    @Autowired
    private PosPaymentLogJpaRepository posPaymentLogJpaRepository;

    @Transactional
    public void savePaidPaymentLog(Long orderId, String sequenceNumber, boolean isSuccess, String msg) {
        PosPaymentLog posPaymentLog = new PosPaymentLog();
        posPaymentLog.setOrderId(orderId);
        posPaymentLog.setSequenceNumber(sequenceNumber);
        posPaymentLog.setIsPaid(1);
        posPaymentLog.setCreateTime(new Date());
        PaymentLogTypeEnum paymentLogTypeEnum = isSuccess
                ? PaymentLogTypeEnum.COMPLETE_ORDER_SUCCESS : PaymentLogTypeEnum.COMPLETE_ORDER_FAIL;
        posPaymentLog.setType(paymentLogTypeEnum.getType());
        posPaymentLog.setTitle(paymentLogTypeEnum.getTitle() + msg);
        posPaymentLogJpaRepository.save(posPaymentLog);
    }

    @Transactional
    public void saveFailPaymentLog(Long orderId, String sequenceNumber, String msg) {
        PosPaymentLog posPaymentLog = new PosPaymentLog();
        posPaymentLog.setOrderId(orderId);
        posPaymentLog.setSequenceNumber(sequenceNumber);
        posPaymentLog.setIsPaid(0);
        posPaymentLog.setCreateTime(new Date());
        PaymentLogTypeEnum paymentLogTypeEnum = PaymentLogTypeEnum.PAYMENT_FAIL;
        posPaymentLog.setType(paymentLogTypeEnum.getType());
        posPaymentLog.setTitle(paymentLogTypeEnum.getTitle() + msg);
        posPaymentLogJpaRepository.save(posPaymentLog);
    }
}
