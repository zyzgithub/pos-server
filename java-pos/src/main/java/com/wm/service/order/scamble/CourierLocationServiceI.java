package com.wm.service.order.scamble;


/**
 * 快递员地理位置服务接口
 * @author wjw
 *
 */
public interface CourierLocationServiceI {
	
	/**
	 * ZSet的key格式：courier:id:city_城市编码
	 * ZSet（member：快递员ID, score: 快递员上传位置的时间*-1:此处次乘-1,可以保证最近上传位置的快递员ID保存ZSet在最前面）
	 * 从保存快递员ID的ZSet中获取索引，使得索引以前的成员的score（上传时间）比临界时间点criticalTime要晚
	 * 索引以后的成员的score（上传时间）比临界时间点criticalTime要早
	 * @param cityCode 城市编码
	 * @param criticalTime 临界时间点
	 * @return
	 */
	public long getZSetIndexByCriticalTime(String cityCode, double criticalTime);
	
	/**
	 * 
	 * @param key格式：courier:id:city_城市编码
	 * @param criticalTime 临界时间点
	 * @return
	 */
	public long getZSetIndexByCriticalTimeIntertal(String key, double criticalTime);
}
