package com.dianba.pos.item.service;

import com.dianba.pos.item.po.ItemTemplate;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
public interface ItemTemplateManager {


    /**
     * 获取分类商品列表
     * @param id
     * @return
     */
    List<ItemTemplate> getAllById(Long id);

    /**
     * 根据code码搜索商品信息
     * @param barcode
     * @return
     */
    ItemTemplate getItemTemplateByBarcode(String barcode);

    ItemTemplate getItemTemplateById(Long id);

    /***
     * 判断模板名字是否重复
     * @param
     * @return
     */
    ItemTemplate getItemTemplateByName(String name);
}
