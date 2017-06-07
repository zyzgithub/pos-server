package com.dianba.pos.settlement.service;

import com.dianba.pos.base.BasicResult;

import java.math.BigDecimal;

public interface SettlementManager {

    BasicResult getSettlementOrder(Long passportId, BigDecimal cashAmount);

    BasicResult settlementShift(Long passportId) throws Exception;

    BasicResult settlementPay(Long passportId, String paymentType, String authCode) throws Exception;
}
