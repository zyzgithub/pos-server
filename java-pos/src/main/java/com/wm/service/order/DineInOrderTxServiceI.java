package com.wm.service.order;

import org.jeecgframework.core.common.model.json.MsgResp;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.order.OrderEntity;
import com.wm.service.impl.flow.NotEnoughBalanceException;
import com.wm.service.impl.order.OrderRefundFailException;

/**
 * 堂食订单服务接口（事务处理部分）
 */
public interface DineInOrderTxServiceI extends CommonService {

	/**
	 * 堂食订单退单
	 * @param orderId 订单ID
	 * @param opUserId 操作人ID
	 * @return 退单结果
	 * @throws NotEnoughBalanceException 商家余额不足
	 * @throws OrderRefundFailException 用户退款失败
	 */
	MsgResp chargeback(Integer orderId, Integer opUserId) throws NotEnoughBalanceException, OrderRefundFailException;

	/**
	 * 堂食订单预检查
	 * @param order 订单对象
	 * @return 检查结果
	 */
	MsgResp chargebackPrecheck(OrderEntity order);
	
	/**
	 * 堂食订单预检查
	 * @param orderId 订单对象
	 * @return 检查结果
	 */
	MsgResp chargebackPrecheck(Integer orderId);

}
