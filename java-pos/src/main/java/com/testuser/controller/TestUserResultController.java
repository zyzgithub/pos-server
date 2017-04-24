package com.testuser.controller;

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
import org.jeecgframework.tag.vo.datatable.SortDirection;

import jeecg.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.testuser.entity.TestUserResult;
import com.testuser.service.TestUserResultServiceI;

/**   
 * @Title: Controller
 * @Description: 真机测试结果
 * @author leichanglin
 * @date 2014-07-22 17:41:43
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/testUserResultController")
public class TestUserResultController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TestUserResultController.class);

	@Autowired
	private TestUserResultServiceI testUserResultService;
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
	 * 真机测试结果列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "testUserResult")
	public ModelAndView testUserResult(HttpServletRequest request) {
		request.setAttribute("sessionkey", request.getParameter("sessionkey"));
		return new ModelAndView("/testuser/testUserResultList");
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
	public void datagrid(TestUserResult testUserResult,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TestUserResult.class, dataGrid);
		//查询条件组装器
		
		//该id是指ssesionKey
		String sessionkey = request.getParameter("sessionkey");
		if(sessionkey != null && sessionkey.length() > 0){
			cq.eq("sessionKey", sessionkey);
		}else{
			cq.eq("sessionkey", "isnull");
		}
		
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, testUserResult);
		//排序
		cq.addOrder("dated",SortDirection.desc);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除真机测试结果
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TestUserResult testUserResult, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		testUserResult = systemService.getEntity(TestUserResult.class, testUserResult.getId());
		message = "删除成功";
		testUserResultService.delete(testUserResult);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加真机测试结果
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(TestUserResult testUserResult, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(testUserResult.getId())) {
			message = "更新成功";
			TestUserResult t = testUserResultService.get(TestUserResult.class, testUserResult.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(testUserResult, t);
				testUserResultService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			testUserResultService.save(testUserResult);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 真机测试结果列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TestUserResult testUserResult, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(testUserResult.getId())) {
			testUserResult = testUserResultService.getEntity(TestUserResult.class, testUserResult.getId());
			testUserResult.setParams(testUserResult.getParams().replaceAll("\\\\", ""));
			testUserResult.setResult(testUserResult.getResult().replaceAll("\\\\", ""));
			req.setAttribute("testUserResultPage", testUserResult);
		}
		return new ModelAndView("/testuser/testUserResult");
	}
	
	//获取结果
	@RequestMapping(params = "getResult")
	@ResponseBody
	public String getResult(TestUserResult testUserResult, HttpServletRequest request,String id) {
		testUserResult = testUserResultService.getEntity(TestUserResult.class, testUserResult.getId());
		if(testUserResult != null){
			return testUserResult.getResult().replaceAll("\\\\", "");
		}else{
			return null;
		}
	}
}
