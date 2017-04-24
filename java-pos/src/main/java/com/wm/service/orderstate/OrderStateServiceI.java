package com.wm.service.orderstate;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;

public interface OrderStateServiceI extends CommonService{
	/**
	 * 生成订单状态记录
	 * @param orderId
	 */
	public void createOrderState(int orderId);
	/**
	 * 第三方支付订单状态
	 * @param createTime
	 * @param wUser
	 * @param orderId
	 */
	public void createThirdOrderState(int orderId);
	/**
	 * 支付订单状态记录
	 * @param orderId
	 */
	public void payOrderState(int orderId);
	
	public void payOrderState(OrderEntity order);
	/**
	 * 商家接受订单状态记录
	 * @param orderId
	 */
	public void accessOrderState(int orderId);
	/**
	 * 配送员开始配送订单状态记录
	 * @param orderId
	 */
	public void deliveryOrderState(int orderId);
	/**
	 * 完成订单订单状态
	 * @param orderId
	 */
	public void doneOrderState(int orderId);
	/**
	 * 评价订单记录
	 * @param orderId
	 */
	public void evaluateOrderState(int orderId);
	/**
	 * 提交退款申请订单状态记录
	 * @param orderId
	 */
	public void askedRefundOrderState(int orderId);
	/**
	 * 成功退款订单状态记录
	 * @param orderId
	 */
	public void refundSuccessOrderState(int orderId);
	/**
	 * 不接受退款订单状态记录
	 * @param orderId
	 */
	public void refundFailedOrderState(int orderId);
	/**
	 * 商家不接受订单状态记录
	 * @param orderId
	 */
	public void noAcceptOrderState(int orderId);
	/**
	 * 配送完成订单状态记录
	 * @param orderId
	 */
	public void deliveryDoneOrderState(int orderId);
	/**
	 * 商家创建电话订单状态记录
	 * @param orderId
	 */
	public void createPhoneOrderState(int orderId);
	/**
	 * 快递员待付款订单状态记录
	 * @param orderId
	 */
	public void deliveryPayOrderState(int orderId);
	
	public void deliveryPayOrderState(String courier, String user, int orderId, double money);
	/**
	 * 厨房制作完成订单状态记录
	 * @param orderId
	 */
	public void cookDoneOrderState(int orderId);
	/**
	 * 系统自动完成已支付未完成订单状态记录
	 * @param orderId
	 */
	public void autoCompleteOrderState(int orderId);
	/**
	 * 商家修改未支付订单为电话订单记录
	 * @param orderId
	 */
	public void merchantUpdateOrder(int orderId);
	
	/**
	 * 接单中状态，商家选择是否接单
	 * @param orderId
	 */
	public void accessOrderStateChoice(int orderId);
	
	/**
	 * 同意退款申请订单状态记录
	 * @param orderId
	 */
	public void acceptRefundOrderState(int orderId);
	
	/**
	 * 快递员已抢单状态
	 * @param courierId
	 * @param orderId
	 */
	public void scrambleOrder(Integer courierId, Integer orderId);
	
}
