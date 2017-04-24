package com.alipay.refund;

/**
 * 即时到账批量退款 可格式化数据接口。实现了此接口的对象表示是可校验的，可格式化为字符串的。
 */
public interface FastpayRefundFormattedData extends Validatable {

	/**
	 * 格式化为字符串
	 * @return 格式化后的字符串
	 */
	String format();
	
}
