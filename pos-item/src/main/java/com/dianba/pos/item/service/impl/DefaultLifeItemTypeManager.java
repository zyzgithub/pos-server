package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.repository.LifeItemTypeJpaRepository;
import com.dianba.pos.item.service.LifeItemTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Service
public class DefaultLifeItemTypeManager implements LifeItemTypeManager {


    @Autowired
    private LifeItemTypeJpaRepository itemTypeJpaRepository;

    @Override
    public LifeItemType getItemTypeById(Long id) {
        return itemTypeJpaRepository.getItemTypeById(id);
    }


}
