package com.courier_mana.monitor.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.monitor.service.CourierMerchantServicI;


@Controller
@RequestMapping("ci/courier/merchant")
public class CourierMerchantController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierMerchantController.class);
	
	@Autowired
	private CourierMerchantServicI courierMerchantServicImpl;
	
	@RequestMapping("/get_manage_merchants_count")
	@ResponseBody
	public AjaxJson getManageMerchantsCount(@RequestParam Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getManageMerchantsCount, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierMerchantServicImpl.getAllAndUnBindMerchantCount(courierId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取商家列表失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
}
