package com.dianba.pos.passport.service.impl;

import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.passport.po.LifeAchieve;
import com.dianba.pos.passport.repository.LifeAchieveJpaRepository;
import com.dianba.pos.passport.service.LifeAchieveManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultLifeAchieveManager implements LifeAchieveManager {

    @Autowired
    private LifeAchieveJpaRepository lifeAchieveJpaRepository;

    @Override
    public LifeAchieve findByPassportId(Long passportId) {
        LifeAchieve lifeAchieve = lifeAchieveJpaRepository.findByPassportId(passportId);
        if (lifeAchieve == null) {
            throw new PosNullPointerException("商家地址信息不存在！");
        }
        return lifeAchieve;
    }
}
