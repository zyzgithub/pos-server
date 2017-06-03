package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.PosItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Transactional
@Repository
public interface PosItemJpaRepository extends JpaRepository<PosItem, Integer> {


    /**
     * 获取商家商品
     *
     * @param
     * @param
     * @return
     */
    @Query("SELECT pi FROM PosItem pi where pi.posTypeId=:posTypeId order by pi.buyCount desc")
    List<PosItem> getAllByPosTypeId(@Param("posTypeId") Long posTypeId);

    /**
     * 获取商家商品信息
     *
     * @param passportId
     * @return
     */
    @Query("select pi from PosItem  pi where pi.passportId=:passportId order by pi"
            + ".buyCount desc")
    List<PosItem> getAllByPassportId(@Param("passportId") Long passportId);

    /**
     * 获取商家商品分类所有
     *
     * @param passportId
     * @return
     */
    @Query("select pi from PosItem  pi where pi.passportId=:passportId and pi.itemTypeId=:itemTypeId order by pi"
            + ".buyCount desc")
    List<PosItem> getAllByPassportIdAndItemTypeId(@Param("passportId") Long passportId
            , @Param("itemTypeId") Long itemTypeId);


    /***
     * 查询此商家是否有此模板商品
     * @param passportId
     * @param itemId
     * @return
     */
    PosItem getPosItemByPassportIdAndItemTemplateId(Long passportId, Long itemId);


    PosItem getPosItemById(Long id);

    @Query("SELECT pi FROM PosItem  pi WHERE pi.barcode LIKE :barcode and pi.passportId=:passportId")
    List<PosItem> findAllByBarcodeLikeAndPassportId(@Param("barcode") String barcode
            , @Param("passportId") Long passportId);

    @Query("SELECT pi FROM PosItem  pi WHERE pi.itemName LIKE CONCAT('%',:itemName,'%') and pi.passportId=:passportId")
    List<PosItem> findAllByItemNameLikeAndPassportId(@Param("itemName") String itemName
            , @Param("passportId") Long passportId);

    /**
     * 获取预警库存商品
     *
     * @param passportId
     * @param itemTemplateIds
     * @return
     */
    @Query("select a from PosItem a where a.warningRepertory<a.repertory and a.passportId=:passportId "
            + " and a.itemTemplateId not in(:itemTemplateIds)")
    List<PosItem> findWarningRepertoryItemsByExclude(@Param("passportId") Long passportId
            , @Param("itemTemplateIds") List<Long> itemTemplateIds);

    PosItem getPosItemByPassportIdAndBarcode(Long passportId,String barcode);
}


