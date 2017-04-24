package com.wm.dao.orderstate.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.orderstate.OrderStateDao;
import com.wm.entity.orderstate.OrderStateEntity;

@Repository("orderStateDao")
public class OrderStateDaoImpl extends GenericBaseCommonDao<OrderStateEntity, Integer> implements OrderStateDao {

}
