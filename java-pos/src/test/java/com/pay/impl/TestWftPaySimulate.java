package com.pay.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.pay.TestPaySimulate;
import com.wm.util.security.HttpUtils;
import com.wp.XMLUtil;

public class TestWftPaySimulate implements TestPaySimulate {

	private static final Logger logger = LoggerFactory.getLogger(TestWftPaySimulate.class);

	private static String wftPayUrlTest = "http://apptest.0085.com/pay/pay/weixin/payRetQuery.action?orderNo=PAY_ID";
	private static String wftNotifyUrlTest = "http://apptest.0085.com/takeOutController/wftnotify.do";

	private static String wftPayUrlProd = "http://pay.0085.com/pay/pay/weixin/payRetQuery.action?orderNo=PAY_ID";
	private static String wftNotifyUrlProd = "http://no1.0085.com/takeOutController/wftnotify.do";
	

	/**
	 * 获取订单状态
	 * @param setting test：测试环境，prod：生产环境
	 * @param payId 订单号
	 * @return
	 */
	public JSONObject getOrderStatus(String setting, String payId) {
		String url = wftPayUrlTest.replace("PAY_ID", payId);
		if("prod".equals(setting)){
			url = wftPayUrlProd.replace("PAY_ID", payId);
		}
		return HttpUtils.get(url);
	}

	/**
	 * 威富通支付补单
	 * @param payUrl
	 * @param notifyUrl 1.5支付回调接口
	 * @param payId
	 * @throws
	 */
	public void fixOrder(String setting, String payId) {
		JSONObject json = this.getOrderStatus(setting, payId);
		String url = wftNotifyUrlTest;
		if("prod".equals(setting)){
			url = wftNotifyUrlProd;
		}
		logger.info("威富通返回:{}", json);
		if ("SUCCESS".equals(json.getString("trade_state"))) {
			JSONObject detail = json.getJSONObject("detail");
			JSONObject postMap = new JSONObject();
			postMap.put("result_code", "FAIL");
			if ("0".equals(detail.getString("result_code"))) {
				postMap.put("result_code", "SUCCESS");
			}
			postMap.put("total_fee", detail.getString("total_fee"));
			postMap.put("out_trade_no", detail.getString("out_trade_no"));
			postMap.put("transaction_id", detail.getString("transaction_id"));
			String ret = HttpUtils.postStr(url, postMap, false);
			logger.info("1号生活返回:{}", ret);
			if(StringUtils.isNotEmpty(ret)){
				Map<String, String> retMap;
				try {
					retMap = XMLUtil.doXMLParse(ret);
					if(retMap != null && "SUCCESS".equals(retMap.get("return_code"))){
						logger.info("fix success payId:{}", payId);
					} else {
						logger.error("fix failed payId:{}", payId);
					}
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				logger.error("fix failed payId:{}", payId);
			}
		} else {
			logger.error("该订单:{} 未支付成功！！！", payId);
		}
	}
	
	public static void main(String[] args) {
		TestPaySimulate simulate = new TestWftPaySimulate();
		String payIds = "3060220000681885128958,3060220000771993510824,3060220000861126814800,3060220000252270468282,3060220000600674394502,3020220000851522500099,3020120000871439399561,3060220000431255374926,3060220000790266904207,3060220000551759214765";
		for(String payId : payIds.split(",")){
			simulate.fixOrder("test", payId);
		}
	}
}
