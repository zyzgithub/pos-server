package com.exelist.controller;
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

import com.exelist.entity.ExeListEntity;
import com.exelist.service.ExeListServiceI;

/**   
 * @Title: Controller
 * @Description: 测试记录
 * @author leichanglin
 * @date 2014-07-08 11:06:08
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/exeListController")
public class ExeListController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ExeListController.class);

	@Autowired
	private ExeListServiceI exeListService;
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
	 * 测试记录列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "exeList")
	public ModelAndView exeList(HttpServletRequest request) {
		request.setAttribute("exeid", request.getParameter("exeid"));
		return new ModelAndView("/exelist/exeListList");
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
	public void datagrid(ExeListEntity exeList,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ExeListEntity.class, dataGrid);
		
		String exeid = request.getParameter("exeid");
		//查询条件组装器
		if(exeid != null && exeid.length() > 0){
			cq.eq("exeid", exeid);
		}else{
			cq.eq("exeid", "-1");
		}
		cq.addOrder("dated",  SortDirection.desc);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, exeList);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除测试记录
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ExeListEntity exeList, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		exeList = systemService.getEntity(ExeListEntity.class, exeList.getId());
		message = "删除成功";
		exeListService.delete(exeList);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加测试记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ExeListEntity exeList, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(exeList.getId())) {
			message = "更新成功";
			ExeListEntity t = exeListService.get(ExeListEntity.class, exeList.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(exeList, t);
				exeListService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String sql = request.getParameter("sql");
			String params = request.getParameter("params");
			String exeid = request.getParameter("exeid");
			
			sql = sql.replaceAll("\\'", "\\\\'");
			
			exeList.setExeid(exeid);
			if(params != null && params.length() > 0){
				params = params.replaceAll("\\'", "\\\\'");
				exeList.setParams(params);
			}
			exeList.setSql(sql);
			exeList.setDated(new Date());
			
			try{
				exeListService.save(exeList);
				message = "保存成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}catch(Exception ex){
				message = "保存失败，请重试";
				ex.printStackTrace();
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 测试记录列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ExeListEntity exeList, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(exeList.getId())) {
			exeList = exeListService.getEntity(ExeListEntity.class, exeList.getId());
			req.setAttribute("exeListPage", exeList);
		}
		return new ModelAndView("/exelist/exeList");
	}
}
