package com.dianba.pos.item.service;

import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.vo.PosItemVo;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/5/24.
 */

public interface PosItemManager {


    List<PosItem> getAllByPosTypeId(@Param("posTypeId") Long posTypeId);

    List<PosItem> getAllByPassportIdAndItemTypeId(@Param("passportId") Long passportId
            , @Param("itemTypeId") Long itemTypeId);

    List<PosItem> getAllByPassportId(@Param("passportId") Long passportId);

    /***
     * 查询此商家是否有此模板商品
     * @param passportId
     * @param itemId
     * @return
     */
    PosItem getPosItemByPassportIdAndItemTemplateId(Long passportId, Long itemId);


    PosItemVo getItemByBarcode(String barcode, String passportId);


    /**
     * 商品入库
     *
     * @param posItemVo
     * @return
     */
    @Transactional
    Map<String, Object> itemStorage(PosItemVo posItemVo);

    PosItem getPosItemById(Long id);

    /**
     * 根据商品条形码或者名称搜索商品信息
     **/

    List<PosItem> findAllBySearchTextPassportId(String searchText, Long passportId);


    /**
     * 商品编辑
     *
     * @param posItemVo
     * @return
     */
    @Transactional
    Map<String, Object> editPosItem(PosItemVo posItemVo);


}
