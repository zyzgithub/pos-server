package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.LifeItemTemplate;
import com.dianba.pos.item.repository.LifeItemTemplateJpaRepository;
import com.dianba.pos.item.service.LifeItemTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Service
public class DefaultLifeItemTemplateManager implements LifeItemTemplateManager {

    @Autowired
    private LifeItemTemplateJpaRepository itemTemplateJpaRepository;
    @Override
    public List<LifeItemTemplate> getAllById(Long id) {
        return itemTemplateJpaRepository.getAllById(id);
    }

    @Override
    public LifeItemTemplate getItemTemplateByBarcode(String barcode) {
        return itemTemplateJpaRepository.getItemTemplateByBarcode(barcode);
    }

    @Override
    public LifeItemTemplate getItemTemplateById(Long id) {
        return itemTemplateJpaRepository.getItemTemplateById(id);
    }

    @Override
    public LifeItemTemplate getItemTemplateByName(String name) {
        return itemTemplateJpaRepository.getItemTemplateByName(name);
    }
}
