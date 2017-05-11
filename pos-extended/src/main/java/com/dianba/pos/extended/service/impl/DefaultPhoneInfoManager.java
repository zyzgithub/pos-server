package com.dianba.pos.extended.service.impl;

import com.dianba.pos.extended.po.PhoneInfo;
import com.dianba.pos.extended.repository.PhoneInfoJpaRepository;
import com.dianba.pos.extended.service.PhoneInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  DefaultPhoneInfoManager implements PhoneInfoManager {

    @Autowired
    private PhoneInfoJpaRepository phoneInfoJpaRepository;

    public PhoneInfo findByMobileNumber(Long mobileNumber) {
        String mobilePrefix = (mobileNumber + "").substring(0, 7);
        return phoneInfoJpaRepository.findOne(Long.parseLong(mobilePrefix));
    }
}
