package com.dianba.pos.item.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.po.PosType;
import com.dianba.pos.item.repository.PosItemJpaRepository;
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


    @Autowired
    private PosItemJpaRepository posItemJpaRepository;

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

    @Override
    public BasicResult deletePosType(Long passportId, Long posTypeId) {
        PosType posType = posTypeJpaRepository.findOne(posTypeId);
        if (posType != null && posType.getPassportId().equals(passportId)) {

            posTypeJpaRepository.delete(posType);
            //删除商品
            List<PosItem> posItems=posItemJpaRepository.getAllByPosTypeId(posTypeId);

            for(PosItem posItem :posItems){

                if(posItem!=null){

                    posItemJpaRepository.delete(posItem);
                }
            }
            return BasicResult.createSuccessResult("删除商家分类成功!");
        } else {

            return BasicResult.createFailResult("删除商家分类异常!");
        }
    }
}
