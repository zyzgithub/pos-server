package com.wm.dao.dineorder;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

/**
 * 0085_dine_order表Dao层接口
 */
public interface DineOrderDao extends IGenericBaseCommonDao {

	int deleteByOrderId(Integer orderId);
	
}
