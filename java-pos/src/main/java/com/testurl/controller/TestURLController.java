package com.testurl.controller;
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
import org.jeecgframework.tag.vo.datatable.SortDirection;

import jeecg.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.testurl.entity.TestURLEntity;
import com.testurl.service.TestURLServiceI;

/**   
 * @Title: Controller
 * @Description: test的URL
 * @author leichanglin
 * @date 2014-07-11 10:37:23
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/testURLController")
public class TestURLController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TestURLController.class);

	@Autowired
	private TestURLServiceI testURLService;
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
	 * test的URL列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "testURL")
	public ModelAndView testURL(HttpServletRequest request) {
		request.setAttribute("exeid", request.getParameter("exeid"));
		return new ModelAndView("/url/testURLList");
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
	public void datagrid(TestURLEntity testURL,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TestURLEntity.class, dataGrid);
		
		//查询条件组装器
		String exeid = request.getParameter("exeid");
		if(exeid != null && exeid.length() > 0){
			cq.eq("exeid", exeid);
		}else{
			cq.eq("exeid", "-1");
		}
		
		cq.addOrder("dated",  SortDirection.desc);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, testURL);
		this.testURLService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除test的URL
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TestURLEntity testURL, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		testURL = systemService.getEntity(TestURLEntity.class, testURL.getId());
		message = "删除成功";
		testURLService.delete(testURL);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加test的URL
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(TestURLEntity testURL, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(testURL.getId())) {
			message = "更新成功";
			TestURLEntity t = testURLService.get(TestURLEntity.class, testURL.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(testURL, t);
				testURLService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String url = request.getParameter("url");
			String exeid = request.getParameter("exeid");
			
			url = url.replaceAll("\\'", "\\\\'").replaceAll("\\&(params)", "& params");
			
			testURL.setDated(new Date());
			testURL.setExeid(exeid);
			testURL.setUrl(url);
			
			try{
				testURLService.save(testURL);
				message = "保存成功";
			}catch(Exception ex){
				message = "保存失败，请重试";
				ex.printStackTrace();
			}
			
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		return j;
	}

	/**
	 * test的URL列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TestURLEntity testURL, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(testURL.getId())) {
			testURL = testURLService.getEntity(TestURLEntity.class, testURL.getId());
			req.setAttribute("testURLPage", testURL);
		}
		return new ModelAndView("/url/testURL");
	}
}
