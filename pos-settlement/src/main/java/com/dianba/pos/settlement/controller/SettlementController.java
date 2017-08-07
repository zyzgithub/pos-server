package com.dianba.pos.settlement.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.settlement.config.SettlementURLConstant;
import com.dianba.pos.settlement.service.SettlementManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping(SettlementURLConstant.SETTLEMENT_ORDER)
public class SettlementController {

    @Autowired
    private SettlementManager settlementManager;

    @ResponseBody
    @RequestMapping("get_order")
    public BasicResult getSettlementOrder(Long passportId) {
        return settlementManager.getSettlementOrder(passportId);
    }

    @ResponseBody
    @RequestMapping("settlement_shift")
    public BasicResult settlementShift(Long passportId, BigDecimal cashAmount) throws Exception {
        return settlementManager.settlementShift(passportId, cashAmount);
    }

    @ResponseBody
    @RequestMapping("settlement_pay")
    public BasicResult settlementPay(Long passportId, String paymentTypeKey, String authCode
            , BigDecimal cashAmount) throws Exception {
        return settlementManager.settlementPay(passportId, paymentTypeKey, authCode, cashAmount);
    }
}
