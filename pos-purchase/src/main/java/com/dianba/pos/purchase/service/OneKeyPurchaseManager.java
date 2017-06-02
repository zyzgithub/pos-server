package com.dianba.pos.purchase.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.purchase.pojo.OneKeyPurchase;

import java.util.List;

public interface OneKeyPurchaseManager {

    List<OneKeyPurchase> getWarnRepertoryItems(Long passportId);

    BasicResult getWarnRepertoryList(Long passportId) throws Exception;
}
