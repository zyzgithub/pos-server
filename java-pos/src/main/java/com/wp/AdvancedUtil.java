package com.wp;

import net.sf.json.JSONObject;

/**
 * 获取微信登录凭证工具类
 * @author lfq
 * @email  545987886@qq.com
 * @2015-5-8
 */
public class AdvancedUtil {
	
	/**
	 * 获取微信登录凭证
	 * @param appId			微信公众号id
	 * @param appSecret		微信公众好appSecret
	 * @param code			微信重定向后得到的微信code  (使用重定向到getWeiXinRedirectUrl(url))
	 * @return
	 */
	public static WeiXinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
		WeiXinOauth2Token wat = new WeiXinOauth2Token();
		
		String requestUrl = ConfigUtil.OAUTH2_URL.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
		JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(requestUrl, "GET", null));
		
		if (null != jsonObject) {
			wat = new WeiXinOauth2Token();
			if (jsonObject.containsKey("access_token")) {
				wat.setAccessToken(jsonObject.getString("access_token"));
			}
			if (jsonObject.containsKey("expires_in")) {
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
			}
			if (jsonObject.containsKey("refresh_token")) {
				wat.setRefeshToken(jsonObject.getString("refresh_token"));
			}
			if (jsonObject.containsKey("openid")) {
				wat.setOpenId(jsonObject.getString("openid"));
			}
			if (jsonObject.containsKey("unionid")) {
				wat.setUnionId(jsonObject.getString("unionid"));
			}
			if (jsonObject.containsKey("scope")) {
				wat.setScope(jsonObject.getString("scope"));
			}
		}
		return wat;
	}
	
	/**
	 * 封装成微信要跳转的url，跳转后的地址将可以获取得到微信返回的code(存放于request中)
	 * @author lfq
	 * @param redirectUrl 要重定向的完整地址，需要传参数时请拼接上参数
	 * @return
	 */
	public static String getWeiXinRedirectUrl(String redirectUrl){
		//微信官网文档使用：https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
		
		//这里使用：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=1#wechat_redirect
		
		return ConfigUtil.OAUTH2_CODE.replace("APPID",ConfigUtil.APPID).replace("REDIRECT_URI",CommonUtil.urlEncodeUTF8(redirectUrl));
	}
}
