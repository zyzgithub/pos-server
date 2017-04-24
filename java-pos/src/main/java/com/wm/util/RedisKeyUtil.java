package com.wm.util;

public class RedisKeyUtil {
	
	/**
	 * 获取所有保存快递员的key
	 * @return
	 */
	public static String allCourierIdKey(){
		return "courier:id:city_*"; 
	}
	
	/**
	 * 获取按城市保存快递ID的key
	 * @param cityCode 
	 * @return
	 */
	public static String courierIdKeyByCity(String cityCode){
		return "courier:id:city_" + cityCode;
	}
	
	/**
	 * 获取快递员保存位置的key
	 * @param courierId
	 * @return
	 */
	public static String courierPositionKey(Integer courierId){
		return "courier:" + "position:" + courierId;
	}
	
	public static String allOrdersKey(){
		return "courier:scamble:all_orders";
	}
	
	public static String queueCanScambleCouriersKey(Integer orderId){
		return "courier:scamble:queue_by_order:" + orderId;
	}
	
	public static String queueCanScambleOrdersKey(Integer courierId){
		return "courier:scamble:queue_by_courier:" + courierId;
	}
	
	public static String orderCourierKey(Integer orderId, Integer courierId){
		return "courier:scamble:order_courier:" + orderId + "_" + courierId;
	}
}
