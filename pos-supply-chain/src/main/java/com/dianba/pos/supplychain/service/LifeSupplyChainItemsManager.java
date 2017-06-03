package com.dianba.pos.supplychain.service;

import com.dianba.pos.supplychain.vo.MatchItemsVo;
import com.dianba.pos.supplychain.vo.WarehouseItemsVo;

import java.util.List;

public interface LifeSupplyChainItemsManager {

    WarehouseItemsVo matchItemsByBarcode(Long passportId, String barcodes);
}
