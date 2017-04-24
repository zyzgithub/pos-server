package org.jeecgframework.core.common.model.common;

public class UserInfo {

	private Integer userId;
	private boolean redirectUrl;
	private boolean newUser;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public boolean isRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(boolean redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

}
