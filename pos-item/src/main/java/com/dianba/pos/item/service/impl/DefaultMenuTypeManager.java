package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.MenuType;
import com.dianba.pos.item.repository.MenuTypeJpaRepository;
import com.dianba.pos.item.service.MenuTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMenuTypeManager implements MenuTypeManager {

    @Autowired
    private MenuTypeJpaRepository menuTypeJpaRepository;

    public List<MenuType> findMenuTypeByIds(List<Integer> typeIds) {
        return menuTypeJpaRepository.findAll(typeIds);
    }
}
