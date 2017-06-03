package com.dianba.pos.supplychain.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.supplychain.config.LifeSupplyChainURLConstant;
import com.dianba.pos.supplychain.service.LifeSupplyChainPrinterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(LifeSupplyChainURLConstant.PRINTER)
public class LifeSupplyChainPrinterController {

    @Autowired
    private LifeSupplyChainPrinterManager printerManager;

    @ResponseBody
    @RequestMapping("print_purchase_order")
    public BasicResult printerPurchaseOrder(long passportId, long orderId) {
        return printerManager.printerPurchaseOrder(passportId, orderId);
    }
}
