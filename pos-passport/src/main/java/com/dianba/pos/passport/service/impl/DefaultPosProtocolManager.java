package com.dianba.pos.passport.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.passport.po.PosProtocol;
import com.dianba.pos.passport.repository.PosProtocolJpaRepository;
import com.dianba.pos.passport.service.PosProtocolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/6/10.
 */
@Service
public class DefaultPosProtocolManager implements PosProtocolManager {

    @Autowired
   private PosProtocolJpaRepository posProtocolJpaRepository;
    @Override
    public BasicResult findPosProtocolById(Long id) {

        PosProtocol posProtocol=posProtocolJpaRepository.findPosProtocolById(id);
        JSONObject jsonObject=(JSONObject)JSONObject.toJSON(posProtocol);
        return BasicResult.createSuccessResult("获取成功",jsonObject);
    }

    @Override
    public BasicResult findAll() {
        List<PosProtocol> posProtocolList=posProtocolJpaRepository.findAll();

        return BasicResult.createSuccessResultWithDatas("获取成功",posProtocolList);
    }
}
