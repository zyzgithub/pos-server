package com.wm.service.version;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.version.VersionEntity;

public interface VersionServiceI extends CommonService{

	/**
	 * 获取客户端最新版本
	 * @param type 客户端类型
	 * @return
	 */
	VersionEntity getLastVersion(String type);

}
