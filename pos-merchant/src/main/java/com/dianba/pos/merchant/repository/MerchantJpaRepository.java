package com.dianba.pos.merchant.repository;

import com.dianba.pos.merchant.po.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantJpaRepository extends JpaRepository<Merchant, Long> {

    Merchant findByUserId(Long userId);
}
