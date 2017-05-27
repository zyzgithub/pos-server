package com.dianba.pos.menu.service;

import com.dianba.pos.menu.po.PosItem;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */

public interface PosItemManager {


    List<PosItem> getAllByPosTypeId(@Param("posTypeId") Long posTypeId);

    List<PosItem> getAllByPassportIdAndItemTypeId(@Param("passportId") Long passportId,@Param("itemTypeId") Long itemTypeId);

    /***
     * 查询此商家是否有此模板商品
     * @param passportId
     * @param itemId
     * @return
     */
    PosItem getPosItemByPassportIdAndItemTemplateId(Long passportId,Long itemId);

}
