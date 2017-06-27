package com.dianba.pos.extended.repository;

import com.dianba.pos.extended.po.PosPhoneInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/6/16.
 */
@Repository
public interface PosPhoneInfoJpaRepository extends JpaRepository<PosPhoneInfo, Long> {
}
