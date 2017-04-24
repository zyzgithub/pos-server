package com.wm.dao.flow.impl;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.flow.FlowDao;
import com.wm.entity.flow.FlowEntity;

@Repository("flowDao")
public class FlowDaoImpl extends GenericBaseCommonDao<FlowEntity, Integer> implements FlowDao {

	@Override
	public int add(Integer userId, Integer detailId, String detail, BigDecimal money, String action, String type,
			BigDecimal preMoney, BigDecimal postMoney) {
		String sql = " insert into flow (user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
		return executeSql(sql, userId, detailId, detail, money, action, type, preMoney, postMoney);
	}

}
