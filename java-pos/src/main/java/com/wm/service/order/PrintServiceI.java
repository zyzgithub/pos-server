package com.wm.service.order;

import org.jeecgframework.core.common.service.CommonService;

import com.alibaba.fastjson.JSONArray;
import com.wm.entity.dineorder.DineOrderEntity;
import com.wm.entity.order.OrderEntity;

public interface PrintServiceI extends CommonService {

	/**
	 * 打印订单
	 * @param order
	 * @param b
	 * @return
	 */
	public void print(OrderEntity order, boolean b);
	
	/**
	 * 远程下单打印--用于超市小票打印
	 * @param orderId
	 * @param b
	 * @return
	 */
	public boolean orderPrint(Integer orderId, boolean b);


	/**
	 * 堂食订单打印
	 * @param dineOrder
	 * @return
	 */
	public boolean printDineOrder(DineOrderEntity dineOrder);
	
	/**
	 * 打印小票 -- 乡村基出餐PAD
	 * @param orderId
	 * @return
	 */
	public boolean mealPrint(Integer orderId);
	
	/**
	 * 门店订单打印
	 * @param order
	 * @param printType 打印类型(加菜前，加菜后)
	 * @param extendParams 加菜的内容
	 * @return
	 */
	public boolean dineOrderPrint(OrderEntity order,String printType,JSONArray extendParams);
}
