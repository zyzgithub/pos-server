package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.PosRewardLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosRewardLoggerJpaRepository extends JpaRepository<PosRewardLogger, Long> {
}
