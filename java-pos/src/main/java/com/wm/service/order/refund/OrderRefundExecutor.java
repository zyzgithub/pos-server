package com.wm.service.order.refund;

import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;

/**
 * 订单退款执行接口
 */
public interface OrderRefundExecutor {

	/**
	 * 执行订单退款
	 * @param orderRefundParam 退款参数
	 * @return 订单退款结果
	 */
	OrderRefundResult execute(DineInOrderRefundParam orderRefundParam);
	
}
