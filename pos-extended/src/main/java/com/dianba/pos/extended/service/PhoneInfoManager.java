package com.dianba.pos.extended.service;

import com.dianba.pos.extended.po.PhoneInfo;

public interface PhoneInfoManager {

    PhoneInfo findByMobileNumber(Long mobileNumber);
}
