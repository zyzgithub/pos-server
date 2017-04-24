package com.wm.dto.order;

/**
 * 威富通退款请求格式
 * 
 * @author 王喜文
 * @since 2016-3-15
 */
public class WftRefundRequest {
	
	/**
	 * 回退渠道, 原路返回
	 */
	public static final String REFUND_CHANNEL_ORIGINAL = "ORIGINAL";
	
	/**
	 * 回退渠道, 余额
	 */
	public static final String REFUND_CHANNEL_BALANCE = "BALANCE";

	/**
	 * 退款单号
	 */
	private String refundNo;

	/**
	 * 商户订单号
	 */
	private String tradeNo;

	/**
	 * 回退渠道
	 */
	private String refundChannel;

	/**
	 * 订单总金额(单位分)
	 */
	private String totalFee;

	/**
	 * 退款总金额(单位分)
	 */
	private String refundFee;

	/**
	 * 第三方订单流水号
	 */
	private String transactionId;

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
