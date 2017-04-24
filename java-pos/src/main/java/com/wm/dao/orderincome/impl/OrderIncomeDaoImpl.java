package com.wm.dao.orderincome.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.orderincome.OrderIncomeDao;
import com.wm.entity.orderincome.OrderIncomeEntity;

@Repository("orderIncomeDao")
public class OrderIncomeDaoImpl extends GenericBaseCommonDao<OrderIncomeEntity, Integer> implements OrderIncomeDao {

}
