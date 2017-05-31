package com.dianba.pos.menu.controller;

import com.dianba.pos.item.repository.ItemTemplateJpaRepository;
import com.dianba.pos.item.repository.ItemTypeJpaRepository;
import com.dianba.pos.item.repository.ItemUnitJpaRepository;
import com.dianba.pos.item.service.ItemTemplateManager;
import com.dianba.pos.item.service.ItemTypeManager;
import com.dianba.pos.item.service.ItemUnitManager;
import com.dianba.pos.item.service.impl.DefaultItemUnitManager;
import com.dianba.pos.menu.repository.PosItemJpaRepository;
import com.dianba.pos.menu.repository.PosTypeJpaRepository;
import com.dianba.pos.menu.service.PosItemManager;
import com.dianba.pos.menu.service.PosTypeManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangyong on 2017/5/27.
 */
public class BaseController {

    @Autowired
    protected ItemTypeManager itemTypeManager;

    @Autowired
    protected ItemUnitManager itemUnitManager;
    @Autowired
    protected ItemUnitJpaRepository itemUnitJpaRepository;

    @Autowired
    protected ItemTypeJpaRepository itemTypeJpaRepository;

    @Autowired
    protected PosItemJpaRepository posItemJpaRepository;

    @Autowired
    protected ItemTemplateManager itemTemplateManager;

    @Autowired
    protected ItemTemplateJpaRepository itemTemplateJpaRepository;

    @Autowired
    protected PosTypeJpaRepository posTypeJpaRepository;

    @Autowired
    protected PosTypeManager posTypeManager;

    @Autowired
    protected PosItemManager posItemManager;
}
