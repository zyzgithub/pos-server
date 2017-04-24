package com.wm.controller.login;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("WMLoginController")
@RequestMapping("/")
public class loginController extends BaseController {
	
	/**
	 * 进入网站首页
	 */
	@RequestMapping("index")
	public String index(){
		return "takeout/index";
	}
	
	/**
	 * 进入内部app下载页
	 */
	@RequestMapping("download")
	public String download(){
		return "takeout/download";
	}
	
	/**
	 * 进入微信版首页
	 */
	@RequestMapping("weixin")
	public String weixin(){
		return "takeout/merchantlist";
	}
	

	/**
	 * 进入接口管理后台
	 */
	@RequestMapping("admin")
	public String admin(){
		return "login/login";
	}
}
