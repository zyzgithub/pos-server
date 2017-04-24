package com.wm.controller.courierposition;
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

import com.wm.entity.courierposition.CourierPositionEntity;
import com.wm.service.courierposition.CourierPositionServiceI;

/**   
 * @Title: Controller
 * @Description: 关联表：快递员-职称
 * @author wuyong
 * @date 2015-08-28 09:29:40
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierPositionController")
public class CourierPositionController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierPositionController.class);

	@Autowired
	private CourierPositionServiceI courierPositionService;
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
	 * 关联表：快递员-职称列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierPosition")
	public ModelAndView courierPosition(HttpServletRequest request) {
		return new ModelAndView("com/wm/courierposition/courierPositionList");
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
	public void datagrid(CourierPositionEntity courierPosition,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierPositionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierPosition);
		this.courierPositionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

}
