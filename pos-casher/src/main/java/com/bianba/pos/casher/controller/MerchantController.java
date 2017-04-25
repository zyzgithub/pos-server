package com.bianba.pos.casher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/25 0025.
 *  商家控制器
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController
{

    /**
     *  获取商家盈利信息
     使用商米POS超过6个月，最近6个月内商家每月盈利额=（月营业额-月成本）的平均数
     使用商米POS未超过6个月,已开店月数商家每月盈利额=（月营业额-月成本）的平均数
     注册商家端超过6个月，最近6个月内商家每月进货额的平均数
     注册商家端未超过6个月,已开店月数商家每月进货额的平均数
     注册商家端超过6个月，最近6个月内商家每月进货次数的平均数
     注册商家端未超过6个月,已开店月数商家每月进货次数的平均数
     使用商米POS超过6个月，最近6个月内商家每日余额的平均数
     使用商米POS未超过6个月,已开店月数商家每日余额的平均数
     */
    @RequestMapping(value="getMerchantProfit",method = {})
    public  void getMerchantProfit(){



    }
}
