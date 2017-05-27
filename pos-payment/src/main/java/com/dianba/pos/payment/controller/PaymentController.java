package com.dianba.pos.payment.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.PaymentManager;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(PaymentURLConstant.PAYMENT_ORDER)
public class PaymentController extends BasicWebService {

    @Autowired
    private PaymentManager paymentManager;

    /**
     * 支付回调（统一渠道)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("notify")
    public JSONObject notify(HttpServletRequest request) {
        System.out.println(request.getParameterMap());
        return null;
    }

    @ResponseBody
    @RequestMapping("pay_order")
    public BasicResult payOrder() {
        long passportId = getLongParameter("passportId");
        long orderId = getLongParameter("orderId");
        String paymentTypeKey = PaymentTypeEnum.getPaymentTypeEnum(getUTF("paymentTypeId")).getKey();
        double transAmount = getDoubleParameter("transTotalAmount");
        long transTotalAmount = (int) (transAmount * 100);
        String transTitle = getUTF("transTitle");
        BasicResult basicResult = paymentManager.payOrder(passportId, orderId
                , paymentTypeKey, TransTypeEnum.PAYMENT.getKey()
                , transTotalAmount, transTitle);
        if (!basicResult.isSuccess()) {
            System.out.println();
        }
        return basicResult;
    }

}
