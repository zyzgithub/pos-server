package com.dianba.pos.menu.service.impl;

import com.dianba.pos.menu.po.PosItem;
import com.dianba.pos.menu.repository.PosItemJpaRepository;
import com.dianba.pos.menu.service.PosItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Service
public class DefaultPosItemManager implements PosItemManager {

    @Autowired
    private PosItemJpaRepository posItemJpaRepository;

    @Override
    public List<PosItem> getAllByPosTypeId(Long posTypeId) {
        return posItemJpaRepository.getAllByPosTypeId(posTypeId);
    }

    @Override
    public List<PosItem> getAllByPassportIdAndItemTypeId(Long passportId, Long itemTypeId) {
        return posItemJpaRepository.getAllByPassportIdAndItemTypeId(passportId, itemTypeId);
    }


    @Override
    public PosItem getPosItemByPassportIdAndItemTemplateId(Long passportId, Long itemId) {
        return posItemJpaRepository.getPosItemByPassportIdAndItemTemplateId(passportId, itemId);
    }
}
