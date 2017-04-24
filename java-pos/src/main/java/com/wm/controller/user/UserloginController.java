package com.wm.controller.user;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.user.UserloginEntity;
import com.wm.service.user.UserloginServiceI;

/**   
 * @Title: Controller
 * @Description: user_login
 * @author wuyong
 * @date 2015-01-07 10:06:14
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/userloginController")
public class UserloginController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(UserloginController.class);

	@Autowired
	private UserloginServiceI userloginService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * user_login列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "userlogin")
	public ModelAndView userlogin(HttpServletRequest request) {
		return new ModelAndView("com/wm/user/userloginList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(UserloginEntity userlogin,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(UserloginEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, userlogin);
		this.userloginService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除user_login
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(UserloginEntity userlogin, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		userlogin = systemService.getEntity(UserloginEntity.class, userlogin.getId());
		message = "删除成功";
		userloginService.delete(userlogin);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加user_login
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(UserloginEntity userlogin, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(userlogin.getId())) {
			message = "更新成功";
			UserloginEntity t = userloginService.get(UserloginEntity.class, userlogin.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(userlogin, t);
				userloginService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			userloginService.save(userlogin);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * user_login列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(UserloginEntity userlogin, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(userlogin.getId())) {
			userlogin = userloginService.getEntity(UserloginEntity.class, userlogin.getId());
			req.setAttribute("userloginPage", userlogin);
		}
		return new ModelAndView("com/wm/user/userlogin");
	}
}
