package com.wm.controller.coupons.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class ReceiverVo {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");

	private String mobile;
	private String header;
	private int money;
	private Date time;

	public String getMobile() {
		char[] c = mobile.toCharArray();
		String t = "";
		for (int i = 0; i < c.length; i++) {
			if(i>=3 && i <= 6)
				t += "*";
			else
				t += c[i];
		}
		return t;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeader() {
		return StringUtils.defaultString(header);
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}

	public String getTime() {
		return sdf.format(time);
	}

	public Date getTimeDate() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}

}
