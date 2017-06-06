package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosMerchantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosMerchantTypeJpaRepository extends JpaRepository<PosMerchantType, Long> {
}
