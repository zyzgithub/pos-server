package com.dianba.pos.passport.service.impl;

import com.dianba.pos.passport.po.PosMerchantRate;
import com.dianba.pos.passport.repository.PosMerchantRateJpaRepository;
import com.dianba.pos.passport.service.PosMerchantRateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPosMerchantRateManager implements PosMerchantRateManager {

    @Autowired
    private PosMerchantRateJpaRepository posMerchantRateJpaRepository;

    @Override
    public PosMerchantRate findByMerchantPassportId(Long merchantPassportId) {
        return posMerchantRateJpaRepository.findOne(merchantPassportId);
    }
}
