package com.dianba.pos.passport.repository;

import com.dianba.pos.passport.po.PosCashierAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Transactional
@Repository
public interface PosCashierAccountJpaRepository extends JpaRepository<PosCashierAccount,Long> {

    List<PosCashierAccount> findAllByMerchantId(Long merchantId);


    PosCashierAccount findPosCashierAccountByCashierId(Long cashierId);


    PosCashierAccount findPosCashierAccountByMerchantIdAndAccountType(Long merchantId,Integer accountType);
}
