package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.support.PaymentRemoteService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultPaymentManager extends PaymentRemoteService implements PaymentManager {


    public BasicResult payOrder(long passportId, long orderId
            , String paymentType, int transType, long transTotalAmount, String transTitle) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        params.put("paymentType", paymentType);
        params.put("transType", transType + "");
        params.put("partnerUserId", passportId + "");
        params.put("partnerTradeNumber", orderId + "");
        params.put("transUnitAmount", String.valueOf(transTotalAmount));
        params.put("transNumber", "1");
        params.put("transTotalAmount", String.valueOf(transTotalAmount));
        params.put("transTitle", transTitle);
//        params.put("remark", remark);
//        params.put("useConpon", String.valueOf(useConpon));
//        params.put("discountAmount", String.valueOf(discountAmount));
        return postPayWithCallBack(UNIFIED_ORDER, PaymentURLConstant.PAYMENT_ORDER + "notify", params);
    }
}
