package com.courier_mana.statistics.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.RetainedReportService;

@Controller
@RequestMapping("/ci/retainedReport")
public class RetainedReportController extends BasicController {
	
	@Autowired
	private RetainedReportService retainedReportService;
	
	@ResponseBody
	@RequestMapping("/getRetainedReport")
	public AjaxJson getRetainedReport(SearchVo timeType, Integer warehouseId){
		AjaxJson result = BasicController.SUCCESS();
		try {
			JSONObject response = this.retainedReportService.getRetainedReportData(timeType, warehouseId);
			// 远程接口返回的code
			int code = response.getIntValue("code");
			// 远程接口返回的msg
			String msg = response.getString("msg");
			
			// 将响应重新组装
			if(code == 0){
				Object obj = response.getJSONObject("response").get("list");
				result.setObj(obj);
			} else {
				result = BasicController.FAIL(code + "", msg);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BasicController.FAIL("出错了.");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getAllWarehouseInfo")
	public AjaxJson getAllWarehouseInfo(){
		AjaxJson result = BasicController.SUCCESS();
		try {
			JSONObject response = this.retainedReportService.getAllWarehouseInfo();
			// 远程接口返回的code
			int code = response.getIntValue("code");
			// 远程接口返回的msg
			String msg = response.getString("msg");
			// 将响应重新组装
			if(code == 0){
				Object obj = response.getJSONObject("response").get("list");
				result.setObj(obj);
			} else {
				result = BasicController.FAIL(code + "", msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BasicController.FAIL("出错了.");
		}
		return result;
	}
}
