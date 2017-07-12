package com.dianba.pos.passport.service.impl;

import com.dianba.pos.passport.po.PosCashierAccount;
import com.dianba.pos.passport.repository.PosCashierAccountJpaRepository;
import com.dianba.pos.passport.service.PosCashierAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyong on 2017/7/11.
 */
@Service
public class DefaultPosCashierAccountManager implements PosCashierAccountManager {

    @Autowired
    private PosCashierAccountJpaRepository posCashierAccountJpaRepository;
    @Override
    public List<PosCashierAccount> findAllByMerchantIdAndAccountType(Long merchantId, Integer type) {
        return posCashierAccountJpaRepository.findAllByMerchantIdAndAccountType(merchantId, type);
    }

    @Override
    public List<PosCashierAccount> findAllByMerchantId(Long merchantId) {
        return posCashierAccountJpaRepository.findAllByMerchantId(merchantId);
    }
}
