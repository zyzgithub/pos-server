package com.wm.controller.menu;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.menu.MenuServiceI;

/**   
 * @Title: Controller
 * @Description: menu
 * @author wuyong
 * @date 2015-01-07 09:58:45
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/menuController")
public class MenuController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@RequestMapping(params = "getMenuList")
	@ResponseBody
	public AjaxJson getMenuList(HttpServletRequest request,
			HttpServletResponse response,Integer merchant_id) {
		AjaxJson j = new AjaxJson();
		int uId=0;
		if(request.getParameter("userId")!=null &&!"".equals(request.getParameter("userId").trim()) ){
			uId=Integer.parseInt(request.getParameter("userId"));
		}
		List<Map<String, Object>> list = menuService.getMenuList(merchant_id,uId);
		
		j.setObj(list);
		return j;
	}
	
}


