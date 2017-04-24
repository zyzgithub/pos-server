package com.wm.service.impl.order.refund;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wm.controller.takeout.vo.WeChatRefundVo;
import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.refund.OrderRefundExecutor;
import com.wp.ConfigUtil;
import com.wp.PayService;
import com.wp.XMLUtil;
import com.wxpay.util.ClientCustomSSL;

@Component("weixinOrderRefundExecutor")
public class WeixinOrderRefundExecutor implements OrderRefundExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeixinOrderRefundExecutor.class);

	@Override
	public OrderRefundResult execute(DineInOrderRefundParam orderRefundParam) {
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		try {
			String requestXML = getRequestXML(orderRefundParam, outRefundNo);
			String responseStr = ClientCustomSSL.doRefund(ConfigUtil.REFUND_URL, requestXML);
			LOGGER.info("WeixinRefun orderId {} response:\n{}", orderRefundParam.getOrder().getId(), responseStr);
			boolean success = isSuccess(responseStr);
			return new OrderRefundResult(success, outRefundNo);
		} catch (Exception e) {
			LOGGER.info("WeixinRefund exception.", e);
		}
		return OrderRefundResult.failResult(outRefundNo);
	}

	private String getRequestXML(DineInOrderRefundParam orderRefundParam, String outRefundNo) {
		OrderEntity order = orderRefundParam.getOrder();
		String totalFee = yuanToFen(String.valueOf(order.getOnlineMoney()));
		String refundFee = yuanToFen(orderRefundParam.getRefundFee());
		String requestXML = PayService.createRefundJSPackage(order.getOutTraceId(), order.getPayId(), outRefundNo,
				totalFee, refundFee);
		LOGGER.info("WeixinRefund params:\n{}", requestXML);
		return requestXML;
	}

	private String yuanToFen(String money) {
		BigDecimal origin = new BigDecimal(money);
		BigDecimal multiple = new BigDecimal(100);
		return origin.multiply(multiple).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	private boolean isSuccess(String responseStr) throws JDOMException, IOException {
		Map<String, String> responseMap = XMLUtil.doXMLParse(responseStr);
		return WeChatRefundVo.RETURN_CODE_SUCCESS.equals(responseMap.get("return_code"))
				&& WeChatRefundVo.RESULT_CODE_SUCCESS.equals(responseMap.get("result_code"));
	}

}
