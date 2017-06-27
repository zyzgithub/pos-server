package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/6/13.
 */
@Repository
public interface YunPosJpaRepository extends JpaRepository<PosLog,Long> {
}
