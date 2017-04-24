package com.wm.controller.open_api.tswj;

/**
 * 接口配置管理
 * @author folo
 *
 */
public class PortConfig {
	/** 第三方调用本系统接口校验key */
	public static final String SYSTEM_VALIDE_KEY = "57ne0zb3-pz6z-q9fc-qvzk-4podx7ag3av5";
	/** 调用第三方接口校验key */
	public static final String EXTERNAL_VALIDE_KEY = "d601e083-f8e6-45d9-88d9-271c189e80aa";
	/** token加密key */
	public static final String TOKEN_KEY = "bcofs3zo-kwvj-ozcd-6xq0-ue2b8814oxl3";
	/** 第三方appid */
	public static final String TSWJ_APPID = "yihaowaimai";//此处使用的是i玩派自己需求的key
	/** 第三方的接口地址 */
	public static final String URL = "http://third.iamgenius.com.cn";
	/** 第三方页面页面url */
	public static String PAGE_URL = "http://m.iamgenius.com.cn";
	
	/** i玩派活动列表url */
	public static String TSWJ_ACTIVITY_URL = PAGE_URL + "/atakeaway/yiActivityList";
	/** i玩派订单列表url */
	public static String TSWJ_ORDER_LIST_URL = PAGE_URL + "/atakeaway/yiOrderList";
	/** i玩派订单详细url */
	public static String TSWJ_ORDER_DETAIL_URL = PAGE_URL + "/atakeaway/yiOrderDetail";
	
	/** 订单支付成功消息模板 */
	public static String CREATE_ORDER_TEMPLATE = "6SSt8QO6oRLGy1xLPig8yploM-SvNuOnDNT8tKnPIAg";
	/** 订单退款成功消息模板 */
	public static String CANCEL_ORDER_TEMPLATE = "VwVWo-Vo6JK6WjKkvKm3QSBbA-Up3qDCN2Hz7mgKeRM";
	
	/** 是否开启调试 true=开启,false=关闭 */
	public static final boolean IS_DEBUG = true;
	
	static{
		if(IS_DEBUG){//调试配置
			CREATE_ORDER_TEMPLATE = "2gLQqnrqS2gQZU9Ua9TeXHjeN8Pz_Btzd72skQEPVto";
			CANCEL_ORDER_TEMPLATE = "FxYSSbrGsg3OSunbe03WHz7VODDXEIOjMsf_vLV31o4";
			
			PAGE_URL = "http://dev.test.iamgenius.com.cn";
			TSWJ_ACTIVITY_URL = PAGE_URL + "/atakeaway/yiActivityList";
			TSWJ_ORDER_LIST_URL = PAGE_URL + "/atakeaway/yiOrderList";
			TSWJ_ORDER_DETAIL_URL = PAGE_URL + "/atakeaway/yiOrderDetail";
		}
	}
}
