package com.dianba.pos.box.service.impl;

import com.dianba.pos.box.po.BoxDoorInfo;
import com.dianba.pos.box.repository.BoxDoorInfoJpaRepository;
import com.dianba.pos.box.service.BoxDoorInfoManager;
import com.dianba.pos.core.annotation.EternalCacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultBoxDoorInfoManager implements BoxDoorInfoManager {

    @Autowired
    private BoxDoorInfoJpaRepository boxDoorInfoJpaRepository;

    public BoxDoorInfo getDoorInfoByPassportId(Long passportId) {
        return boxDoorInfoJpaRepository.findByPassportId(passportId);
    }

    @EternalCacheable
    public BoxDoorInfo getDoorInfoByAccessSN(String accessSN) {
        return boxDoorInfoJpaRepository.findByAccessSN(accessSN);
    }
}
