package com.dianba.pos.order.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.service.SettlementOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(OrderURLConstant.SETTLEMENT)
public class SettlementOrderController {

    @Autowired
    private SettlementOrderManager settlementOrderManager;

    @ResponseBody
    @RequestMapping("get_settlement_order")
    public BasicResult getSettlementOrder(Long passportId) {
        return settlementOrderManager.getSettlementOrder(passportId);
    }
}
