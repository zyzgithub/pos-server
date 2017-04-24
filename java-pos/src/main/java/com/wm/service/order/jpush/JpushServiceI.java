package com.wm.service.order.jpush;

import java.util.Map;

public interface JpushServiceI {
	
	/**
	 * 极光推送新订单给快递员
	 * @param orderId 订单ID
	 * @param courierId 快递员ID
	 * @param canScramble 可抢订单数
	 */
	void pushNewOrderToCourier(Integer orderId, Integer courierId, Integer canScramble);
	
	/**
	 * 极光推送
	 * @param userId 推送对象
	 * @param pushMap 推送参数,title：标题；content：内容；voiceFile：声音文件
	 */
	public void push(Integer userId, Map<String, String> pushMap);
	
}
