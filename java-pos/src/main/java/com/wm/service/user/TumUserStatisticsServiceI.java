package com.wm.service.user;

import org.jeecgframework.core.common.service.CommonService;

public interface TumUserStatisticsServiceI extends CommonService {

	/**
	 * 更新用户消费统计表
	 * @param userId
	 * @param consumeMoney 单次消费金额
	 * @param rechargeMoney 单次充值金额
	 */
	public void updateStat(Integer userId, Integer consumeMoney, Integer rechargeMoney);

}
