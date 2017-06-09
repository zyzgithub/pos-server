package com.dianba.pos.payment.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.CreditLoanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PaymentURLConstant.CREDIT_LOAN)
public class CreditLoanController {

    @Autowired
    private CreditLoanManager creditLoanManager;

    @ResponseBody
    @RequestMapping("vaild")
    public BasicResult creditLoanVaild(Long passportId) throws Exception {
        return creditLoanManager.isHaveCreditLoanQuota(passportId);
    }

    @ResponseBody
    @RequestMapping("submit_order")
    public BasicResult submitOrder(Long passportId, Long orderId, String paymentPassword) throws Exception {
        return creditLoanManager.submitOrder(passportId, orderId, paymentPassword);
    }
}
