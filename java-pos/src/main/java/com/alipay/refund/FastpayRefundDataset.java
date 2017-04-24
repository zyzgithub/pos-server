package com.alipay.refund;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.config.AlipayConfig;

/**
 * 即时到账批量退款 交易退款数据集
 */
public class FastpayRefundDataset implements FastpayRefundFormattedData {

	private static final Logger LOGGER = LoggerFactory.getLogger(FastpayRefundDataset.class);

	/**
	 * 退款理由最大字节数
	 */
	private static final int MAX_REFUND_REASON_BYTE = 256;

	/**
	 * 连接字符串
	 */
	private static final String CARET = "^";

	/**
	 * 非法的字符
	 */
	private static final char[] ILLEGAL_CHARS = { '^', '|', '$', '#' };

	/**
	 * 即时到账批量退款-请求退款的参数
	 */
	private FastpayRefundParam fastpayRefundData;

	public FastpayRefundDataset(FastpayRefundParam fastpayRefundData) {
		this.fastpayRefundData = fastpayRefundData;
	}

	protected FastpayRefundParam getFastpayRefundData() {
		return fastpayRefundData;
	}

	@Override
	public boolean validate() {
		if (getFastpayRefundData() == null) {
			LOGGER.info("fastpayRefundData is null");
			return false;
		}
		return validateTradeNo() && validateTotalRefund() && validateRefundReason();
	}

	private boolean validateTradeNo() {
		if (StringUtils.isEmpty(getFastpayRefundData().getTradeNo())) {
			LOGGER.info("tradeNo of fastpayRefundData is empty");
			return false;
		}
		return true;
	}

	private boolean validateTotalRefund() {
		if (getFastpayRefundData().getTotalRefund() == null) {
			LOGGER.info("totalRefund is null, tradeNo[{}]", getFastpayRefundData().getTradeNo());
			return false;
		}
		if (getFastpayRefundData().getTotalRefund().compareTo(BigDecimal.ZERO) < 0) {
			LOGGER.info("totalRefund is less than zero, tradeNo[{}]", getFastpayRefundData().getTradeNo());
			return false;
		}
		return true;
	}

	private boolean validateRefundReason() {
		String refundReason = getFastpayRefundData().getRefundReason();
		if (StringUtils.isEmpty(refundReason)) {
			return true;
		}
		if (StringUtils.containsAny(refundReason, ILLEGAL_CHARS)) {
			LOGGER.info("refund reason contains illegal char ['^', '|', '$', '#'], tradeNo[{}]",
					getFastpayRefundData().getTradeNo());
			return false;
		}
		try {
			if (refundReason.getBytes(AlipayConfig.input_charset).length >= MAX_REFUND_REASON_BYTE) {
				LOGGER.info("refund reason is greater or equals {}, tradeNo[{}]", MAX_REFUND_REASON_BYTE,
						getFastpayRefundData().getTradeNo());
				return false;
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			LOGGER.info("can't get bytes with charset: {}.", AlipayConfig.input_charset);
			return false;
		}
	}

	@Override
	public String format() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getFastpayRefundData().getTradeNo()).append(CARET)
				.append(getFastpayRefundData().getTotalRefund()).append(CARET)
				.append(StringUtils.defaultString(getFastpayRefundData().getRefundReason()));
		return stringBuilder.toString();
	}

}
