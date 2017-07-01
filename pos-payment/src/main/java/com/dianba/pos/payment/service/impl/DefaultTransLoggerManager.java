package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.payment.po.LifePaymentTransactionLogger;
import com.dianba.pos.payment.repository.LifePaymentTransLoggerJpaRepository;
import com.dianba.pos.payment.service.TransLoggerManager;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        if (TransTypeEnum.PAYMENT.getKey() == transType.getKey()) {
            transactionLogger.setTransTitle(transType.getValue());
            transactionLogger.setCurrencyType(CurrencyTypeEnum.BALANCE.getKey() + "");
            transactionLogger.setExtendParameter(CurrencyTypeEnum.BALANCE.getKey() + "");
        } else if (TransTypeEnum.RECHARGE.getKey() == transType.getKey()) {
            transactionLogger.setTransTitle("营业返利");
            transactionLogger.setCurrencyType(CurrencyTypeEnum.VIP_BALANCE.getKey() + "");
            transactionLogger.setExtendParameter(CurrencyTypeEnum.VIP_BALANCE.getKey() + "");
        }
        transactionLogger.setTransStatus(TransStatusEnum.TRADE_SUCCESSED_SERVER.getKey());
        transactionLogger.setTransType(transType.getKey());
        transactionLogger.setPartnerId(appConfig.getPosPartnerId());
        transactionLogger.setAppId(appConfig.getPosAppId());
        transactionLogger.setPartnerUserId(passportId + "");
        transactionLogger.setPartnerTradeNumber(authCode);
        transactionLogger.setChannelId(paymentType.getChannelId());
        transactionLogger.setTransUnitAmount(transAmount);
        transactionLogger.setTransNumber(1);
        transactionLogger.setTransTotalAmount(transAmount);
        transactionLogger.setUseConpon(GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        transactionLogger.setDiscountAmount(0L);
        transactionLogger.setCreateTime(new Date());
        transactionLogger.setTransCreateTime(new Date());
        transactionLogger.setPaymentTime(new Date());
        transLoggerJpaRepository.save(transactionLogger);
    }
}
