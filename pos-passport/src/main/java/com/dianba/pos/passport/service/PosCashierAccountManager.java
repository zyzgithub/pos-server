package com.dianba.pos.passport.service;

import com.dianba.pos.passport.po.PosCashierAccount;

import java.util.List;

/**
 * Created by zhangyong on 2017/7/11.
 */
public interface PosCashierAccountManager {
    List<PosCashierAccount> findAllByMerchantIdAndAccountType(Long merchantId, Integer type);
    List<PosCashierAccount> findAllByMerchantId(Long merchantId);
}
