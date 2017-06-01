package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.PosType;
import com.dianba.pos.item.repository.PosTypeJpaRepository;
import com.dianba.pos.item.service.PosTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Service
public class DefaultPosTypeManager implements PosTypeManager {

    @Autowired
    private PosTypeJpaRepository posTypeJpaRepository;
    @Override
    public List<PosType> getAllByPassportId(Long passportId) {
        return posTypeJpaRepository.getAllByPassportId(passportId);
    }

    @Override
    public PosType getPosTypeByPassportIdAndItemTypeTitle(Long passportId, String title) {
        return posTypeJpaRepository.getPosTypeByPassportIdAndItemTypeTitle(passportId, title);
    }

    @Override
    public PosType getPosTypeById(Long id) {
        return posTypeJpaRepository.getPosTypeById(id);
    }
}
