package com.courier_mana.monitor.service;

import java.util.List;
import java.util.Map;

public interface OrderMonitorService {
	
	/**
	 * 获得未抢单的订单详情
	 * @return
	 */
	public List<Map<String,Object>> getNosendOrder(Integer page,Integer rows,List<Integer> merchantIds);
	
	/**
	 * 获得催单列表
	 * @param page
	 * @param rows
	 * @param 快递员所管辖范围的地址id集
	 * @return
	 */
	public List<Map<String,Object>> getUrgeOrder(Integer page,Integer rows,List<Integer> addressIds);
	
	/**
	 * 获得快递员所管辖下的所有地址id
	 * @param addressIds
	 * @return
	 */
	public List<Integer> getAddressId(Integer courierId);
	
	
	/**
	 * 获得还剩15中的订单列表
	 * @param courierId
	 * @return
	 */
	public List<Map<String,Object>> getCastFifteen(Integer courierId,Integer page,Integer rows);
	
	
	/**
	 * 获得还剩30分钟的订单列表
	 * @param courierId
	 * @return
	 */
	public List<Map<String,Object>> getCastThirdty(Integer courierId,Integer page,Integer rows);
	
	
	/**
	 * 判断订单是否是私厨订单，2为私厨；
	 * @param orderId
	 * @return
	 */
	public Map<String,Object> getPrivateOrder(Integer merchantId);
	
	/**
	 * 如果为众包订单则，获取商家详情
	 * @param merchantId
	 * @return
	 */
	public Map<String,Object> getMerchantDetail(Integer merchantId);
	
	
	/**
	 * 获取催单数量
	 * @param courierId
	 * @return
	 */
	public Map<String,Object> getUrgeCount(Integer courierId);
	
	/**
	 * 为未抢单订单指派快递员
	 * @param courierId
	 * @param orderId
	 */
	public Integer assigCourier(Integer courierId,Integer orderId);
	
	/**
	 * 通过快递员Id查询其所管辖下的所有商家
	 * @param courierId
	 * @return
	 */
	public List<Integer> getMerchantIds(Integer courierId);
	
	/**
	 * 通过快递员Id查询其所管辖下的所有商家  加强版
	 * @param courierId
	 * @return
	 */
	public List<Integer> getMerchantIdsTwo(Integer courierId);
	
	/**
	 * 通过快递员Id查询其所管辖下的所有合作商家
	 * @param courierId 用户ID
	 * @return
	 */
	public List<Integer> getPartnerMerchantIds(Integer courierId);
}
