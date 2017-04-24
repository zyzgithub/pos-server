package com.testuser.controller;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import jeecg.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.testuser.entity.TestUser;
import com.testuser.service.TestUserServiceI;

/**   
 * @Title: Controller
 * @Description: 测试用户
 * @author zhenjunzhuo
 * @date 2014-07-15 11:06:09
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/testUserController")
public class TestUserController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TestUserController.class);

	@Autowired
	private TestUserServiceI testUserService;
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
	 * 测试用户列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "testUser")
	public ModelAndView testUser(HttpServletRequest request) {
		return new ModelAndView("/testuser/testUserList");
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
	public void datagrid(TestUser testUser,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TestUser.class, dataGrid);
		//查询条件组装器
		cq.ge("timeOut", new Date());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, testUser);
		this.testUserService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除测试用户
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TestUser testUser, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		testUser = systemService.getEntity(TestUser.class, testUser.getId());
		message = "删除成功";
		testUserService.delete(testUser);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加测试用户
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(TestUser testUser, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(testUser.getId())) {
			message = "更新成功";
			TestUser t = testUserService.get(TestUser.class, testUser.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(testUser, t);
				testUserService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			testUserService.save(testUser);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 测试用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TestUser testUser, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(testUser.getId())) {
			testUser = testUserService.getEntity(TestUser.class, testUser.getId());
			req.setAttribute("testUserPage", testUser);
		}
		return new ModelAndView("/testuser/testUser");
	}
}
