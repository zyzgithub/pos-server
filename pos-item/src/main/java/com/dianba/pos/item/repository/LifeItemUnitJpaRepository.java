package com.dianba.pos.item.repository;


import com.dianba.pos.item.po.LifeItemUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Transactional
@Repository
public interface LifeItemUnitJpaRepository extends JpaRepository<LifeItemUnit,Integer> {

    LifeItemUnit getItemUnitById(Long id);
}
