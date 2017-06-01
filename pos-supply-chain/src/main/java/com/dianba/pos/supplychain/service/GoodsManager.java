package com.dianba.pos.supplychain.service;

import com.dianba.pos.supplychain.vo.MatchItems;

import java.util.List;

public interface GoodsManager {

    List<MatchItems> matchItemsByBarcode(int userId, String barcodes);
}
