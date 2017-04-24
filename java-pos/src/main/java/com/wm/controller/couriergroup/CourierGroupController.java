package com.wm.controller.couriergroup;
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

import com.wm.entity.couriergroup.CourierGroupEntity;
import com.wm.service.couriergroup.CourierGroupServiceI;

/**   
 * @Title: Controller
 * @Description: 快递员群组
 * @author wuyong
 * @date 2015-08-28 09:30:44
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierGroupController")
public class CourierGroupController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierGroupController.class);

	@Autowired
	private CourierGroupServiceI courierGroupService;
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
	 * 快递员群组列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierGroup")
	public ModelAndView courierGroup(HttpServletRequest request) {
		return new ModelAndView("com/wm/couriergroup/courierGroupList");
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
	public void datagrid(CourierGroupEntity courierGroup,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierGroupEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierGroup);
		this.courierGroupService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除快递员群组
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CourierGroupEntity courierGroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		courierGroup = systemService.getEntity(CourierGroupEntity.class, courierGroup.getId());
		message = "删除成功";
		courierGroupService.delete(courierGroup);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加快递员群组
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CourierGroupEntity courierGroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(courierGroup.getId())) {
			message = "更新成功";
			CourierGroupEntity t = courierGroupService.get(CourierGroupEntity.class, courierGroup.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierGroup, t);
				courierGroupService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierGroupService.save(courierGroup);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 快递员群组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CourierGroupEntity courierGroup, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(courierGroup.getId())) {
			courierGroup = courierGroupService.getEntity(CourierGroupEntity.class, courierGroup.getId());
			req.setAttribute("courierGroupPage", courierGroup);
		}
		return new ModelAndView("com/wm/couriergroup/courierGroup");
	}
}
