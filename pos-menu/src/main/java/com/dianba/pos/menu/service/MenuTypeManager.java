package com.dianba.pos.menu.service;

import com.dianba.pos.menu.po.MenuType;

import java.util.List;

public interface MenuTypeManager {

    List<MenuType> findMenuTypeByIds(List<Integer> typeIds);
}
