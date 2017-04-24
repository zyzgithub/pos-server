package com.wxpay.refund;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.util.JacksonUtil;

public class WxRefundResponseValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(WxRefundResponseValidator.class);

	@SuppressWarnings("unchecked")
	public boolean validate(WxRefundResponse refundResponse) {
		if (refundResponse.getSign() == null) {
			return false;
		}
		try {
			Map<String, String> map = JacksonUtil.readValue(JacksonUtil.writeValueAsString(refundResponse), Map.class);
			Map<String, String> filteredMap = WxRefundCore.filterNull(map);
			String calculatedSign = WxRefundCore.getSign(filteredMap);
			return refundResponse.getSign().equals(calculatedSign);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}

}
