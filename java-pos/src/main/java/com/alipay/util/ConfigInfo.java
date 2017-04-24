package com.alipay.util;

import org.jeecgframework.core.util.PropertiesUtil;

public class ConfigInfo {

	public static String getConfigInfo(String key) {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return p.readProperty(key);
	}
}
