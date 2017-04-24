package com.wm.dao.order.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.order.PushedOrderDao;
import com.wm.entity.order.PushedOrderEntity;

@Repository("pushedOrderDao")
public class PushedOrderDaoImpl extends GenericBaseCommonDao<PushedOrderEntity, Integer> implements PushedOrderDao {

	@Override
	public int deleteByOrderId(Integer orderId) {
		String sql = "DELETE FROM 0085_pushed_order WHERE order_id=?";
		return executeSql(sql, orderId);
	}


}
