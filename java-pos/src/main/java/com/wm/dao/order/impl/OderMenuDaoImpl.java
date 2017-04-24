package com.wm.dao.order.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.order.OrderMenuDao;
import com.wm.entity.order.OrdermenuEntity;

@Repository("oderMenuDao")
public class OderMenuDaoImpl extends GenericBaseCommonDao<OrdermenuEntity, Integer> implements OrderMenuDao{

	@Override
	public boolean createDefaultOrderMenu(Integer orderId) {
		String o_msql = "insert into order_menu (MENU_ID, ORDER_ID, PRICE, QUANTITY, TOTAL_PRICE) values(?, ?, ?, ?, ?)";
        int rows = executeSql(o_msql, 0, orderId, 0, 0, 0.0);
		return rows == 1;
	}

}
