package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.LifeItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Transactional
@Repository
public interface LifeItemTypeJpaRepository extends JpaRepository<LifeItemType, Long> {

    List<LifeItemType> getAllByParentId(Long id);

    LifeItemType getItemTypeById(Long id);

}


