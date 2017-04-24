package com.wm.dto.order;

/**
 * 订单退款结果
 */
public class OrderRefundResult {

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 生成的退单序列号
	 */
	private String outRefundNo;

	public static OrderRefundResult successResult(String outRefundNo) {
		return new OrderRefundResult(true, outRefundNo);
	}

	public static OrderRefundResult failResult(String outRefundNo) {
		return new OrderRefundResult(false, outRefundNo);
	}

	public OrderRefundResult() {

	}

	public OrderRefundResult(boolean success, String outRefundNo) {
		this.success = success;
		this.outRefundNo = outRefundNo;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	@Override
	public String toString() {
		return "OrderRefundResult{" +
				"success=" + success +
				", outRefundNo='" + outRefundNo + '\'' +
				'}';
	}
}
