package com.dianba.pos.passport.service.impl;

import com.dianba.pos.passport.po.PosBlackList;
import com.dianba.pos.passport.repository.PosBlackListJpaRepository;
import com.dianba.pos.passport.service.PosBlackListManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPosBlackListManager implements PosBlackListManager {

    @Autowired
    private PosBlackListJpaRepository blackListJpaRepository;

    public boolean isBlackMerchat(Long passportId) {
        PosBlackList posBlackList = blackListJpaRepository.findOne(passportId);
        if (posBlackList != null) {
            return true;
        }
        return false;
    }
}
