package com.dianba.pos.passport.service;

import com.dianba.pos.passport.po.PosMerchantRate;

public interface PosMerchantRateManager {

    PosMerchantRate findByMerchantPassportId(Long merchantPassportId);
}
