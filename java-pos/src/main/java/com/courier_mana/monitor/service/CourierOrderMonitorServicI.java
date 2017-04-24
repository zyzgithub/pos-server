package com.courier_mana.monitor.service;

import java.util.List;
import java.util.Map;

import com.wm.entity.order.OrderEntity;


/**
 * 
 * 快递员订单服务接口
 *
 */
public interface CourierOrderMonitorServicI {
	/**
	 * 根据状态和用户ID获取今日指定状态的所有订单数（管理员）
	 * @param courierId 用户ID
	 * @param state 订单状态：unpay未支付，pay支付成功，accept制作中，done待评价，confirm 已完成，refund 退款 delivery 配送中，delivery_done配送完成
	 * @return
	 */
	public Long getCourierOrdersCount(List<Integer> courierIds,String[] states);
	/**
	 * 获取指定用户下今日所有的催单订单数(管理员)
	 * @param courierId 用户ID
	 * @return
	 */
	public Long getCourierOrdersReminderCount(Integer courierId);
	/**
	 * 获取指定用户下今日所有的超时订单数(管理员)
	 * @param courierId 用户ID
	 * @return
	 */
	public Long getCourierOrdersOutTimeCount(List<Integer> courierIds);
	
	/**
	 * 获取指定用户下订单统计(管理员)
	 * @param courierId
	 * @return
	 */
	public Map<String, Object> getCourierOrdersCounts(Integer courierId);
	
	/**
	 * 获取指定快递员身上的订单数(单个快递员)
	 * @param courierId
	 * @return
	 */
	public Long getCourierOrdersById(Integer courierId,String[] states);
	/**
	 * 获取指定单个快递员身上订单统计(单个快递员)
	 * @param courierId
	 * @return
	 */
	public Map<String, Object> getCourierOrdersCountById(Integer courierId);
	
	/**
	 * 根据订单编号查询订单明细
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getOrderDetailByOrderId(Integer orderId);
	
	/**
	 * 根据管理员id查询管理员下所有的快递员位置信息和订单信息
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCouriersAndOrdersCount(Integer courierId);
	/**
	 * 根据片区id和管理员id查询管理员下所有的快递员位置信息和订单信息
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCouriersAndOrdersCountById(Integer courierId,Integer orgId);
	/**
	 * 根据关键词查询订单
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getOrdersByKeywords(Integer page, Integer rows,Integer courierId,String keyword);
	/**
	 * 根据机构查询订单
	 * @param courierId
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> getOrdersByOrgId(Integer page, Integer rows,Integer courierId,Integer orgId);
	/**
	 * 根据订单查询该网点下所有的快递员
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> getCouriersByOrderId(Integer orderId);
	/**
	 * 派单
	 * @param courierId
	 * @param orderId
	 * @return
	 */
	public Integer updateOrderCourierById(Integer courierId,Integer orderId);

}
