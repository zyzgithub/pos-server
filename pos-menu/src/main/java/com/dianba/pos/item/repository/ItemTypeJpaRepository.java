package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zhangyong on 2017/5/24.
 */
@Transactional
@Repository
public interface ItemTypeJpaRepository extends JpaRepository<ItemType, Integer> {

    List<ItemType> getAllByParentId(Long id);


    ItemType getItemTypeById(Long id);

}


