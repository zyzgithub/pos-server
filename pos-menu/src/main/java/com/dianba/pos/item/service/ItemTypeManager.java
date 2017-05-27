package com.dianba.pos.item.service;

import com.xlibao.metadata.item.ItemType;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
public interface ItemTypeManager {

    List<ItemType> getItemList();


    com.dianba.pos.item.po.ItemType getItemTypeById(Long id);

}
