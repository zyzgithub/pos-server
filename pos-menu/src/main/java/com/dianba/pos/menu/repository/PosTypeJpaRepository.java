package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.PosType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Transactional
@Repository
public interface PosTypeJpaRepository extends JpaRepository<PosType,Integer> {

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
