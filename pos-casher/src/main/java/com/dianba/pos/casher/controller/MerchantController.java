package com.dianba.pos.casher.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.casher.config.CasherUrlConstant;
import com.dianba.pos.order.service.LifeOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/4/25 0025.
 * zyz
 * 商家控制器
 */
@Controller
@RequestMapping(CasherUrlConstant.MERCHANT)
public class MerchantController {

    @Autowired
    private LifeOrderManager orderManager;

    @ResponseBody
    @RequestMapping(value = "getMerchantProfitInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult getMerchantProfitInfo(Long merchantId, String phone) {
        return orderManager.getMerchantProfitInfo(merchantId, phone);
    }
}
