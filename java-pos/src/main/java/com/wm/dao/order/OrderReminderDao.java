package com.wm.dao.order;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.order.OrderReminderEntity;

/**
 *订单催单
 */
public interface OrderReminderDao extends IGenericBaseCommonDao{

	/**
	 * 查询催单列表(通过订单id)
	 * 通过时间反序
	 * @param orderId
	 * @param page , 第一条记录位置, 从0 开始
	 * @param rows ，每页记录数
	 * @return
	 */
	public List<OrderReminderEntity> queryListByOrderId(int orderId, int first, int rows);
	
	/**
	 * 
	 * 查询催单列表(通过订单id和分钟查询)
	 * 查询当前时间到指定分钟内的催单列表，如查询30分钟内的催单，或当天的催单，或一段时间内的催单
	 * @param orderId, 订单的ID, 如果订单id为空，则查所有订单多少分钟内的，如果分钟为空，则查默认30分钟内的所有催单列表
	 * @param minuteBefore, 如30分钟内的写30，24小时内的写24*60， 一段时间内的写（小时差）*60
	 * @param page , 页数，从1开始
	 * @param rows ，每页记录数
	 * @return 订单催单列表
	 */
	public List<Map<String, Object>> queryByOrderIdAndMinute(Integer orderId, Integer minuteBefore, int page, int rows);
	

	/**
	 * 查询催单列表(通过订单id和日期时间查询)
	 * @param orderId 订单ID，如果为空，则查所有订单的一段时间内的催单列表
	 * @param dtStart 日期时间开始，格式yyyy-MM-dd HH:mm:ss， 非空
	 * @param dtEnd 日期时间结束，格式yyyy-MM-dd HH:mm:ss， 非空
	 * @param page , 页数，从1开始
	 * @param rows ，每页记录数
	 * @return 订单催单列表
	 */
	public List<Map<String, Object>> queryByOrderIdAndDateTime(Integer orderId, String dtStart, String dtEnd, int page, int rows);
	
	
	/**
	 * 查询催单列表(通过用户id，用户类型，分钟查询)
	 * 查询当前时间到指定分钟内的催单列表，如查询30分钟内的催单，或当天的催单，或一段时间内的催单
	 * @param userId 用户id，如果是快递员则传快递员id，是商家则传商家id
	 * @param userType 用户类型，3为快递员，2为商家 
	 * @param minuteBefore, 如30分钟内的写30，24小时内的写24*60， 一段时间内的写（小时差）*60, 如果填空，则默认30分钟内的催单
	 * @param page , 页数，从1开始
	 * @param rows ，每页记录数
	 * @return
	 */
	public List<Map<String, Object>> queryByUserIdUserTypeAndMinute(Integer userId, Integer userType, Integer minuteBefore, int page, int rows);

	/**
	 * 查询催单列表(通过用户id，用户类型，日期时间段查询)
	 * 查询时间段内的催单列表
	 * @param userId 用户id，如果是快递员则传快递员id，是商家则传商家id
	 * @param userType 用户类型，3为快递员，2为商家 
	 * @param dtStart 日期时间开始，格式yyyy-MM-dd HH:mm:ss， 非空, 如2015-02-03 12:22:33
	 * @param dtEnd 日期时间结束，格式yyyy-MM-dd HH:mm:ss， 非空, 如2015-03-03 12:22:33
	 * @param page , 页数，从1开始
	 * @param rows ，每页记录数
	 * @return
	 */
	public List<Map<String, Object>> queryByUserIdUserTypeAndDateTime(Integer userId, Integer userType, String dtStart, String dtEnd, int page, int rows);
	
}
