package com.wm.service.dineorder;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface DineOrderServiceI extends CommonService{
	/**
	 * 创建堂食订单
	 * @param merchantId
	 * @param params
	 * @param timeRemark
	 * @param totalOrigin
	 * @return
	 */
	public int createDineOrder(int merchantId, String params, String timeRemark, double totalOrigin);
	
	/**
	 * 根据订单状态查询订单
	 * @param state
	 * @return
	 */
	public List<Map<String, Object>> getDineOrderByDineType(int dineType,int merchantId,int pageNo,int row);
	
	
}
