package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.payment.po.LifePaymentTransactionLogger;
import com.dianba.pos.payment.repository.LifePaymentTransLoggerJpaRepository;
import com.dianba.pos.payment.service.TransLoggerManager;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultTransLoggerManager implements TransLoggerManager {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private LifePaymentTransLoggerJpaRepository transLoggerJpaRepository;

    @Transactional
    public void saveTransLog(String transSequenceNumber, Long passportId, String authCode
            , PaymentTypeEnum paymentType, TransTypeEnum transType, Long transAmount) {
        LifePaymentTransactionLogger transactionLogger = new LifePaymentTransactionLogger();
        transactionLogger.setTransSequenceNumber(transSequenceNumber);
        transactionLogger.setPassportId(passportId);
        transactionLogger.setPaymentType(paymentType.getKey());
        transactionLogger.setTransType(transType.getKey());
        transactionLogger.setPartnerId(appConfig.getPosPartnerId());
        transactionLogger.setAppId(appConfig.getPosAppId());
        transactionLogger.setPartnerUserId(passportId + "");
        transactionLogger.setPartnerTradeNumber(authCode);
        transactionLogger.setChannelId(paymentType.getChannelId());
        transactionLogger.setTransUnitAmount(transAmount);
        transactionLogger.setTransNumber(1);
        transactionLogger.setTransTotalAmount(transAmount);
        transactionLogger.setTransTitle(transType.getValue());
        transactionLogger.setUseConpon(GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        transactionLogger.setDiscountAmount(0L);
        transLoggerJpaRepository.save(transactionLogger);
    }
}
