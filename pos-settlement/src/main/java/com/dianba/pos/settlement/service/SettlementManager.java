package com.dianba.pos.settlement.service;

import com.dianba.pos.base.BasicResult;

public interface SettlementManager {

    BasicResult getSettlementOrder(Long passportId);
}
