package com.dianba.pos.item.service;

import com.dianba.pos.item.po.MenuType;

import java.util.List;

public interface MenuTypeManager {

    List<MenuType> findMenuTypeByIds(List<Integer> typeIds);
}
