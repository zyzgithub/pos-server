package com.dianba.pos.item.repository;


import com.dianba.pos.item.po.ItemUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Transactional
@Repository
public interface ItemUnitJpaRepository extends JpaRepository<ItemUnit,Integer> {

    ItemUnit getItemUnitById(Long id);
}
