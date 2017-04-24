package com.wm.controller.position;
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

import com.wm.entity.position.PositionEntity;
import com.wm.service.position.PositionServiceI;

/**   
 * @Title: Controller
 * @Description: 职称表
 * @author wuyong
 * @date 2015-08-28 09:25:19
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/positionController")
public class PositionController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PositionController.class);

	@Autowired
	private PositionServiceI positionService;
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
	 * 职称表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "position")
	public ModelAndView position(HttpServletRequest request) {
		return new ModelAndView("com/wm/position/positionList");
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
	public void datagrid(PositionEntity position,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PositionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, position);
		this.positionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除职称表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(PositionEntity position, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		position = systemService.getEntity(PositionEntity.class, position.getId());
		message = "删除成功";
		positionService.delete(position);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加职称表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(PositionEntity position, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(position.getId())) {
			message = "更新成功";
			PositionEntity t = positionService.get(PositionEntity.class, position.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(position, t);
				positionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			positionService.save(position);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 职称表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(PositionEntity position, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(position.getId())) {
			position = positionService.getEntity(PositionEntity.class, position.getId());
			req.setAttribute("positionPage", position);
		}
		return new ModelAndView("com/wm/position/position");
	}
}
