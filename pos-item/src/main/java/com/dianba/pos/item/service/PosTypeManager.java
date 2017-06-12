package com.dianba.pos.item.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.item.po.PosType;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 删除商品商品分类并删除分类以下商品信息
     * @param passportId
     * @param posTypeId
     * @return
     */
    @Transactional
    BasicResult deletePosType(Long passportId, Long posTypeId);
}
