package com.dianba.pos.casher.controller;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.service.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Created by Administrator on 2017/4/25 0025.
 * zyz
 * 商家控制器
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {


    @Autowired
   private OrderManager orderManager;
    @ResponseBody
    @RequestMapping(value = "getMerchantProfitInfo")
    public BasicResult getMerchantProfitInfo(Long merchantId,String phone) {
        return orderManager.getMerchantProfitInfo(merchantId,phone);
    }
}
