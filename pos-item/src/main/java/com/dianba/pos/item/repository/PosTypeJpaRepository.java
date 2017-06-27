package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.PosType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Transactional
@Repository
public interface PosTypeJpaRepository extends JpaRepository<PosType,Long> {


    @Query("select pt from PosType  pt where pt.passportId =:passportId  ORDER BY pt.sort")
    List<PosType> getAllByPassportId(@Param("passportId") Long passportId);

    /**
     * 查询商家是否添加过此分类名称类
     * @param passportId
     * @param title
     * @return
     */
    PosType getPosTypeByPassportIdAndItemTypeTitle(Long passportId, String title);

    PosType getPosTypeById(Long id);
}
