package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;

public interface SettlementOrderManager {

    BasicResult getSettlementOrder(Long passportId);
}
