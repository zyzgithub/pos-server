package com.team.wechat.util;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.DateUtils;

import com.wp.ConfigUtil;
import com.wp.PayCommonUtil;
import com.wxpay.util.SHA1Util;

public class JsapiTicket {
	
	private static final Logger LOGGER = Logger.getLogger(JsapiTicket.class);

	private String appId;
	private int timestamp;
	private String nonceStr;
	private String signature;
	private String url;
	private String apiTicket;

	private JsapiTicket() {
	}

	public static JsapiTicket getJsapiTicket(String apiTicket, String url) {
		JsapiTicket ticket = new JsapiTicket();
		ticket.setAppId(ConfigUtil.APPID_KFZ);
		ticket.setNonceStr(PayCommonUtil.CreateNoncestr());
		ticket.setTimestamp(DateUtils.getSeconds());
		ticket.setUrl(url);
		ticket.setApiTicket(apiTicket);
		
		//签名必须放在最后一步完成
		ticket.setSignature(ticket.createSignature());
		
		return ticket;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiTicket() {
		return apiTicket;
	}

	public void setApiTicket(String apiTicket) {
		this.apiTicket = apiTicket;
	}

	private String createSignature() {
		Map<String, String> map = new TreeMap<String, String>();
		map.put("noncestr", getNonceStr());
		map.put("jsapi_ticket", getApiTicket());
		map.put("timestamp", String.valueOf(getTimestamp()));
		map.put("url", getUrl());
		
		return createSignature(map);
	}
	
	private String createSignature(Map<String, String> map) {
		String str = "";
		Set<String> keys = map.keySet();
		for (String key : keys) {
			String value = map.get(key);
			str += (key + "=" + value + "&");
		}
		str = str.substring(0, str.lastIndexOf("&"));
		LOGGER.info("jsapi 待签名字符串----"+str);
		
		String signature = SHA1Util.Sha1(str);
		LOGGER.info("jsapi 签名----"+signature);
		return signature;
	}
}
