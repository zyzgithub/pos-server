package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.ItemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Transactional
@Repository
public interface ItemTemplateJpaRepository extends JpaRepository<ItemTemplate,Integer> {


    /**
     * 获取分类商品列表
     * @param id
     * @return
     */

    List<ItemTemplate> getAllById(Long id);

    /**
     * 根据code码搜索商品信息
     * @param barcode
     * @return
     */
    @Query("SELECT itemp FROM ItemTemplate itemp where itemp.barcode=:barcode and "
            +"itemp.ascriptionType=1")
    ItemTemplate getItemTemplateByBarcode(@Param("barcode") String barcode);
}
