package com.dianba.pos.purchase.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.purchase.pojo.OneKeyPurchase;

import java.util.List;
import java.util.Map;

public interface PurchaseManager {

    String BASE_URL = "order/";

    String GENERATE_ORDER = BASE_URL + "generateOrder";

    List<OneKeyPurchase> getWarnRepertoryItems(Long passportId);

    BasicResult getWarnRepertoryList(Long passportId) throws Exception;

    BasicResult generatePurchaseOrder(long passportId, String sequenceNumber, Long warehouseId
            , Map<String, Object> itemSet) throws Exception;
}
