package com.wm.service.systemconfig;

import org.jeecgframework.core.common.service.CommonService;

public interface SystemconfigServiceI extends CommonService{

	/**
	 * 根据Code查找对应的Value
	 * @param code
	 * @return
	 */
	public String getValByCode(String code);
}
