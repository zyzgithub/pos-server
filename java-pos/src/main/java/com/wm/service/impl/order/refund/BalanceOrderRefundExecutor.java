package com.wm.service.impl.order.refund;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.entity.order.OrderEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.order.refund.OrderRefundExecutor;

@Component("balanceOrderRefundExecutor")
public class BalanceOrderRefundExecutor implements OrderRefundExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BalanceOrderRefundExecutor.class);

	@Resource
	private FlowServiceI flowService;

	@Override
	public OrderRefundResult execute(DineInOrderRefundParam orderRefundParam) {
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		try {
			OrderEntity order = orderRefundParam.getOrder();
			flowService.refundFlowCreate(order.getWuser().getId(), Double.parseDouble(orderRefundParam.getRefundFee()),
					order.getId());
			return OrderRefundResult.successResult(outRefundNo);
		} catch (Exception e) {
			LOGGER.info("blance refund exception.", e);
		}
		return OrderRefundResult.failResult(outRefundNo);
	}

}
