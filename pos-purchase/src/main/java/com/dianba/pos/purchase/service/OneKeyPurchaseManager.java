package com.dianba.pos.purchase.service;

import java.util.Map;

public interface OneKeyPurchaseManager {

    Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId) throws Exception;
}
