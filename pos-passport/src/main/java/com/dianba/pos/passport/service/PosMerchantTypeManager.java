package com.dianba.pos.passport.service;

import com.dianba.pos.passport.po.PosMerchantType;

public interface PosMerchantTypeManager {

    PosMerchantType findByPassportId(Long passportId);
}
