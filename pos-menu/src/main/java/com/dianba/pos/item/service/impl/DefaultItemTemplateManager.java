package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.ItemTemplate;
import com.dianba.pos.item.repository.ItemTemplateJpaRepository;
import com.dianba.pos.item.service.ItemTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Service
public class DefaultItemTemplateManager implements ItemTemplateManager {

    @Autowired
    private ItemTemplateJpaRepository itemTemplateJpaRepository;
    @Override
    public List<ItemTemplate> getAllById(Long id) {
        return itemTemplateJpaRepository.getAllById(id);
    }

    @Override
    public ItemTemplate getItemTemplateByBarcode(String barcode) {
        return itemTemplateJpaRepository.getItemTemplateByBarcode(barcode);
    }
}
