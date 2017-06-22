package com.dianba.pos.maintenance.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.maintenance.config.MTURLConstant;
import com.dianba.pos.maintenance.service.MTOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MTURLConstant.MT_ORDER_URL)
public class MTOrderController {

    @Autowired
    private MTOrderManager mtOrderManager;

    @GetMapping("fix_offset_amount")
    public BasicResult fixOffsetAmount() {
        return mtOrderManager.fixOffsetAmount();
    }
}
