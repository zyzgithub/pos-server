package com.wm.controller.courierorg;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.courierorg.CourierOrgEntity;
import com.wm.service.courierorg.CourierOrgServiceI;

/**   
 * @Title: Controller
 * @Description: 关联表：快递员-组织架构
 * @author wuyong
 * @date 2015-08-28 09:14:44
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierOrgController")
public class CourierOrgController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierOrgController.class);

	@Autowired
	private CourierOrgServiceI courierOrgService;
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
	 * 关联表：快递员-组织架构列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierOrg")
	public ModelAndView courierOrg(HttpServletRequest request) {
		return new ModelAndView("com/wm/courierOrg/courierOrgList");
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
	public void datagrid(CourierOrgEntity courierOrg,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierOrgEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierOrg);
		this.courierOrgService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


}
