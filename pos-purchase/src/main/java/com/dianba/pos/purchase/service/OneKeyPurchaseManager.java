package com.dianba.pos.purchase.service;

import com.dianba.pos.common.util.HttpProxy;

import java.io.IOException;
import java.util.Map;

public interface OneKeyPurchaseManager {

    Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId) throws HttpProxy.HttpAccessException, IOException;
}
