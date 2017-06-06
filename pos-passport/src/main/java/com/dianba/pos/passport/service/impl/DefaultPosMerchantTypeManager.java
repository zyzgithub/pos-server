package com.dianba.pos.passport.service.impl;

import com.dianba.pos.passport.po.PosMerchantType;
import com.dianba.pos.passport.repository.PosMerchantTypeJpaRepository;
import com.dianba.pos.passport.service.PosMerchantTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPosMerchantTypeManager implements PosMerchantTypeManager {

    @Autowired
    private PosMerchantTypeJpaRepository posMerchantTypeJpaRepository;

    @Override
    public PosMerchantType findByPassportId(Long passportId) {
        return posMerchantTypeJpaRepository.findOne(passportId);
    }
}
