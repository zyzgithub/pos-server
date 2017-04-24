package com.wm.dto.order;

import com.wm.entity.order.OrderEntity;

/**
 * 堂食退单请求参数
 */
public class DineInOrderRefundParam {

	/**
	 * 订单记录
	 */
	private OrderEntity order;

	/**
	 * 退款金额（元）
	 */
	private String refundFee;

	public DineInOrderRefundParam(OrderEntity order, String refundFee) {
		if (order == null || refundFee == null) {
			throw new IllegalArgumentException("empty argument.");
		}
		this.order = order;
		this.refundFee = refundFee;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

}
