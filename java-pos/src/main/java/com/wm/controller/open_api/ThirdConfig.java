package com.wm.controller.open_api;

/**
 * 接口配置管理
 * @author folo
 *
 */
public class ThirdConfig {
	/** 是否开启调试 true=开启,false=关闭 */
	public static final boolean IS_DEBUG = true;
	
	@SuppressWarnings("deprecation")
	public static ThirdPart getThird(ThirdPlat thirdPlat, boolean third_debug_enable){
		if(null == thirdPlat) return null;
		ThirdPart third = new ThirdPart();
		
		third.pay_template_id = "6SSt8QO6oRLGy1xLPig8yploM-SvNuOnDNT8tKnPIAg";
		third.refush_template_id = "VwVWo-Vo6JK6WjKkvKm3QSBbA-Up3qDCN2Hz7mgKeRM";
		if(third_debug_enable){
			third.pay_template_id = "2gLQqnrqS2gQZU9Ua9TeXHjeN8Pz_Btzd72skQEPVto";
			third.refush_template_id = "FxYSSbrGsg3OSunbe03WHz7VODDXEIOjMsf_vLV31o4";
		}
		
		switch (thirdPlat) {
		case tswj:
			third.thirdPlat = ThirdPlat.tswj;
			third.app_id = "yihaowaimai";
			third.app_key = "57ne0zb3-pz6z-q9fc-qvzk-4podx7ag3av5";
			third.app_name = "i玩派";
			third.page_url = "http://m.iamgenius.com.cn";
			third.interface_url = "";
			third.sort_name = ThirdPlat.tswj.toString();
			third.token_key = "bcofs3zo-kwvj-ozcd-6xq0-ue2b8814oxl3";
			
			third.interface_key = "d601e083-f8e6-45d9-88d9-271c189e80aa";
			third.index_page = "/atakeaway/yiActivityList";
			third.order_detail_page = "/atakeaway/yiOrderDetail";
			
			//debug config
			if(third_debug_enable){
				third.page_url = "http://dev.test.iamgenius.com.cn";
			}
			return third;
		case iwash:
			third.thirdPlat = ThirdPlat.iwash;
			third.app_id = "10800101";
			third.app_key = "e5e4d889-6418-49c3-851d-5949e6c069a5";
			third.app_name = "我要洗衣";
			third.page_url = "http://wx.iwantwash.com";
			third.interface_url = "http://interface.iwantwash.com:8080/openapi";
			third.index_page = "/takeout/index.do";
			third.sort_name = ThirdPlat.iwash.toString();
			if(third_debug_enable){
				third.interface_url = "http://pic.iwantwash.com:8080/openapi";
			}
			return third;
		default:
			return null;
		}
	}
	
	/**
	 * 获取token加密key
	 * @param third
	 * @return
	 */
	public static String getTokenKey(ThirdPart third){
		ThirdPlat thirdPlat = third.thirdPlat;
		if(null == thirdPlat) return null;
		switch (thirdPlat) {
		case tswj:
			return third.token_key;
		case iwash:
			return third.app_key;
		default: return null;
		}
	}
}
