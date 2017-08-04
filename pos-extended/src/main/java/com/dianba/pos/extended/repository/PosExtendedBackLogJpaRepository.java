package com.dianba.pos.extended.repository;

import com.dianba.pos.extended.po.PosExtendedBackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/8/4.
 */
@Repository
public interface PosExtendedBackLogJpaRepository extends JpaRepository<PosExtendedBackLog,Long> {
}
