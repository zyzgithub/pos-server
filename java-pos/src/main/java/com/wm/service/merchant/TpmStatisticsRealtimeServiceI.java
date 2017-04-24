package com.wm.service.merchant;

import org.jeecgframework.core.common.service.CommonService;

/**
 * 商家统计表
 * @author lzh
 *
 */
public interface TpmStatisticsRealtimeServiceI extends CommonService{
	
	/**
	 * 更新商家订单统计
	 * @param merchantId
	 * @param consumeMoney
	 * @param orderTime 订单配送时长，堂食订单默认为20分钟
	 */
	public void updateStat(Integer merchantId, Integer consumeMoney, Integer orderTime);
	
}
