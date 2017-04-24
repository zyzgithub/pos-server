package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

public interface EatInOrderServiceI extends CommonService{
	
	/**
	 * 修改eatin_order表 的status状态为pay
	 * @param orderId
	 */
	public void updateStatus (Integer orderId);
	
}
