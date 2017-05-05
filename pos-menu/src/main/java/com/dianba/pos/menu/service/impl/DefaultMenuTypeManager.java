package com.dianba.pos.menu.service.impl;

import com.dianba.pos.menu.po.MenuType;
import com.dianba.pos.menu.repository.MenuTypeJpaRepository;
import com.dianba.pos.menu.service.MenuTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMenuTypeManager implements MenuTypeManager{

    @Autowired
    private MenuTypeJpaRepository menuTypeJpaRepository;

    public List<MenuType> findMenuTypeByIds(List<Integer> typeIds){
        return menuTypeJpaRepository.findAll(typeIds);
    }
}
