package com.wm.controller.couriertrainresource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.wm.service.couriertrainresource.CourierTrainResourceServiceI;

@Controller
@RequestMapping("ci/courierTrainResourceController")
public class CourierTrainResourceController extends BasicController {
	
	@Autowired
	private CourierTrainResourceServiceI courierTrainResourceService;
	
	/**
	 * 分页获取培训资料类型
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getCourierTrainType")
	@ResponseBody
	public AjaxJson getCourierTrainType(@RequestParam int page, @RequestParam int rows){
		AjaxJson json = new AjaxJson();
		List<Map<String, Object>> courierTrainType = new ArrayList<Map<String,Object>>();
		try {
			courierTrainType = courierTrainResourceService.getCourierTrainType(page, rows);
			json.setObj(courierTrainType);
			json.setMsg("获取培训资料类型成功");
			json.setStateCode("00");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setObj(courierTrainType);
			json.setMsg("获取培训资料类型失败");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 根据快递员类型和培训资料的类型， 分页获取培训资料名称
	 * @param courierType
	 * @param trainType
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getCourierTrainInfo")
	@ResponseBody
	public AjaxJson getCourierTrainInfo(@RequestParam Integer courierType, @RequestParam Integer type, @RequestParam int page, @RequestParam int rows){
		AjaxJson json = new AjaxJson();
		List<Map<String, Object>> courierTrainInfo = new ArrayList<Map<String,Object>>();
		try {
			courierTrainInfo = courierTrainResourceService.getCourierTrainInfo(courierType, type, page, rows);
			json.setObj(courierTrainInfo);
			json.setMsg("获取培训资料成功");
			json.setStateCode("00");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setObj(courierTrainInfo);
			json.setMsg("获取培训资料失败");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 根据id获取培训资料url
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getCourierTrainUrl")
	@ResponseBody
	public AjaxJson getCourierTrainUrl(@RequestParam Integer id){
		AjaxJson json = new AjaxJson();
		Map<String, Object> courierTrainUrl = new HashMap<String, Object>();
		try {
			courierTrainUrl = courierTrainResourceService.getCourierTrainUrl(id);
			json.setObj(courierTrainUrl);
			json.setMsg("获取培训资料URL成功");
			json.setStateCode("00");
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			json.setObj(courierTrainUrl);
			json.setMsg("获取培训资料URL失败");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		return json;
	}
}
