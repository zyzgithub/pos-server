package com.wp;

import net.sf.json.JSONObject;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.config.EnvConfig;

public class AccessTokenContext {
	
	private static final String access_token_key = "wx_access_token";
	private static final String jsapi_ticket_key = "wx_jsapi_ticket";
	private static MemcachedClient client;
	
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenContext.class);
	
	private AccessTokenContext() {}
	
	static {
		try {
			client  = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT));
		} catch (Exception e) {
			logger.error("初始化Memcached失败：" + EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT, e);
		}
	}
	
	public static String setAccessToken() {
		String accessToken = "";
		
		String requestUrl = ConfigUtil.ACCESS_TOKEN_URL.replace("APPID", ConfigUtil.APPID).replace("APPSECRET", ConfigUtil.APP_SECRECT);
		JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(requestUrl, "GET", null));
		logger.info("通过微信接口获取AccessToken：" + jsonObject + ", appid:" + ConfigUtil.APPID);
		if (null != jsonObject) {
			accessToken = jsonObject.getString("access_token");
			while(true) {
				CASValue<Object> cas = client.gets(access_token_key);
				if(null != cas){
					CASResponse response = client.cas(access_token_key, cas.getCas(), accessToken);
					if(CASResponse.OK.name().equals(response.name())) {
						logger.info("================reset AccessToken：" + cas.getCas() + " 为 " + accessToken);
						break;
					}
				} else {
					client.set(access_token_key, 0, accessToken);
					logger.info("================首次初始化access_token_key为 " + accessToken);
					break;
				}
			}
		}
		return accessToken;
	}
	
	public static String getAccessToken(){
		Object obj = client.get(access_token_key);
		logger.info("getAccessToken " + obj + " is not null ? : " + (null != obj));
		if(null != obj){
			return obj.toString();
		} else {
			return setAccessToken();
		}
	}
	
	public static String setJsapiTicket() {
		String ticket = "";
		String accessToken = getAccessToken();
		logger.info("setJsapiTicket accessToken: {}", accessToken);
		//jsapi_ticket
		String jsapiTicketUrl = ConfigUtil.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("jsapiTicketUrl: {}", jsapiTicketUrl);
		String ticktUrl = CommonUtil.httpsRequest(jsapiTicketUrl, "GET", null);
		logger.info("setJsapiTicket ticktUrl: {}", ticktUrl);
		JSONObject jsapiTicket = JSONObject.fromObject(ticktUrl);
		logger.info("setJsapiTicket jsapiTicket: {}", jsapiTicket);
		if(null != jsapiTicket) {
			ticket = jsapiTicket.getString("ticket");
			client.set(jsapi_ticket_key, 0, ticket);
		}
			
		return ticket;
	}
	
	public static String getJsapiTicket() {
		Object obj = client.get(jsapi_ticket_key);
		if(null != obj)
			return obj.toString();
		else
			return setJsapiTicket();
	}
}