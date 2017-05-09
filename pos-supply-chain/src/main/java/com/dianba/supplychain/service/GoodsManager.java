package com.dianba.supplychain.service;

import com.dianba.supplychain.vo.MatchItems;

import java.util.List;

public interface GoodsManager {

    List<MatchItems> matchItemsByBarcode(int userId, String barcodes);
}
