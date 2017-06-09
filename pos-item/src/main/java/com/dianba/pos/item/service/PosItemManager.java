package com.dianba.pos.item.service;

import com.dianba.pos.base.BasicResult;
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

    List<PosItemVo> convertToVos(List<PosItem> posItems);

    PosItemVo convertToVo(PosItem posItem);

    /**
     * 更新商品库存
     *
     * @param itemIdMaps key:id,value:库存偏移量
     */
    void offsetItemRepertory(Map<Long, Integer> itemIdMaps);

    /**
     *  获取商品规格，以及商家商品分类
     * @param passportId
     * @return
     */
    BasicResult getItemUnitAndType(String passportId);

    /**
     * 获取商品分类以及商品信息并显示各类商品数量
     *
     * @param passportId
     * @return
     */
    BasicResult getItemByPassportId(String passportId, String itemTypeId);

    /**
     * 搜索
     * @param searchText
     * @param passportId
     * @return
     */
    BasicResult getListBySearchText(String searchText, Long passportId);
}
