package com.dianba.pos.payment.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.vo.CreditLoanQuotaVo;

import java.math.BigDecimal;

public interface CreditLoanManager {

    String BASE_URL = "loan/";

    String GET_QUOTA = BASE_URL + "business/getquota";

    String SUBMIT_ORDER = BASE_URL + "order/submitorder";

    String QUOTA_CALCULATION = "common/quotacalculation";

    BasicResult isHaveCreditLoanQuota(Long passportId) throws Exception;

    CreditLoanQuotaVo getQuota(Long passportId) throws Exception;

    BasicResult submitOrder(Long passportId, Long orderId, String paymentPassword) throws Exception;

    BasicResult calculationPayAmount(Long passportId, BigDecimal orderAmount);
}
