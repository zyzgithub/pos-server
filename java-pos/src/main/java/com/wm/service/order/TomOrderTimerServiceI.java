package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

public interface TomOrderTimerServiceI  extends CommonService{
	public void createOrUpdate(Integer orderId,Integer merchantId,Integer createTime);
}
