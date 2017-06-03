package com.dianba.pos.supplychain.service;

import com.dianba.pos.base.BasicResult;

public interface LifeSupplyChainPrinterManager {

    String BASE_URL = "order/";

    String PRINT_PURCHASE_ORDER = BASE_URL + "printerOrderTicket";

    BasicResult printerPurchaseOrder(Long passportId, Long orderId);
}
