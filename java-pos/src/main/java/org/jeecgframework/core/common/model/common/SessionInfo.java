package org.jeecgframework.core.common.model.common;

import java.io.Serializable;

import jeecg.system.pojo.base.TSUser;


@SuppressWarnings("serial")
public class SessionInfo implements Serializable {

	private TSUser user;
	public TSUser getUser() {
		return user;
	}

	public void setUser(TSUser user) {
		this.user = user;
	}

}
