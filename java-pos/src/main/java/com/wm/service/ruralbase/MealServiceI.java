package com.wm.service.ruralbase;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface MealServiceI extends CommonService{

	/**
	 * 备餐列表
	 * @param merchantId
	 * @param type
	 * @param start
	 * @param num
	 * @param no
	 * @return
	 */
	public List<Map<String, Object>> getMealPreList(Integer merchantId,String type, Integer start, Integer num);
	
	/**
	 * 根据商家id获取菜单列表
	 * @param merchant_id 商家id
	 * @return
	 */
	public List<Map<String, Object>> getMealList(Integer merchantId,String type, Integer start, Integer num,int no);
	
	/**
	 * 排号置顶
	 * @param fullName
	 * @param merchantId
	 */
	public boolean setTop(String orderNum,int merchantId);

	
	/**
	 * 获得订单打印次数
	 * @param orderId
	 * @return
	 */
	public Long getPrintCount(Integer orderId);
	
	/**
	 * 排号电视
	 * @param merchantId
	 * @param id
	 * @param orderType
	 * @param start
	 * @param num
	 * @return
	 * 
	 * */ 
	public Map<String,Object> getMealOrderNum(int merchantId, Integer id,String orderType, Integer start, Integer num);
	
	
	/**
	 * 打印出餐日志
	 * @param orderEntity
	 */
	public void getPrintLog(OrderEntity orderEntity,AjaxJson j);
	
	/**
	 * 乡村鸡扫码
	 * @param userId
	 * @param merchantId
	 * @return
	 */
	public List<Map<String,Object>> qrCodeGetDineinOrder(Integer userId,Integer merchantId);
	
	/**
	 * 乡村鸡扫码更新状态
	 * @param id
	 */
	public void qrCodeUpdateDineinOrder(Integer id);
	
	/**
	 * PAD出餐旧
	 */
	public Integer updatePadList(Integer merchantId,Integer orderId,String type);
	
	/**
	 * PAD出餐
	 */
	public AjaxJson outMeal(Integer merchantId,Integer orderId,String type,String fullNum,String version)throws Exception;
	
	/**
	 * 修改失效状态
	 * @param orderId
	 * @param orderType
	 */
	public Integer updateDisplayStatus(int orderId,String orderType);
	
	/**
	 * 获得乡村基-状态
	 * @param merchantId
	 * @param orderId
	 * @param type
	 * @param fullNum
	 * @return
	 */
	public Map<String, Object> getMealStatus(Integer merchantId, Integer orderId, String type) ;
	
	/**
	 * 商家电视排号列表
	 * @param merchantId
	 * @param id
	 * @param orderType
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> TVOrderNumList(int merchantId, String orderType);
	
	/**
	 * 乡村基需求--堂食系统
	 * @param order
	 */
	public void rarulbase(OrderEntity order);
}
