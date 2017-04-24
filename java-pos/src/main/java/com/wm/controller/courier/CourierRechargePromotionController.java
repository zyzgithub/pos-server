package com.wm.controller.courier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wp.ConfigUtil;

@Controller
@RequestMapping("ci/courierRechargePromotionController")
public class CourierRechargePromotionController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CourierRechargePromotionController.class);
	
	
	@RequestMapping(params = "getScanCampaignRechargeUrl")
	@ResponseBody
	public AjaxJson getScanCampaignRechargeUrl(Integer courierType){
		logger.info("获取二维码url：" + ConfigUtil.COURIER_SCAN_CAMPAIGN_RECHARGE_URL);
		AjaxJson json = new AjaxJson();
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(ConfigUtil.COURIER_SCAN_CAMPAIGN_RECHARGE_URL)){
			json.setObj(map);
			json.setMsg("获取快递员扫码推广二维码url失败");
			json.setStateCode("01");
			json.setSuccess(false);
			return json;
		}
		map.put("rechargeUrl", ConfigUtil.COURIER_SCAN_CAMPAIGN_RECHARGE_URL);
		json.setObj(map);
		json.setMsg("获取快递员扫码推广二维码url成功");
		json.setStateCode("00");
		json.setSuccess(true);
		return json;
	}

}
