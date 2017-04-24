package com.wm.controller.open_api.iwash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wm.controller.open_api.ThirdPart;

public class IwashPortCall {
	public static final Logger logger = Logger.getLogger(IwashPortCall.class);
	
	public static void updatePayInfo(String custom_order_id, String pay_way, Double total_price, ThirdPart third){
		//同步到第三方
		try {
			Long timestamp = System.currentTimeMillis();
			Map<String, String> params = new HashMap<String, String>();
			params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp)));
			params.put("custom_order_id", custom_order_id);
			params.put("pay_way", "weichat");
			params.put("total_price", total_price + "");
			params.put("favourable", "0");
			params.put("payable_price", total_price + "");
			params.put("custom_order_status", "1");
			//参数签名
			params = MD5Util.getNewParams(params);
			//同步状态到第三方
			HttpUtils.postForm(third.interface_url + "/customOrder/updateOrderStatus", JSON.parseObject(JSON.toJSONString(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步状态到第三方
	 * @param third
	 * @param out_order_id 第三方订单id
	 * @param status 第三方状态 2快递员已抢单，3快递员收到用户的衣服，5快递员收到商户的衣服,9退款成功
	 * @param courier_id 快递员id
	 * @param update_time 抢单时间或者收衣时间，格式：2015-12-3 16:28:33
	 */
	public static void syncOrderStatus(ThirdPart third, String out_order_id, String status, String courier_id){
		//同步到第三方
		try {
			Long timestamp = System.currentTimeMillis();
			Map<String, String> params = new HashMap<String, String>();
			params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp)));
			params.put("custom_order_id", out_order_id);
			params.put("custom_order_status", status);
			params.put("courier_id", courier_id);
			params.put("update_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp)));
			
			//参数签名
			params = MD5Util.getNewParams(params);
			//同步状态到第三方
			HttpUtils.postForm(third.interface_url + "/customOrder/updateOrderStatus", JSON.parseObject(JSON.toJSONString(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
