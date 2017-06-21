package com.dianba.pos.item.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.po.PosType;
import com.dianba.pos.item.repository.PosItemJpaRepository;
import com.dianba.pos.item.repository.PosTypeJpaRepository;
import com.dianba.pos.item.service.PosTypeManager;
import com.dianba.pos.passport.mapper.PassportMapper;
import com.dianba.pos.passport.po.Passport;
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

    @Autowired
    private PassportMapper passportMapper;
    @Override
    public List<PosType> getAllByPassportId(Long passportId) {
        Passport passport=passportMapper.getPassportInfoByCashierId(passportId);
        return posTypeJpaRepository.getAllByPassportId(passport.getId());


    }

    @Override
    public PosType getPosTypeByPassportIdAndItemTypeTitle(Long passportId, String title) {
        Passport passport=passportMapper.getPassportInfoByCashierId(passportId);
        return posTypeJpaRepository.getPosTypeByPassportIdAndItemTypeTitle(passport.getId(), title);
    }

    @Override
    public PosType getPosTypeById(Long id) {
        return posTypeJpaRepository.getPosTypeById(id);
    }

    @Override
    public BasicResult deletePosType(Long passportId, Long posTypeId) {
        PosType posType = posTypeJpaRepository.findOne(posTypeId);
        Passport passport=passportMapper.getPassportInfoByCashierId(passportId);
        if (posType != null && posType.getPassportId().equals(passport.getId())) {

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
