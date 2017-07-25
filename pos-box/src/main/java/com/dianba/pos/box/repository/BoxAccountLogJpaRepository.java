package com.dianba.pos.box.repository;

import com.dianba.pos.box.po.BoxAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxAccountLogJpaRepository extends JpaRepository<BoxAccountLog, Long> {

    BoxAccountLog findByOpenIdAndLeaveTimeIsNull(String openId);
}
