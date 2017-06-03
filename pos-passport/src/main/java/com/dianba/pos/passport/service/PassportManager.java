package com.dianba.pos.passport.service;

import com.dianba.pos.passport.po.Passport;



/**
 * Created by zhangyong on 2017/6/1.
 */
public interface PassportManager {

    Passport getPassportInfoByCashierId(Long cashierId);


}
