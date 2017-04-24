package com.wm.service.order;


import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.orderlimitlog.OrderLimitLogEntity;


public interface OrderLimitLogServiceI extends CommonService {

	public void updateOrderLimitLog (OrderLimitLogEntity OrderLimitLog);

	public OrderLimitLogEntity getOrderLimitLogToday(Integer userId, Integer merchantId,
			String payType);

	public void createOrderLimitLog(OrderLimitLogEntity orderLimitLog);
}