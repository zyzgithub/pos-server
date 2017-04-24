package com.wm.dao.order;

public interface OrderMenuDao {
	/**
	 * 创建订单对应的默认菜品列表
	 * @param orderId
	 * @return
	 */
	boolean createDefaultOrderMenu(Integer orderId);
}
