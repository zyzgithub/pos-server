package com.dianba.pos.payment.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.PosRewardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PaymentURLConstant.REWARD)
public class RewardController {

    @Autowired
    private PosRewardManager posRewardManager;

    @ResponseBody
    @RequestMapping()
    public BasicResult getTotalRewardAmount() {
        return BasicResult.createSuccessResult();
    }
}
