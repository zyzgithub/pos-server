package com.wm.controller.open_api.tswj;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PortCall {
	public static final Logger logger = Logger.getLogger(PortCall.class);
	/**
	 * 支付成功回调i玩派
	 * @throws UnsupportedEncodingException
	 */
	public static void cpsPayCallback(TreeMap<String,Object> params) {
		try {
			String url = PortConfig.URL + "/order/cpsPayCallback";
			params.put("appKey", PortConfig.EXTERNAL_VALIDE_KEY);//加入签名key
			String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
			params.put("sign", sign.toUpperCase());//放入签名
			params.remove("appKey");//移除签名key
			System.out.println(JSON.toJSONString(params));
			HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cpsToPayCallback(String out_order_id, Double price, long payTime, String outTraceId) {
		// 支付成功
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		params.put("tno", out_order_id);// 第三方的订单编号
		params.put("price", price);// 订单金额
		long payDateTime = Long.valueOf(payTime + "000");
		params.put("payTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(payDateTime)));// 支付时间 yyyy-MM-dd HH:mm:ss
		params.put("payMethod", "ORDER_MZ");// 固定返回ORDER_MZ
		params.put("transId", outTraceId);// 微信支付流水号
		params.put("status", "success");// 固定返回success
	}
	
	/**
	 * 取消订单成功回调i玩派
	 * @throws UnsupportedEncodingException
	 */
	public static void cpsRefundCallback(TreeMap<String,Object> params){
		try {
			String url = PortConfig.URL + "/order/cpsRefundCallback";
			params.put("appKey", PortConfig.EXTERNAL_VALIDE_KEY);//加入签名key
			String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
			params.put("sign", sign.toUpperCase());//放入签名
			params.remove("appKey");//移除签名key
			HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void cpsRefundCallback(String payid, double price, String refundNo, Integer refundTimeSecond, String refundStatus){
		try {
			TreeMap<String,Object> params = new TreeMap<String,Object>();
			params.put("tno", payid);//i玩派的订单编号
			params.put("price", price);//订单金额
			params.put("status", "success");//固定返回success
			params.put("refundId", refundNo);//微信退款单号
			long refundTime = Long.valueOf(refundTimeSecond + "000");
			params.put("refundTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(refundTime)));//退款时间
			if(refundStatus.equals("Y")) params.put("errMsg", null);//退款失败原因 没有传入null
			else params.put("errMsg", "退款失败");
			cpsRefundCallback(params);
			logger.info(String.format("\n------【cpsRefundCallback】 params:%s", JSON.toJSONString(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 支付之前查询订单状态
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject detail(String orderid){
		try {
			String url = PortConfig.URL + "/order/detail";
			TreeMap<String,String> params = new TreeMap<String,String>();
			params.put("tno", orderid);//i玩派的订单编号
			params.put("appKey", PortConfig.EXTERNAL_VALIDE_KEY);//加入签名key
			String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
			params.put("sign", sign.toUpperCase());//放入签名
			params.remove("appKey");//移除签名key
			String ret = HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
			return JSON.parseObject(ret, JSONObject.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
