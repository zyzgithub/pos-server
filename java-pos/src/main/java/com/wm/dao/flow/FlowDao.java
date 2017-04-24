package com.wm.dao.flow;

import java.math.BigDecimal;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface FlowDao extends IGenericBaseCommonDao {

	int add(Integer userId, Integer detailId, String detail, BigDecimal money, String action, String type, BigDecimal preMoney, BigDecimal postMoney);
	
}
