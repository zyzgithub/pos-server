package com.alipay.refund.util;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 即时到账批量退款接口 批次号工具
 */
public class FastpayRefundBatchNoHelper {

	/**
	 * 日期格式，值为{@value}
	 */
	private static final String REFUND_DATE_FORMAT = "yyyyMMdd";

	/**
	 * 日期后面的随机数长度，值为{@value}
	 */
	private static final int RANDOM_NUMBER_LENGTH = 24;

	/**
	 * 创建批次号格式为：退款日期（8 位当天日期）+流水号（特定长度的随机数字）。
	 * @return 批次号
	 */
	public static String buildBatchNo() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DateFormatUtils.format(System.currentTimeMillis(), REFUND_DATE_FORMAT));
		stringBuilder.append(randomNumber(RANDOM_NUMBER_LENGTH));
		return stringBuilder.toString();
	}

	/**
	 * 生成特定长度的随机数字
	 * @param length 随机数数字长度
	 * @return 随机数字字符串
	 */
	private static String randomNumber(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
		}
		return stringBuilder.toString();
	}

}
