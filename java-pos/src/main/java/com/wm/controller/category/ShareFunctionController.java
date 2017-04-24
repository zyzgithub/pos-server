package com.wm.controller.category;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wm.service.category.ShareFunctionService;

@Controller
@RequestMapping("/shareFunctionController")
public class ShareFunctionController {
	
	@Autowired
	private ShareFunctionService shareFunctionService;

	@RequestMapping(params = "loadAdministrativeAddress")
	@ResponseBody
	public AjaxJson loadAdministrativeAddress() {
		JSONObject responseContent = shareFunctionService.loadAdministrativeAddress();
		
		AjaxJson returnContent = new AjaxJson();
		returnContent.setObj(responseContent);
		return returnContent;
	}
}