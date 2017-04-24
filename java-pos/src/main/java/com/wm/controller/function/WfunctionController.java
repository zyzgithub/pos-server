package com.wm.controller.function;
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

import com.wm.entity.function.WfunctionEntity;
import com.wm.service.function.WfunctionServiceI;

/**   
 * @Title: Controller
 * @Description: function
 * @author wuyong
 * @date 2015-01-07 11:32:09
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/wfunctionController")
public class WfunctionController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(WfunctionController.class);

	@Autowired
	private WfunctionServiceI wfunctionService;
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
	 * function列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "wfunction")
	public ModelAndView wfunction(HttpServletRequest request) {
		return new ModelAndView("com/wm/function/wfunctionList");
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
	public void datagrid(WfunctionEntity wfunction,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WfunctionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wfunction);
		this.wfunctionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除function
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(WfunctionEntity wfunction, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wfunction = systemService.getEntity(WfunctionEntity.class, wfunction.getId());
		message = "删除成功";
		wfunctionService.delete(wfunction);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加function
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WfunctionEntity wfunction, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(wfunction.getId())) {
			message = "更新成功";
			WfunctionEntity t = wfunctionService.get(WfunctionEntity.class, wfunction.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(wfunction, t);
				wfunctionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			wfunctionService.save(wfunction);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * function列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(WfunctionEntity wfunction, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wfunction.getId())) {
			wfunction = wfunctionService.getEntity(WfunctionEntity.class, wfunction.getId());
			req.setAttribute("wfunctionPage", wfunction);
		}
		return new ModelAndView("com/wm/function/wfunction");
	}
}
