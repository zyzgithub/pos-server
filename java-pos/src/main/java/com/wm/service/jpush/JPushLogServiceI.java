package com.wm.service.jpush;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.jpush.JPushLogEntity;

public interface JPushLogServiceI extends CommonService{
	
	/**
	 * 根据日志id更新日志状态
	 * @param jpushlogId
	 * @return
	 */
	public void changeIsFeedBack(Integer jpushlogId, JPushLogEntity jPushLogEntity);

}
