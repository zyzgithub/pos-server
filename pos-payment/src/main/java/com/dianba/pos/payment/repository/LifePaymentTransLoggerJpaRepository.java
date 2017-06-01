package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.LifePaymentTransactionLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePaymentTransLoggerJpaRepository extends JpaRepository<LifePaymentTransactionLogger, Long> {

}
