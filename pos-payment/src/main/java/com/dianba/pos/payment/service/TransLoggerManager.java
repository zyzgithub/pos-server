package com.dianba.pos.payment.service;

import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;

public interface TransLoggerManager {

    void saveTransLog(String transSequenceNumber, Long passportId, String authCode
            , PaymentTypeEnum paymentType, TransTypeEnum transType, Long transAmount);
}
