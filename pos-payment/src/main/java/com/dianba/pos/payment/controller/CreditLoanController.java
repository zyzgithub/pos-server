package com.dianba.pos.payment.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.CreditLoanManager;
import com.dianba.pos.payment.vo.CreditLoanQuotaVo;
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
        CreditLoanQuotaVo creditLoanQuotaVo = creditLoanManager.getQuota(passportId);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponse(JSONObject.parseObject(JSONObject.toJSON(creditLoanQuotaVo).toString()));
        return basicResult;
    }
}
