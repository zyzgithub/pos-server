package com.wm.dao.order;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface CashSettlementDao extends IGenericBaseCommonDao{
	
	/**
	 * 找出某一段时间已经结算的订单 
	 * @param beginTime 格式: yyyy-MM-dd HH:mm:ss
	 * @param endTime 格式: yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	List<Integer> findSettledOrderIds(String beginTime, String endTime);
}
