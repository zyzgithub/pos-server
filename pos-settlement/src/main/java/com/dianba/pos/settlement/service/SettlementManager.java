package com.dianba.pos.settlement.service;

import com.dianba.pos.base.BasicResult;

import java.math.BigDecimal;

public interface SettlementManager {

    BasicResult getSettlementOrder(Long passportId);

    BasicResult settlementShift(Long passportId, BigDecimal cashAmount) throws Exception;

    BasicResult settlementPay(Long passportId, String paymentType, String authCode
            , BigDecimal cashAmount) throws Exception;
}
