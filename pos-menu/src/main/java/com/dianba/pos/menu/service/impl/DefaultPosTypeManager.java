package com.dianba.pos.menu.service.impl;

import com.dianba.pos.menu.po.PosType;
import com.dianba.pos.menu.repository.PosTypeJpaRepository;
import com.dianba.pos.menu.service.PosTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Service
public class DefaultPosTypeManager implements PosTypeManager {

    @Autowired
    private PosTypeJpaRepository posTypeJpaRepository;
    @Override
    public List<PosType> getAllByPassportId(Long passportId) {
        return posTypeJpaRepository.getAllByPassportId(passportId);
    }
}
