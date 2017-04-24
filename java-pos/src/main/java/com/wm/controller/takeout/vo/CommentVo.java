package com.wm.controller.takeout.vo;

import org.apache.commons.lang3.StringUtils;

public class CommentVo {
	private Integer orderId;
	private String content;
	private int score;
	private String time;
	private String userName;
	private String headIcon;
	private String mobile;
	

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserName() {
		if(StringUtils.isEmpty(userName)) {
			return "匿名";
		} else {
			return userName.replaceFirst(""+userName.charAt(0), "*");
		}
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
