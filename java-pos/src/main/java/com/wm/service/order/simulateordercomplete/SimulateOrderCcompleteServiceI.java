package com.wm.service.order.simulateordercomplete;


import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface SimulateOrderCcompleteServiceI extends CommonService {
	
	public void orderAlipayDone(int orderId) throws Exception;
	
	public void simulateSettlementOrderHandler(OrderEntity order, String outTraceId,  String orderIds, String payType) throws Exception;

}
