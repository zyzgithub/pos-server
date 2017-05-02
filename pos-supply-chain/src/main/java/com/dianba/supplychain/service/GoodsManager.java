package com.dianba.supplychain.service;

import com.alibaba.fastjson.JSONArray;

public interface GoodsManager {

    JSONArray matchItems(int userId, String barcodes);
}