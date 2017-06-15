package com.dianba.pos.passport.service;

import com.dianba.pos.passport.po.LifeAchieve;

public interface LifeAchieveManager {

    LifeAchieve findByPassportId(Long passportId);
}
