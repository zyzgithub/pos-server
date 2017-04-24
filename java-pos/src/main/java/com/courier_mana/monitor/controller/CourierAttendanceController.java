package com.courier_mana.monitor.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.monitor.service.CourierAttendanceServiceI;

@Controller
@RequestMapping("courier/attendance")
public class CourierAttendanceController extends BasicController{
	
	private final static Logger logger = LoggerFactory.getLogger(CourierAttendanceController.class);
	
	@Autowired
	private CourierAttendanceServiceI courierAttendanceServiceI;
	
	@RequestMapping("/getCourierOndutySum")
	@ResponseBody
	public AjaxJson getCourierOndutySum(Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getManageOrgs, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierAttendanceServiceI.getCourierOndutySum(courierId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取快递员所管辖的快递员的上班人数失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	
	

}
