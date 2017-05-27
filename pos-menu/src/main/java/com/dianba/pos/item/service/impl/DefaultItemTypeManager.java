package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.repository.ItemTypeJpaRepository;
import com.dianba.pos.item.service.ItemTypeManager;
import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.metadata.item.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Service
public class DefaultItemTypeManager implements ItemTypeManager {


    @Autowired
    private ItemTypeJpaRepository itemTypeJpaRepository;
    @Override
    public List<ItemType> getItemList() {
        DataCacheApplicationContextLoaderNotify.setItemRemoteServiceURL(
                "http://120.25.199.108:8081/");
        ItemDataCacheService.initItemCache();

        //从内存中读取
        List<ItemType> itemTypes = ItemDataCacheService.getItemTypes();
        return itemTypes;
    }

    @Override
    public com.dianba.pos.item.po.ItemType getItemTypeById(Long id) {
        return itemTypeJpaRepository.getItemTypeById(id);
    }


}
