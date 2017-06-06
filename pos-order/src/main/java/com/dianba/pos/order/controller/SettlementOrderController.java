package com.dianba.pos.order.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.config.OrderURLConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(OrderURLConstant.SETTLEMENT)
public class SettlementOrderController {

    @ResponseBody
    @RequestMapping("get_settlement_order")
    public BasicResult getSettlementOrder(Long passportId){

        return BasicResult.createSuccessResult();
    }
}
