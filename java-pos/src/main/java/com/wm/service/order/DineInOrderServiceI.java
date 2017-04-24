package com.wm.service.order;

import org.jeecgframework.core.common.model.json.AjaxJson;

/**
 * 堂食订单服务接口，用于包装处于事务管理下的方法调用（在必要时回滚事务），并转换返回结果。
 */
public interface DineInOrderServiceI {

	/**
	 * 堂食订单退单
	 * @param orderId 订单ID
	 * @param opUserId 操作人ID
	 * @return 退单结果
	 */
	AjaxJson chargeback(Integer orderId, Integer opUserId);
	
}
