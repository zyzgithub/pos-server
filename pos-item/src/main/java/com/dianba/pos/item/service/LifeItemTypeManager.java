package com.dianba.pos.item.service;

import com.dianba.pos.item.po.LifeItemType;
import com.xlibao.metadata.item.ItemType;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
public interface LifeItemTypeManager {

    List<ItemType> getItemList();


    LifeItemType getItemTypeById(Long id);

}
