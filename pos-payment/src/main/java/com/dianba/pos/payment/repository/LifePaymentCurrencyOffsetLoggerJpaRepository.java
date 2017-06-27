package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.LifePaymentCurrencyOffsetLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePaymentCurrencyOffsetLoggerJpaRepository
        extends JpaRepository<LifePaymentCurrencyOffsetLogger, Long> {
}
