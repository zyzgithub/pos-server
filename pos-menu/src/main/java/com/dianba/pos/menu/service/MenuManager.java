package com.dianba.pos.menu.service;

import com.dianba.pos.menu.po.Menu;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
public interface MenuManager {


    /**
     * 增值服务商品信息
     * @param
     * @param is_flash
     * @return
     */
       List<Menu> findAllByIsFlashAndMerchantId(Integer is_flash,Integer merchant_id);
}
