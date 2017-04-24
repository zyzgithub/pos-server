package com.wm.service.impl.order.refund;

import java.math.BigDecimal;
import java.util.UUID;

import javax.annotation.Resource;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.refund.nopwd.UnificationAlipayRefundUtil;
import org.springframework.stereotype.Component;

import com.alipay.refund.FastpayRefundParam;
import com.alipay.refund.nopwd.NopwdFastpayRefundAction;
import com.alipay.refund.nopwd.NopwdFastpayRefundApplyResult;
import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.service.order.refund.OrderRefundExecutor;

@Component("alipayOrderRefundExecutor")
public class AlipayOrderRefundExecutor implements OrderRefundExecutor {

    @Resource
    private NopwdFastpayRefundAction nopwdFastpayRefundAction;

    @Override
    public OrderRefundResult execute(DineInOrderRefundParam orderRefundParam) {
        FastpayRefundParam fastpayRefundParam = new FastpayRefundParam(orderRefundParam.getOrder().getOutTraceId(),
                new BigDecimal(orderRefundParam.getRefundFee()));
        NopwdFastpayRefundApplyResult result = nopwdFastpayRefundAction.apply(fastpayRefundParam);
        if (!result.isSuccess()) {
            String batchNo = UUID.randomUUID().toString();
            AlipayTradeRefundResponse refundResponse = UnificationAlipayRefundUtil.apply(fastpayRefundParam.getTotalRefund().toString(), null, fastpayRefundParam.getTradeNo(), fastpayRefundParam.getRefundReason(), batchNo);
            return new OrderRefundResult(refundResponse.isSuccess(), batchNo);
        }
        return new OrderRefundResult(result.isSuccess(), result.getBatchNo());
    }

}
