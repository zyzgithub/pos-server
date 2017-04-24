package com.wm.controller.systemconfig;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.service.systemconfig.SystemconfigServiceI;

/**   
 * @Title: Controller
 * @Description: system_config
 * @author wuyong
 * @date 2015-01-07 10:05:08
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/systemconfigController")
public class SystemconfigController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(SystemconfigController.class);

	@Autowired
	private SystemconfigServiceI systemconfigService;
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
	 * system_config列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "systemconfig")
	public ModelAndView systemconfig(HttpServletRequest request) {
		return new ModelAndView("com/wm/systemconfig/systemconfigList");
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
	public void datagrid(SystemconfigEntity systemconfig,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SystemconfigEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, systemconfig);
		this.systemconfigService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除system_config
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SystemconfigEntity systemconfig, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		systemconfig = systemService.getEntity(SystemconfigEntity.class, systemconfig.getId());
		message = "删除成功";
		systemconfigService.delete(systemconfig);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加system_config
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SystemconfigEntity systemconfig, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(systemconfig.getId())) {
			message = "更新成功";
			SystemconfigEntity t = systemconfigService.get(SystemconfigEntity.class, systemconfig.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(systemconfig, t);
				systemconfigService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			systemconfigService.save(systemconfig);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * system_config列表页面跳转
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SystemconfigEntity systemconfig, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(systemconfig.getId())) {
			systemconfig = systemconfigService.getEntity(SystemconfigEntity.class, systemconfig.getId());
			req.setAttribute("systemconfigPage", systemconfig);
		}
		return new ModelAndView("com/wm/systemconfig/systemconfig");
	}
	
	
	/**
	 * 获取系统配置值
	 * @param code 配置项code
	 * @return  配置项value
	 */
	@RequestMapping(params="getConfValue", method=RequestMethod.GET)
	@ResponseBody
	public AjaxJson getConfValue(HttpServletRequest request, String code){
		AjaxJson result = new AjaxJson();
		result.setSuccess(false);
		message = "获取系统配置失败";
		result.setMsg(message);
		if(StringUtil.isNotEmpty(code)){
			String confValue = systemconfigService.getValByCode(code);
			result.setObj(confValue);
			result.setSuccess(true);
			message = "获取系统配置成功";
			result.setMsg(message);
		}
		systemService.addLog(message, Globals.Log_Type_OTHER, Globals.Log_Leavel_INFO);
		return result;
	}
	
	/**
	 * 获取系统时间
	 * 
	 * @return HH:mm:ss
	 */
	@RequestMapping(params = "getSystemTime")
	@ResponseBody
	public AjaxJson getSystemTime() {
		AjaxJson j = new AjaxJson();
		String newDate = DateUtils.getDate("HH:mm:ss");
		j.setObj(newDate);
		j.setMsg("获取系统时间成功");
		j.setSuccess(true);
		return j;
	}
	
	@RequestMapping(params = "getReportPositionFrequency")
	@ResponseBody
	public AjaxJson getReportPositionFrequency() {
		AjaxJson result = new AjaxJson();
		result.setSuccess(false);
		try {
			String confValue = systemconfigService.getValByCode("report_pos_frequency");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("reportPositionFrequency", confValue);
			result.setObj(params);
			result.setSuccess(true);
			message = "获取快递员上传位置频率配置成功";
			result.setMsg(message);
		} 
		catch (Exception e) {
			message = "获取快递员上传位置频率配置成功";
			result.setMsg(message);
		}
		
		return result;
	}
}
