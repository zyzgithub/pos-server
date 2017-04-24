package com.wm.service.statistics;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface CourierStatisticsRealtimeServiceI extends CommonService {
	/**
	 * 新建或更新快递员的评价，如果是更新则会累加总次数，总评分
	 * 
	 * @param courierId
	 *            商家id
	 * @param commentScore
	 *            商家评分
	 */
	public void createOrUpdateCourierComment(Integer courierId, Integer commentScore);
	
	/**
	 * 订单完成时，实时统计快递员订单和提成
	 * @param order
	 */
	public void statisticsRealtime(OrderEntity order);

}
