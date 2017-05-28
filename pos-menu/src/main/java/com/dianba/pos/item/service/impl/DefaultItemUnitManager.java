package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.ItemUnit;
import com.dianba.pos.item.repository.ItemUnitJpaRepository;
import com.dianba.pos.item.service.ItemUnitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/5/27.
 */
@Service
public class DefaultItemUnitManager implements ItemUnitManager {

    @Autowired
    private ItemUnitJpaRepository itemUnitJpaRepository;

    @Override
    public ItemUnit getItemUnitById(Long id) {
        return itemUnitJpaRepository.getItemUnitById(id);
    }
}
