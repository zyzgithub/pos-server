package com.dianba.pos.payment.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.PosRewardManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api("商家返利控制器")
@Controller
@RequestMapping(PaymentURLConstant.REWARD)
public class RewardController {

    @Autowired
    private PosRewardManager posRewardManager;

    @ApiOperation("获取商家今日销售返利金额")
    @ApiImplicitParam(name = "passportId", value = "商家通行证Id", required = true)
    @ResponseBody
    @RequestMapping(value = "get_reward_amount", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult getTotalRewardAmount(Long passportId) {
        return posRewardManager.getTotalRewarAmountByDate(passportId, DateUtil.getNow());
    }
}
