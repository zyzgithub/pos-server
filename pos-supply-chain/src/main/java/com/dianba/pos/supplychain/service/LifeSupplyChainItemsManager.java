package com.dianba.pos.supplychain.service;

import com.dianba.pos.supplychain.vo.MatchItems;

import java.util.List;

public interface LifeSupplyChainItemsManager {

    List<MatchItems> matchItemsByBarcode(Long passportId, String barcodes);
}
