package com.wm.dao.order;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.controller.takeout.vo.OrderMenuVo;
import com.wm.controller.takeout.vo.OrderVo;

public interface OrderDao extends IGenericBaseCommonDao{

	OrderVo queryById(Integer orderId, Integer userId);

	List<OrderMenuVo> queryOrderMenusById(Integer orderId);

	AddressDetailVo queryLastedUserInfo(Integer userId, Integer saleType);
	
	/***
	 * 创建供应链订单
	 * @param cityId 城市Id
	 * @param merchantId 商家ID
	 * @param origin 订单金额
	 */
	Integer createSupplyChainOrder(Integer cityId, Integer merchantId, 
			Double origin);
	
	/**
	 * 设置订单的payId、orderNum
	 * @param payId
	 * @param orderNum
	 * @return
	 */
	boolean setPayIdAndOrderNum(Integer orderId, String payId, String orderNum);
	
	/**
	 * 更新订单的状态
	 * @param orderId
	 * @param state
	 */
	void setOrderDeliveryState(Integer orderId);
	
	void setOrderConfirmState(Integer orderId);
	
	
	/**
	 * 根据订单Id获取对应的商品列表
	 * @param orderId
	 * @return
	 */
	List<Map<String, Object>> getMenus(Integer orderId);
	
	
	/**
	 * 获取715超市商家的所有未经过POS机的订单
	 * @param merchantId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String, Object>> getSupermarketNoPosPayedOrders(Integer merchantId, String beginTime, String endTime);

	/**
	 * 获取715超市商家某一个收银员的在POS机创建的并且支付了的订单
	 * @param cashierId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String, Object>> getSupermarketPosPayedOrders(Integer cashierId, String beginTime, String endTime);

	/**
	 * 获取收银员上班期间的退款订单
	 * @param cashierId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	Map<String, Object> getSupermarketRefundOrder(Integer cashierId, String beginTime, String endTime);
	
	/**
	 * 获取收银员Pos上无码收银订单
	 * @param cashierId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	Map<String, Object> getSupermarketNoCodeOrder(Integer cashierId, String beginTime, String endTime);
	
	/**
	 * 统计收银员整单折扣、免单、抹零订单
	 * @param merchantId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String, Object>> getBasicPosOrderModifyOrder(Integer merchantId, String beginTime, String endTime);
}
