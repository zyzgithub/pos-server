package com.dianba.pos.order.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.service.SettlementOrderManager;
import org.springframework.stereotype.Service;

@Service
public class DefaultSettlementManager implements SettlementOrderManager {

    public BasicResult getSettlementOrder(Long passportId) {

        return BasicResult.createSuccessResult();
    }
}
