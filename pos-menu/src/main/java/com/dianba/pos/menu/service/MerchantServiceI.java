package com.dianba.pos.menu.service;

import com.dianba.pos.menu.mapper.MerchantMapper;
import com.dianba.pos.menu.po.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
@Service
public interface MerchantServiceI {

   Merchant getInfoById(Long id);

}
