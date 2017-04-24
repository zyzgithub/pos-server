package com.wm.controller.open_api;

import java.util.Map;


/**
 * 第三方信息
 * @author folo
 *
 */
public class ThirdPart {
	
	/** 第三方id */
	public String app_id;
	
	/** 第三方key */
	public String app_key;
	
	//** 第三方名称 */
	public String app_name;
	
	/** 第三方页面调用地址 */
	public String page_url;
	
	/** 第三方接口调用地址 */
	public String interface_url;
	
	/** 第三方缩写 */
	public String sort_name;
	
	//** 第三方其他信息(暂时用来存放不同平台差异的信息) */
	public Map<String, Object> other;
	
	/** 订单支付成功消息模板 */
	public String pay_template_id;
	
	/** 订单退款成功消息模板 */
	public String refush_template_id;
	
	/** 跳转到第三方的活动首页面 */
	public String index_page;
	
	/** 第三方订单详细页面 */
	public String order_detail_page;
	
	/** 接口校验key */
	public String interface_key;
	
	/** 老版本的天生玩家在用 */
	@Deprecated
	public String token_key;
	
	public ThirdPlat thirdPlat;
	
}
