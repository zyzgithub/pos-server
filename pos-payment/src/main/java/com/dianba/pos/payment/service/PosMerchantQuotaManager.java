package com.dianba.pos.payment.service;

import com.dianba.pos.payment.po.PosMerchantQuota;

public interface PosMerchantQuotaManager {

    PosMerchantQuota getMerchantQuota(Long passportId, Integer rewardType);

    PosMerchantQuota saveMerchantQuota(PosMerchantQuota posMerchantQuota);
}
