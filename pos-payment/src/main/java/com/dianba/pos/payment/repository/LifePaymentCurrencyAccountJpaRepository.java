package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.LifePaymentCurrencyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePaymentCurrencyAccountJpaRepository extends JpaRepository<LifePaymentCurrencyAccount, Long> {

    LifePaymentCurrencyAccount findByPassportIdAndChannelIdAndCurrencyType(Long passportId
            , Long channelId, Integer currencyType);
}
