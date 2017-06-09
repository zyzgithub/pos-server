package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.service.CreditLoanManager;
import com.dianba.pos.payment.support.PaymentRemoteService;
import com.dianba.pos.payment.vo.CreditLoanQuotaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class DefaultCreditLoanManager extends PaymentRemoteService implements CreditLoanManager {

    @Autowired
    private PassportManager passportManager;

    @Override
    public BasicResult isHaveCreditLoanQuota(Long passportId) throws Exception {
        CreditLoanQuotaVo creditLoanQuotaVo = getQuota(passportId);
        if (creditLoanQuotaVo != null) {
            if (creditLoanQuotaVo.getSurplusQuota().compareTo(BigDecimal.ZERO) <= 0) {
                return BasicResult.createFailResult("商家信用卡可用额度不足！");
            }
            BasicResult basicResult = BasicResult.createSuccessResult();
            basicResult.setResponse(JSONObject.parseObject(JSONObject.toJSON(creditLoanQuotaVo).toString()));
            return basicResult;
        } else {
            return BasicResult.createFailResult("商家信用卡信息不存在！");
        }
    }

    @Override
    public CreditLoanQuotaVo getQuota(Long passportId) throws Exception {
        Passport passport = passportManager.getPassportInfoByCashierId(passportId);
        JSONObject jsonObject = postCreditLoan(GET_QUOTA, passport.getId(), new HashMap<>());
        CreditLoanQuotaVo creditLoanQuotaVo = new CreditLoanQuotaVo();
        creditLoanQuotaVo.setNowQuota(jsonObject.getBigDecimal("now_quota"));
        creditLoanQuotaVo.setSurplusQuota(jsonObject.getBigDecimal("surplus_quota"));
        creditLoanQuotaVo.setAccountPeriodDays(jsonObject.getInteger("account_period_days"));
        creditLoanQuotaVo.setCardName(jsonObject.getString("card_name"));
        creditLoanQuotaVo.setBusType(jsonObject.getString("bus_type"));
        return creditLoanQuotaVo;
    }

    @Override
    public BasicResult submitOrder(Long passportId, Long orderId) throws Exception {
        return null;
    }
}
