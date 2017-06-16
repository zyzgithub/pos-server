package com.dianba.pos.extended.service.impl;

import com.dianba.pos.extended.po.PosPhoneInfo;
import com.dianba.pos.extended.repository.PosPhoneInfoJpaRepository;
import com.dianba.pos.extended.service.PosPhoneInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPosPhoneInfoManager implements PosPhoneInfoManager {

    @Autowired
    private PosPhoneInfoJpaRepository phoneInfoJpaRepository;

    public PosPhoneInfo findByMobileNumber(Long mobileNumber) {
        String mobilePrefix = (mobileNumber + "").substring(0, 7);
        return phoneInfoJpaRepository.findOne(Long.parseLong(mobilePrefix));
    }
}
