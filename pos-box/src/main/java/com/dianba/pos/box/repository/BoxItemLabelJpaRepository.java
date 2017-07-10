package com.dianba.pos.box.repository;

import com.dianba.pos.box.po.BoxItemLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/7/4.
 */
@Repository
public interface BoxItemLabelJpaRepository extends JpaRepository<BoxItemLabel, Long> {
}
