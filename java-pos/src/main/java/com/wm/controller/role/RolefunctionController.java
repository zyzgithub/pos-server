package com.wm.controller.role;
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

import com.wm.entity.role.RolefunctionEntity;
import com.wm.service.role.RolefunctionServiceI;

/**   
 * @Title: Controller
 * @Description: role_function
 * @author wuyong
 * @date 2015-01-07 10:03:49
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/rolefunctionController")
public class RolefunctionController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(RolefunctionController.class);

	@Autowired
	private RolefunctionServiceI rolefunctionService;
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
	 * role_function列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "rolefunction")
	public ModelAndView rolefunction(HttpServletRequest request) {
		return new ModelAndView("com/wm/role/rolefunctionList");
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
	public void datagrid(RolefunctionEntity rolefunction,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RolefunctionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, rolefunction);
		this.rolefunctionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除role_function
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RolefunctionEntity rolefunction, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		rolefunction = systemService.getEntity(RolefunctionEntity.class, rolefunction.getId());
		message = "删除成功";
		rolefunctionService.delete(rolefunction);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加role_function
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(RolefunctionEntity rolefunction, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(rolefunction.getId())) {
			message = "更新成功";
			RolefunctionEntity t = rolefunctionService.get(RolefunctionEntity.class, rolefunction.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(rolefunction, t);
				rolefunctionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			rolefunctionService.save(rolefunction);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * role_function列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(RolefunctionEntity rolefunction, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(rolefunction.getId())) {
			rolefunction = rolefunctionService.getEntity(RolefunctionEntity.class, rolefunction.getId());
			req.setAttribute("rolefunctionPage", rolefunction);
		}
		return new ModelAndView("com/wm/role/rolefunction");
	}
}
