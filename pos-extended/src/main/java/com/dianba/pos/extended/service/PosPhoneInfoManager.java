package com.dianba.pos.extended.service;

import com.dianba.pos.extended.po.PosPhoneInfo;

public interface PosPhoneInfoManager {

    PosPhoneInfo findByMobileNumber(Long mobileNumber);
}
