package com.dianba.pos.casher.controller;

import com.dianba.pos.menu.mapper.MenuMapper;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.po.Merchant;
import com.dianba.pos.menu.po.Order;

import com.dianba.pos.menu.service.MerchantServiceI;
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
//    @Autowired
//    private MerchantServiceI merchantServiceI;


    /**
     * 获取商家盈利信息
     * 使用商米POS超过6个月，最近6个月内商家每月盈利额=（月营业额-月成本）的平均数
     * 使用商米POS未超过6个月,已开店月数商家每月盈利额=（月营业额-月成本）的平均数
     * 注册商家端超过6个月，最近6个月内商家每月进货额的平均数
     * 注册商家端未超过6个月,已开店月数商家每月进货额的平均数
     * 注册商家端超过6个月，最近6个月内商家每月进货次数的平均数
     * 注册商家端未超过6个月,已开店月数商家每月进货次数的平均数
     * 使用商米POS超过6个月，最近6个月内商家每日余额的平均数
     * 使用商米POS未超过6个月,已开店月数商家每日余额的平均数
     */
    @ResponseBody
    @RequestMapping(value = "getMerchantProfit")
    public void getMerchantProfit(Long id) {

        //先获取商家信息
      //  Merchant mc=merchantServiceI.getInfoById(id);

        //获取商家使用商米pos机的时间


      //  Menu menu = menuMapper.selectByPrimaryKey(25l);



    }
}
