package com.dianba.pos.merchant.service;

import com.dianba.pos.merchant.po.Merchant;

public interface MerchantManager {


    Merchant findById(Long merchantId);
}
