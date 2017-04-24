package com.wm.util.innerAPI;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.ValidUtil;
import com.wm.util.HttpUtils;

@Component
public class WMApiCall {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${supply_mc_host}") 
	private String supplyMcHost;
	
	/**
	 * 供应链支付宝支付回调，1.5项目调用供应链supplymc项目更新订单
	 * @author suyuqiang
	 * @return
	 */
	public AjaxJson aliConfirmPay(String outTradeNo, String transactionId, String totalFee) {
		AjaxJson json = new AjaxJson();
		
		String url = supplyMcHost + "/orderController.do?aliPayNotify";
		JSONObject params = new JSONObject();
		params.put("outTradeNo", outTradeNo);
		params.put("transactionId", transactionId);
		params.put("totalFee", totalFee);

		json = HttpUtils.post(url, params, true, AjaxJson.class, false);
		if (ValidUtil.anyEmpty(json)){
			json.setMsg("支付宝回调，更新订单支付状态失败，调用结果为空");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		
		logger.info("供应链支付宝支付回调，1.5项目调用supplymc项目更新订单，返回结果：payId"+ outTradeNo +",success:" + json.isSuccess());
		return json;
	}
}
