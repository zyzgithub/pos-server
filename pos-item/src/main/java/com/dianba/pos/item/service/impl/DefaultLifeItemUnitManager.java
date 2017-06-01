package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.LifeItemUnit;
import com.dianba.pos.item.repository.LifeItemUnitJpaRepository;
import com.dianba.pos.item.service.LifeItemUnitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/5/27.
 */
@Service
public class DefaultLifeItemUnitManager implements LifeItemUnitManager {

    @Autowired
    private LifeItemUnitJpaRepository itemUnitJpaRepository;

    @Override
    public LifeItemUnit getItemUnitById(Long id) {
        return itemUnitJpaRepository.getItemUnitById(id);
    }
}
