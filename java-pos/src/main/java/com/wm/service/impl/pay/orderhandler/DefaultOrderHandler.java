package com.wm.service.impl.pay.orderhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wm.entity.order.OrderEntity;
import com.wm.service.pay.OrderHandler;

/**
 * 默认订单回调处理器
 */
@Component("defaultOrderHandler")
public class DefaultOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultOrderHandler.class);

	@Override
	public void handle(OrderEntity order) throws Exception {
		logger.warn("未知订单类型：orderId={}, orderType={}", order.getId(), order.getOrderType());
	}

}
