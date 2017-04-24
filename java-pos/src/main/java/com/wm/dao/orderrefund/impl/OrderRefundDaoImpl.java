package com.wm.dao.orderrefund.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.orderrefund.OrderRefundDao;
import com.wm.entity.orderrefund.OrderRefundEntity;

@Repository("orderRefundDao")
public class OrderRefundDaoImpl extends GenericBaseCommonDao<OrderRefundEntity, Integer> implements OrderRefundDao {

}
