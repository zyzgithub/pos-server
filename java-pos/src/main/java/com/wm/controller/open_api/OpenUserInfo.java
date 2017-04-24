package com.wm.controller.open_api;

import org.jeecgframework.core.common.model.common.UserInfo;

public class OpenUserInfo extends UserInfo {
	private String openid;
	
	public void setOpenid(Object openid) {
		this.openid = String.valueOf(openid);
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getOpenid() {
		return openid;
	}
}
