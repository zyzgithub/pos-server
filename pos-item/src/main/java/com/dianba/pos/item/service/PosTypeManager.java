package com.dianba.pos.item.service;

import com.dianba.pos.item.po.PosType;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
public interface PosTypeManager {

    List<PosType> getAllByPassportId(Long passportId);
    /**
     * 查询商家是否添加过此分类名称类
     * @param passportId
     * @param title
     * @return
     */
    PosType getPosTypeByPassportIdAndItemTypeTitle(Long passportId,String title);



    PosType getPosTypeById(Long id);
}
