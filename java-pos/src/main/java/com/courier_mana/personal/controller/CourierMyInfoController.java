package com.courier_mana.personal.controller;

import java.util.Map;

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
import com.courier_mana.personal.service.CourierMyInfoService;


@Controller
@RequestMapping("/ci/courier/myinfo")
public class CourierMyInfoController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierMyInfoController.class);
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@RequestMapping("/getMyInfo")
	@ResponseBody
	public AjaxJson getMyInfo(@RequestParam Integer courierId){
		logger.info("invoke method getMyInfo, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				return FAIL("01", "参数错误");
			}
			else {
				Map<String, Object> user = courierMyInfoService.getMyInfo(courierId);
				logger.info("return result:{}", JSON.toJSONString(user));
				return SUCCESS(user);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return FAIL("02", "获取个人资料失败");
		}
	}
}
