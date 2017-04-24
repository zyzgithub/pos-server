package com.alipay.refund;

import java.math.BigDecimal;

/**
 * 即时到账批量退款-请求退款的参数
 */
public class FastpayRefundParam {

	/**
	 * 原付款支付宝交易号
	 */
	private String tradeNo;

	/**
	 * 退款总金额。警告！退款总金额尽量与同一交易的付款总金额相同，虽然支持同一笔交易分多次退款，但如果分多次退款，则支付请求的回调接口和不是退款请求的回调接口都会被通知。
	 * 如果全额退款，则只有退款请求的回调接口会被通知。建议尽可能简化模型，进行全额退款
	 */
	private BigDecimal totalRefund;

	/**
	 * 退款理由
	 */
	private String refundReason;
	
	public FastpayRefundParam() {
		
	}
	
	public FastpayRefundParam(String tradeNo, BigDecimal totalRefund) {
		this.tradeNo = tradeNo;
		this.totalRefund = totalRefund;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getTotalRefund() {
		return totalRefund;
	}

	public void setTotalRefund(BigDecimal totalRefund) {
		this.totalRefund = totalRefund;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

}
