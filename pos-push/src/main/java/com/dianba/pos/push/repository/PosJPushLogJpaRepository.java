package com.dianba.pos.push.repository;

import com.dianba.pos.push.po.PosJPushLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/8/9.
 */
@Repository
public interface PosJPushLogJpaRepository extends JpaRepository<PosJPushLog,Long> {
}
