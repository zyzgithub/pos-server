package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.repository.LifeItemTypeJpaRepository;
import com.dianba.pos.item.service.LifeItemTypeManager;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.metadata.item.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Service
public class DefaultLifeItemTypeManager implements LifeItemTypeManager {


    @Autowired
    private LifeItemTypeJpaRepository itemTypeJpaRepository;

    @Override
    public List<ItemType> getItemList() {
        //从内存中读取
        List<ItemType> itemTypes = ItemDataCacheService.getItemTypes();
        return itemTypes;
    }

    @Override
    public LifeItemType getItemTypeById(Long id) {
        return itemTypeJpaRepository.getItemTypeById(id);
    }


}
