package com.dianba.pos.casher.controller;

import com.dianba.pos.menu.service.MerchantServiceI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class BaseController
{
    @Autowired
    protected MerchantServiceI merchantServiceI;
}
