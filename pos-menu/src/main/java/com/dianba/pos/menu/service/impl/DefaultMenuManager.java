package com.dianba.pos.menu.service.impl;

import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.repository.MenuJpaRepository;
import com.dianba.pos.menu.service.MenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
@Service
public class DefaultMenuManager implements MenuManager {

    @Autowired
    private MenuJpaRepository menuJpaRepository;


    @Override
    public List<Menu> findAllByIsFlashAndMerchantId(Integer is_flash, Integer merchant_id) {
        return menuJpaRepository.findAllByIsFlashAndMerchantId(is_flash, merchant_id);
    }
}
