package com.wm.dao.order;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface PushedOrderDao extends IGenericBaseCommonDao {

	/**
	 * 根据orderId删除记录
	 * @param orderId 订单ID
	 * @return 修改的记录数
	 */
	int deleteByOrderId(Integer orderId);
	
}
