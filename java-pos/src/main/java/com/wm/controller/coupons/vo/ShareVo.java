package com.wm.controller.coupons.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.base.config.EnvConfig;
import com.wp.PayCommonUtil;

public class ShareVo {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");

	private String title = EnvConfig.base.WELCOME_TITLE;
	private String desc = EnvConfig.base.WELCOME_TITLE;
	private String serial = PayCommonUtil.CreateNoncestr(6) + sdf.format(new Date());
	private String link = EnvConfig.base.DOMAIN + "/coupons/receive.do?serial="+serial;
	private String imgUrl = "http://oss.0085.com/user/20151010.png";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSerial() {
		return serial;
	}
	
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
