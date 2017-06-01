package com.dianba.pos.item.service;

import com.dianba.pos.item.po.LifeItemTemplate;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
public interface LifeItemTemplateManager {


    /**
     * 获取分类商品列表
     * @param id
     * @return
     */
    List<LifeItemTemplate> getAllById(Long id);

    /**
     * 根据code码搜索商品信息
     * @param barcode
     * @return
     */
    LifeItemTemplate getItemTemplateByBarcode(String barcode);

    LifeItemTemplate getItemTemplateById(Long id);

    /***
     * 判断模板名字是否重复
     * @param
     * @return
     */
    LifeItemTemplate getItemTemplateByName(String name);
}
