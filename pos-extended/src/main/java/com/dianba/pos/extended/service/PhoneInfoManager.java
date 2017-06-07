package com.dianba.pos.extended.service;

import com.dianba.pos.extended.po.PosPhoneInfo;

public interface PhoneInfoManager {

    PosPhoneInfo findByMobileNumber(Long mobileNumber);
}
