package com.alipay.refund.nopwd;

/**
 * 即时到账批量退款无密接口 发送退款请求的结果
 */
public class NopwdFastpayRefundApplyResult {

	/**
	 * 发送退款请求是否成功
	 */
	private boolean success;

	/**
	 * 发送退款请求的批次号
	 */
	private String batchNo;
	
	/**
	 * 创建表示退款请求失败的结果
	 * @param batchNo 批次号
	 * @return 表示退款请求失败的结果
	 */
	public static NopwdFastpayRefundApplyResult failResult(String batchNo) {
		return new NopwdFastpayRefundApplyResult(false, batchNo);
	}
	
	public NopwdFastpayRefundApplyResult(boolean success, String batchNo) {
		this.success = success;
		this.batchNo = batchNo;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getBatchNo() {
		return batchNo;
	}

}
