package com.wm.dao.dineorder.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.dineorder.DineOrderDao;
import com.wm.entity.dineorder.DineOrderEntity;

/**
 * 0085_dine_order表Dao层实现
 */
@Repository("dineOrderDao")
public class DineOrderDaoImpl extends GenericBaseCommonDao<DineOrderEntity, Integer> implements DineOrderDao {

	@Override
	public int deleteByOrderId(Integer orderId) {
		String sql = "DELETE FROM 0085_dinein_order WHERE order_id=?";
		return executeSql(sql, orderId);
	}

}
