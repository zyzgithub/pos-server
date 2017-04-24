package com.log.controller;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import com.log.entity.OperateLog;
import com.log.service.OperateLogServiceI;


/**   
 * @Title: Controller
 * @Description: 操作日志
 * @author leichanglin
 * @date 2014-07-24 12:48:15
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/operateLogController")
public class OperateLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OperateLogController.class);

	@Autowired
	private OperateLogServiceI operateLogService;
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
	 * 操作日志列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operateLog")
	public ModelAndView operateLog(HttpServletRequest request) {
		return new ModelAndView("/log/operateLogList");
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
	public void datagrid(OperateLog operateLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OperateLog.class, dataGrid);
		//查询条件组装器
		String beginTime = request.getParameter("operateDate_begin");
		String endTime = request.getParameter("operateDate_end");
		
		try {
			DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			if(beginTime != null){
				beginTime = beginTime + " 00:00:00";
				cq.ge("operateDate", fmt.parse(beginTime));
			}
			if(endTime != null){
				if(beginTime != null && endTime.equals(request.getParameter("operateDate_begin"))){
					endTime = endTime + " 23:59:59";
				}else{
					endTime = endTime + " 00:00:00";
				}
				cq.le("operateDate", fmt.parse(endTime));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cq.add();
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, operateLog);
		this.operateLogService.getDataGridReturn(cq, true);
		
		cq.addOrder("operateDate", SortDirection.desc);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除操作日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(OperateLog operateLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		operateLog = systemService.getEntity(OperateLog.class, operateLog.getId());
		message = "删除成功";
		operateLogService.delete(operateLog);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加操作日志
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OperateLog operateLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(operateLog.getId())) {
			message = "更新成功";
			OperateLog t = operateLogService.get(OperateLog.class, operateLog.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(operateLog, t);
				operateLogService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			operateLogService.save(operateLog);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 操作日志列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(OperateLog operateLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(operateLog.getId())) {
			operateLog = operateLogService.getEntity(OperateLog.class, operateLog.getId());
			
			//把\去掉,把双引号改成单引号
			if(operateLog.getParams() != null && operateLog.getParams().length() > 0){
				operateLog.setParams(operateLog.getParams().replaceAll("\"", "\'").replaceAll("\\\\", "")); 
			}
			
			if(operateLog.getOperateResult() != null && operateLog.getOperateResult().length() > 0){
				operateLog.setOperateResult(operateLog.getOperateResult().replaceAll("\"", "\'").replaceAll("\\\\", "")); 
			}
			
			req.setAttribute("operateLogPage", operateLog);
		}
		return new ModelAndView("/log/operateLog");
	}
}
