package com.wm.service.pay;


/**
 * 订单处理工厂
 */
public interface OrderHandleFactory {

	/**
	 * 生成处理器
	 * @param orderType 订单类型
	 * @return
	 */
	OrderHandler getHandler(String orderType);

}
