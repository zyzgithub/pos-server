package com.wm.service.pay;

import java.util.Map;

import com.wm.service.impl.pay.BarcodePayResponse;


public interface BarcodePayServiceI {
	
	/**
	 * 对订单进行支付
	 * @param userId
	 * @param orderId
	 * @param otherParams
	 * @return
	 */
	BarcodePayResponse payOrder(Integer userId, Integer orderId, Map<String, String> otherParams);
	
	/**
	 * 
	 * @param authCode
	 * @return
	 */
	String getOpenId(String authCode);
	
	
	/**
	 * 订单退款
	 * @param orderId
	 * @return
	 */
	BarcodePayResponse refundOrder(Integer orderId);
}