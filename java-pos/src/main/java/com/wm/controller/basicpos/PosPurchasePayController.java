package com.wm.controller.basicpos;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.pos.PosPurchasePayServiceI;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月26日 上午11:29:00    
 * @return
 * @version 1.0
 */
@Controller
@RequestMapping("ci/posPurchasePayController")
public class PosPurchasePayController {
	private static final Logger logger = LoggerFactory.getLogger(PosPurchasePayController.class);
	
	@Autowired
	private PosPurchasePayServiceI posPurchasePayService;
	
	@RequestMapping(params = "onWeixinPay")
	@ResponseBody
	public AjaxJson onWeixinPay(@RequestParam String authCode, @RequestParam Integer mainOrderId, @RequestParam Integer merchantId){
		logger.info("超市一键采购微信扫码付款：authCode：{}, mainOrderId:{}, merchantId:{}", authCode, mainOrderId, merchantId);
		AjaxJson json = new AjaxJson();
		Map<String, String> params = new HashMap<String, String>();
		params.put("auth_code", authCode);
		params.put("mainOrderId", mainOrderId.toString());
		params.put("merchantId", merchantId.toString());
		try {
			BarcodePayResponse response = posPurchasePayService.onWeixinPay(params);
			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("付款成功");
			}else {
				json.setSuccess(false);
				json.setStateCode("01");
				json.setMsg("付款失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("付款失败");
		}
		return json;
	}
	
	@RequestMapping(params = "onAliPay")
	@ResponseBody
	public AjaxJson onAliPay(@RequestParam String authCode, @RequestParam Integer mainOrderId, @RequestParam Integer merchantId){
		logger.info("超市一键采购支付宝扫码付款：authCode：{}, mainOrderId:{}, merchantId:{}", authCode, mainOrderId, merchantId);
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);
		json.setStateCode("01");
		json.setMsg("支付宝付款异常，请选择其他支付方式");
		return json;
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("auth_code", authCode);
//		params.put("mainOrderId", mainOrderId.toString());
//		params.put("merchantId", merchantId.toString());
//		
//		try {
//			BarcodePayResponse response = posPurchasePayService.onAliPay(params);
//			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
//				json.setSuccess(true);
//				json.setStateCode("00");
//				json.setMsg("付款成功");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			json.setSuccess(false);
//			json.setStateCode("01");
//			json.setMsg("付款失败");
//		}
//		return json;
	}
	
	@RequestMapping(params = "onBalancePay")
	@ResponseBody
	public AjaxJson onBalancePay(@RequestParam Integer merchantUserId, @RequestParam String payPassword, @RequestParam Integer mainOrderId, @RequestParam Integer merchantId){
		logger.info("超市一键采购余额付款： merchantUserId:{}, payPassword:{}, mainOrderId:{}, merchantId:{}", merchantUserId, merchantUserId, mainOrderId, merchantId);
		AjaxJson json = new AjaxJson();
		try {
			json = posPurchasePayService.onBalancePay( merchantUserId, payPassword, mainOrderId, merchantId);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("付款失败");
		}
		return json;
	}

}
