package com.dianba.pos.supplychain.service;

import com.dianba.pos.supplychain.vo.WarehouseItemsVo;

public interface LifeSupplyChainItemsManager {

    WarehouseItemsVo matchItemsByBarcode(Long passportId, String barcodes);
}
