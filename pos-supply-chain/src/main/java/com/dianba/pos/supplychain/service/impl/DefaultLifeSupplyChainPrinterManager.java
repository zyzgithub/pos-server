package com.dianba.pos.supplychain.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.supplychain.service.LifeSupplyChainPrinterManager;
import com.dianba.pos.supplychain.support.LifeSupplyChainPrinterRemoteService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultLifeSupplyChainPrinterManager extends LifeSupplyChainPrinterRemoteService
        implements LifeSupplyChainPrinterManager {

    public BasicResult printerPurchaseOrder(Long passportId, Long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", passportId + "");
        params.put("orderId", orderId + "");
        return postPrint(PRINT_PURCHASE_ORDER, params);
    }
}
