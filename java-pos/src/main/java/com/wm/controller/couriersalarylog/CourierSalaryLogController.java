package com.wm.controller.couriersalarylog;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
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

import com.wm.entity.couriersalarylog.CourierSalaryLogEntity;
import com.wm.service.couriersalarylog.CourierSalaryLogServiceI;

/**   
 * @Title: Controller
 * @Description: 快递员调薪记录表
 * @author wuyong
 * @date 2015-09-01 17:05:30
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierSalaryLogController")
public class CourierSalaryLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierSalaryLogController.class);

	@Autowired
	private CourierSalaryLogServiceI courierSalaryLogService;
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
	 * 快递员调薪记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierSalaryLog")
	public ModelAndView courierSalaryLog(HttpServletRequest request) {
		return new ModelAndView("com/wm/couriersalarylog/courierSalaryLogList");
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
	public void datagrid(CourierSalaryLogEntity courierSalaryLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierSalaryLogEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierSalaryLog);
		this.courierSalaryLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除快递员调薪记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CourierSalaryLogEntity courierSalaryLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		courierSalaryLog = systemService.getEntity(CourierSalaryLogEntity.class, courierSalaryLog.getId());
		message = "删除成功";
		courierSalaryLogService.delete(courierSalaryLog);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加快递员调薪记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CourierSalaryLogEntity courierSalaryLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(courierSalaryLog.getId())) {
			message = "更新成功";
			CourierSalaryLogEntity t = courierSalaryLogService.get(CourierSalaryLogEntity.class, courierSalaryLog.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierSalaryLog, t);
				courierSalaryLogService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierSalaryLogService.save(courierSalaryLog);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 快递员调薪记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CourierSalaryLogEntity courierSalaryLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(courierSalaryLog.getId())) {
			courierSalaryLog = courierSalaryLogService.getEntity(CourierSalaryLogEntity.class, courierSalaryLog.getId());
			req.setAttribute("courierSalaryLogPage", courierSalaryLog);
		}
		return new ModelAndView("com/wm/couriersalarylog/courierSalaryLog");
	}
}
