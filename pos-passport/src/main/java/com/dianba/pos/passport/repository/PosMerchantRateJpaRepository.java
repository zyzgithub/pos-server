package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosMerchantRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosMerchantRateJpaRepository extends JpaRepository<PosMerchantRate, Long> {

}
