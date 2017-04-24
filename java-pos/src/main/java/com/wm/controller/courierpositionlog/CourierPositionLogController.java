package com.wm.controller.courierpositionlog;
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

import com.wm.entity.courierpositionlog.CourierPositionLogEntity;
import com.wm.service.courierpositionlog.CourierPositionLogServiceI;

/**   
 * @Title: Controller
 * @Description: 快递员调岗记录表
 * @author wuyong
 * @date 2015-09-01 17:01:31
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierPositionLogController")
public class CourierPositionLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierPositionLogController.class);

	@Autowired
	private CourierPositionLogServiceI courierPositionLogService;
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
	 * 快递员调岗记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierPositionLog")
	public ModelAndView courierPositionLog(HttpServletRequest request) {
		return new ModelAndView("com/wm/courierpositionlog/courierPositionLogList");
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
	public void datagrid(CourierPositionLogEntity courierPositionLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierPositionLogEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierPositionLog);
		this.courierPositionLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除快递员调岗记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CourierPositionLogEntity courierPositionLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		courierPositionLog = systemService.getEntity(CourierPositionLogEntity.class, courierPositionLog.getId());
		message = "删除成功";
		courierPositionLogService.delete(courierPositionLog);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加快递员调岗记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CourierPositionLogEntity courierPositionLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(courierPositionLog.getId())) {
			message = "更新成功";
			CourierPositionLogEntity t = courierPositionLogService.get(CourierPositionLogEntity.class, courierPositionLog.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierPositionLog, t);
				courierPositionLogService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierPositionLogService.save(courierPositionLog);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 快递员调岗记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CourierPositionLogEntity courierPositionLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(courierPositionLog.getId())) {
			courierPositionLog = courierPositionLogService.getEntity(CourierPositionLogEntity.class, courierPositionLog.getId());
			req.setAttribute("courierPositionLogPage", courierPositionLog);
		}
		return new ModelAndView("com/wm/courierpositionlog/courierPositionLog");
	}
}
