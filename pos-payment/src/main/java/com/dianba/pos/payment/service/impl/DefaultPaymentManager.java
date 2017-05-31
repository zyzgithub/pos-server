package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.support.PaymentRemoteService;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultPaymentManager extends PaymentRemoteService implements PaymentManager {

    @Autowired
    private OrderManager orderManager;


    public BasicResult payOrder(long passportId, long orderId
            , String paymentType, TransTypeEnum transType, long transTotalAmount) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //支付类型
        params.put("paymentType", paymentType);
        //交易类型
        params.put("transType", transType.getKey() + "");
        //合作商户ID
        params.put("partnerUserId", passportId + "");
        //订单ID/订单批次编号
        params.put("partnerTradeNumber", orderId + "");
        //交易单位金额
        params.put("transUnitAmount", String.valueOf(transTotalAmount));
        //交易单位数量
        params.put("transNumber", "1");
        //交易总金额
        params.put("transTotalAmount", String.valueOf(transTotalAmount));
        //交易标题
        params.put("transTitle", transType.getValue());
        //交易备注
        params.put("remark", "POS-" + transType.getValue());
        //是否使用优惠券
        params.put("useConpon", "0");
        //优惠额度
        params.put("discountAmount", "0");
        return postPayWithCallBack(UNIFIED_ORDER, PaymentURLConstant.PAYMENT_ORDER + "notify", params);
    }

    @Override
    public BasicResult offsetBalance(long passportId, String transSequenceNumber
            , long offsetAmount, TransTypeEnum transTypeEnum) {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", String.valueOf(passportId));
        //余额偏移数额
        params.put("offsetAmount", offsetAmount + "");
        //交易标题
        params.put("transTitle", transTypeEnum.getValue());
        //交易类型
        params.put("transType", transTypeEnum.getKey() + "");
        //交易订单号
        params.put("transSequenceNumber", transSequenceNumber + "");
        return postPay(OFFSET_BALANCE, params);
    }
}
