package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.PosMerchantRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosMerchantRateJpaRepository extends JpaRepository<PosMerchantRate, Long> {

}
