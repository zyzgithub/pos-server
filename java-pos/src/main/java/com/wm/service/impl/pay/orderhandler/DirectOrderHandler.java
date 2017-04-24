package com.wm.service.impl.pay.orderhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wm.entity.order.OrderEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.pay.OrderHandler;

/**
 * 直接支付订单回调处理器
 */
@Component("directOrderHandler")
public class DirectOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(DirectOrderHandler.class);

	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	
	@Override
	public void handle(OrderEntity order) throws Exception {
		logger.info("handle direct order. orderId={}", order.getId());
		orderIncomeService.createOrderIncome(order);
	}

}
