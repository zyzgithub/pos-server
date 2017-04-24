package com.wm.service.impl.order.refund;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.dto.order.WftRefundRequest;
import com.wm.dto.order.WftRefundResponse;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.refund.OrderRefundExecutor;
import com.wm.util.HttpClientUtil;
import com.wp.ConfigUtil;

@Component("wftOrderRefundExecutor")
public class WftOrderRefundExecutor implements OrderRefundExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WftOrderRefundExecutor.class);

	@Override
	public OrderRefundResult execute(DineInOrderRefundParam orderRefundParam) {
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		try {
			boolean success = refund(orderRefundParam, outRefundNo);
			return new OrderRefundResult(success, outRefundNo);
		} catch (IOException e) {
			LOGGER.info("WftRefund exception.", e);
		}
		return OrderRefundResult.failResult(outRefundNo);
	}

	private boolean refund(DineInOrderRefundParam orderRefundParam, String outRefundNo) throws IOException {
		HttpPost httpPost = new HttpPost(ConfigUtil.PAY_REFUND_URL_WFT);
		httpPost.setEntity(buildEntity(orderRefundParam, outRefundNo));
		WftRefundResponse wftRefundResponse = HttpClientUtil.execute(httpPost, WftRefundResponse.class);
		if (wftRefundResponse == null) {
			return false;
		}
		if (!WftRefundResponse.RESULT_CODE_SUCCESS.equals(wftRefundResponse.getResultCode())) {
			LOGGER.info("WftRefund orderId {} response: {}", orderRefundParam.getOrder().getId(), wftRefundResponse.getMsg());
			return false;
		}
		return true;
	}

	private HttpEntity buildEntity(DineInOrderRefundParam orderRefundParam, String outRefundNo) throws UnsupportedEncodingException {
		OrderEntity order = orderRefundParam.getOrder();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("refundNo", outRefundNo));
		params.add(new BasicNameValuePair("tradeNo", order.getPayId()));
		params.add(new BasicNameValuePair("refundChannel", WftRefundRequest.REFUND_CHANNEL_ORIGINAL));
		String totalFee = yuanToFen(String.valueOf(order.getOnlineMoney()));
		params.add(new BasicNameValuePair("totalFee", totalFee));
		String refundFee = yuanToFen(orderRefundParam.getRefundFee());
		params.add(new BasicNameValuePair("refundFee", refundFee));
		params.add(new BasicNameValuePair("transactionId", order.getOutTraceId()));
		LOGGER.info("WftRefund params - {}", paramsLog(params));
		return new UrlEncodedFormEntity(params, "utf-8");
	}

	private String yuanToFen(String money) {
		BigDecimal origin = new BigDecimal(money);
		BigDecimal multiple = new BigDecimal(100);
		return origin.multiply(multiple).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	private String paramsLog(List<NameValuePair> params) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			NameValuePair nameValuePair = params.get(i);
			stringBuilder.append(nameValuePair.getName()).append(":").append(nameValuePair.getValue());
			if (i < params.size() - 1) {
				stringBuilder.append(", ");
			}
		}
		return stringBuilder.toString();
	}

}
