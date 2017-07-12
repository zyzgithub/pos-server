package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosPushLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhangyong on 2017/7/12.
 */
@Transactional
@Repository
public interface PosPushLogJpaRepository extends JpaRepository<PosPushLog,Long> {
}
