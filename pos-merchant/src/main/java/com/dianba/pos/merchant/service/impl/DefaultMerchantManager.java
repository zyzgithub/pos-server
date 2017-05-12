package com.dianba.pos.merchant.service.impl;

import com.dianba.pos.merchant.po.Merchant;
import com.dianba.pos.merchant.repository.MerchantJpaRepository;
import com.dianba.pos.merchant.service.MerchantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultMerchantManager implements MerchantManager{


    @Autowired
    private MerchantJpaRepository merchantJpaRepository;

    @Override
    public Merchant findById(Long merchantId) {
        return merchantJpaRepository.findById(merchantId);
    }
}
