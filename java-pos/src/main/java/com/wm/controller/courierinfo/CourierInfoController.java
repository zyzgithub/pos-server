package com.wm.controller.courierinfo;
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

import com.wm.entity.courierinfo.CourierInfoEntity;
import com.wm.service.courierinfo.CourierInfoServiceI;

/**   
 * @Title: Controller
 * @Description: 快递员用户信息
 * @author wuyong
 * @date 2015-09-01 17:09:34
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierInfoController")
public class CourierInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierInfoController.class);

	@Autowired
	private CourierInfoServiceI courierInfoService;
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
	 * 快递员用户信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierInfo")
	public ModelAndView courierInfo(HttpServletRequest request) {
		return new ModelAndView("com/wm/courierinfo/courierInfoList");
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
	public void datagrid(CourierInfoEntity courierInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierInfoEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierInfo);
		this.courierInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除快递员用户信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CourierInfoEntity courierInfo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		courierInfo = systemService.getEntity(CourierInfoEntity.class, courierInfo.getId());
		message = "删除成功";
		courierInfoService.delete(courierInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加快递员用户信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CourierInfoEntity courierInfo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(courierInfo.getId())) {
			message = "更新成功";
			CourierInfoEntity t = courierInfoService.get(CourierInfoEntity.class, courierInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierInfo, t);
				courierInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierInfoService.save(courierInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 快递员用户信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CourierInfoEntity courierInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(courierInfo.getId())) {
			courierInfo = courierInfoService.getEntity(CourierInfoEntity.class, courierInfo.getId());
			req.setAttribute("courierInfoPage", courierInfo);
		}
		return new ModelAndView("com/wm/courierinfo/courierInfo");
	}
}
