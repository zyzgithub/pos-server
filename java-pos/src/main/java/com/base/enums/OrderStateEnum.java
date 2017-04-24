package com.base.enums;

/**
 * 订单状态：unpay未支付，pay支付成功，accept制作中，done待评价，confirm 已完成，refund 退款 delivery
 * 配送中，delivery_done配送完成
 *
 */
public enum OrderStateEnum {

	PAY("pay", "支付成功"), UNPAY("unpay", "未支付"), ACCEPT("accept", "制作中"), DONE(
			"done", "待评价"), CONFIRM("confirm", "已完成"), REFUND("refund", "退款"), DELIVERY(
			"delivery", "配送中"), DELIVERY_DONE("delivery_done", "配送完成"), CANCEL("cancel", "已取消"),
	/**
	 * 自定义-待收货状态（包括：delivery, accept, pay三种状态）
	 */
	CUSTOM_RECEIVING("custom_receiving", "待收货");

	private OrderStateEnum(String orderStateEn, String orderStateCn) {
		this.orderStateEn = orderStateEn;
		this.orderStateCn = orderStateCn;
	}

	private String orderStateEn;
	private String orderStateCn;

	public String getOrderStateEn() {
		return orderStateEn;
	}

	public String getOrderStateCn() {
		return orderStateCn;
	}

	/**
	 * 根据英文名找中文名，不分大小写
	 */
	public static String getCnNameByEnName(String enName) {
		for (OrderStateEnum o : OrderStateEnum.values()) {
			if (o.getOrderStateEn().equalsIgnoreCase(enName)) {
				return o.getOrderStateCn();
			}
		}
		return "";
	}

}
