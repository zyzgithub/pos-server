package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

public interface DineInDiscountLogServiceI extends CommonService{

	public void createLog (Integer orderId,Double onlineMoney);
}
