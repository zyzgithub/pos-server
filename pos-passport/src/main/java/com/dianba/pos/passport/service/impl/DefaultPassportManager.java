package com.dianba.pos.passport.service.impl;
import com.dianba.pos.passport.mapper.PassportMapper;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.repository.PassportJpaRepository;
import com.dianba.pos.passport.service.PassportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Service
public class DefaultPassportManager implements PassportManager {
    @Autowired
    private PassportMapper passportMapper;

    @Override
    public Passport getPassportInfoByCashierId(Long cashierId) {
        return passportMapper.getPassportInfoByCashierId(cashierId);
    }
}
