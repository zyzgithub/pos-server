package com.wm.service.pay;

import com.wm.entity.order.OrderEntity;

/**
 * 订单回调处理器
 */
public interface OrderHandler {

	/**
	 * 处理订单回调
	 * @param order
	 * @throws Exception 
	 */
	void handle(OrderEntity order) throws Exception;
	
}
