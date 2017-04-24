package com.courier_mana.common.contoller;

import java.util.ArrayList;
import java.util.List;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础控制器
 */
@Controller
@RequestMapping("/basicController")
public class BasicController {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static AjaxJson SUCCESS(){
		AjaxJson json = new AjaxJson();
		return json;
	}
	
	public static AjaxJson SUCCESS(String message) {
		AjaxJson json = new AjaxJson();
		json.setMsg(message);
		return json;
	}
	
	public static AjaxJson SUCCESS(Object obj) {
		AjaxJson json = new AjaxJson();
		json.setObj(obj);
		return json;
	}
	
	
	public static AjaxJson SUCCESS(String message,Object obj) {
		AjaxJson json = new AjaxJson();
		json.setMsg(message);
		json.setObj(obj);
		return json;
	}
	
	public static AjaxJson FAIL(String message) {
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);
		json.setStateCode("01");
		json.setMsg(message);
		return json;
	}

	public static AjaxJson FAIL() {
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);
		json.setStateCode("01");
		json.setMsg("操作失败");
		return json;
	}

	public static AjaxJson ERROR(BindingResult bindingResult) {
		List<String> errors=new ArrayList<String>(); 
		List<ObjectError> allErrors = bindingResult.getAllErrors();
		
		for (ObjectError objectError : allErrors){
			// 输出错误信息
			errors.add(objectError.getDefaultMessage());
		}
		
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);		
		json.setStateCode("02");
		json.setMsg("验证错误");
		json.setObj(errors);
		
		return json;
	}
	
	public static AjaxJson ERROR(String message) {
		List<String> errors=new ArrayList<String>(); 
		errors.add(message);
		
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);		
		json.setStateCode("02");
		json.setMsg("验证错误");
		json.setObj(errors);
		
		return json;
	}
	
	public static AjaxJson FAIL(String code, String message) {
		AjaxJson json = new AjaxJson();
		json.setSuccess(false);
		json.setStateCode(code);
		json.setMsg(message);
		return json;
	}
	
}
