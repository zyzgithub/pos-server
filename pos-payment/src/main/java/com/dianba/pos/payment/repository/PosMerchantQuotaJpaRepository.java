package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.PosMerchantQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosMerchantQuotaJpaRepository extends JpaRepository<PosMerchantQuota, Long> {

    PosMerchantQuota findByPassportIdAndRewardTypeAndPeriod(Long passportId, Integer rewardType, Integer period);
}
