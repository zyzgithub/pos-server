package com.wm.service.statistics;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface MerchatStatisticsRealtimeServiceI extends CommonService{
	/**
	 * 新建或更新商家的评价.，如果是更新则会累加总次数，总评分
	 * @param merchantId 商家id
	 * @param commentScore 商家总评分
	 */
	public void createOrUpdateMerchantComment(Integer merchantId, Integer commentScore );
	
	/**
	 * 订单完成时，实时统计商家订单和送餐数
	 * @param order
	 */
	public void statisticsRealtime(OrderEntity order);
	
}
