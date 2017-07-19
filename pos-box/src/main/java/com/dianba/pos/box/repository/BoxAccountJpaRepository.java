package com.dianba.pos.box.repository;

import com.dianba.pos.box.po.BoxAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangyong on 2017/7/17.
 */
@Repository
public interface BoxAccountJpaRepository extends JpaRepository<BoxAccount,Long> {
    BoxAccount findByOpenId(String openId);
}
