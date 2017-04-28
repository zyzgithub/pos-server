package com.dianba.pos.menu.service.impl;

import com.dianba.pos.menu.mapper.MerchantMapper;
import com.dianba.pos.menu.po.Merchant;
import com.dianba.pos.menu.service.MerchantServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
@Service
public class MerchantServiceImpl implements MerchantServiceI {

    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public Merchant getInfoById(Long id) {
        return merchantMapper.selectByPrimaryKey(id);
    }
}
