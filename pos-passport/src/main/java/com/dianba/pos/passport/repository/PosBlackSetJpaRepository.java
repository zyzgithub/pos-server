package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosBlackSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/7/19.
 */
@Repository
public interface PosBlackSetJpaRepository extends JpaRepository<PosBlackSet,Long> {
    PosBlackSet findByStateAndType(Integer state,Integer type);
}
