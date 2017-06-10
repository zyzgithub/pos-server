package com.dianba.pos.passport.service;

import com.dianba.pos.base.BasicResult;

/**
 * Created by zhangyong on 2017/6/10.
 */
public interface PosProtocolManager {

    BasicResult findPosProtocolById(Long id);

    BasicResult findAll();
}
