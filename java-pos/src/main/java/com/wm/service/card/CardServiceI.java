package com.wm.service.card;

import org.jeecgframework.core.common.service.CommonService;

public interface CardServiceI extends CommonService{

	/**
	 * 注册送代金券
	 * @param userId
	 */
	public void registerCard(int userId);
}
