package com.wm.service.impl.pay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.config.Configs;

public class AliBarcodePayApi {
	private final static Logger logger = LoggerFactory.getLogger(AliBarcodePayApi.class);
	public final static String SUCCESS = "10000";
	
	public static final String ALI_REFUND_URL = "https://openapi.alipay.com/gateway.do"; 
	
	
	/**
	 * 支付宝退款
	 * @param params
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeRefundResponse refundOrder(Map<String, String> params)
		throws AlipayApiException{
		
		logger.info("支付宝请求退款，请求参数:{}", JSON.toJSONString(params));
		String appId = Configs.getAppid();
		String privateKey = Configs.getPrivateKey();
		String publicKey = Configs.getAlipayPublicKey();
		
		AlipayClient alipayClient = new DefaultAlipayClient(ALI_REFUND_URL, appId, privateKey, "json","UTF-8", publicKey);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		Map<String, Object> requiredParams = new HashMap<String, Object>();
		requiredParams.put("out_trade_no", params.get("out_trade_no"));
		requiredParams.put("refund_amount", params.get("refund_amount"));
		requiredParams.put("refund_reason", params.get("refund_reason"));
		requiredParams.put("out_request_no", params.get("out_request_no"));
		request.setBizContent(JSON.toJSONString(requiredParams));
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		
		logger.info("支付宝请求退款，return:{}", JSON.toJSONString(response));
		return response;
	}
	
}
