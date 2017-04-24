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

import com.wm.entity.role.RoleuserEntity;
import com.wm.service.role.RoleuserServiceI;

/**   
 * @Title: Controller
 * @Description: role_user
 * @author wuyong
 * @date 2015-01-07 10:04:26
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/roleuserController")
public class RoleuserController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(RoleuserController.class);

	@Autowired
	private RoleuserServiceI roleuserService;
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
	 * role_user列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "roleuser")
	public ModelAndView roleuser(HttpServletRequest request) {
		return new ModelAndView("com/wm/role/roleuserList");
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
	public void datagrid(RoleuserEntity roleuser,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RoleuserEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, roleuser);
		this.roleuserService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除role_user
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RoleuserEntity roleuser, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		roleuser = systemService.getEntity(RoleuserEntity.class, roleuser.getId());
		message = "删除成功";
		roleuserService.delete(roleuser);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加role_user
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(RoleuserEntity roleuser, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(roleuser.getId())) {
			message = "更新成功";
			RoleuserEntity t = roleuserService.get(RoleuserEntity.class, roleuser.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(roleuser, t);
				roleuserService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			roleuserService.save(roleuser);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * role_user列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(RoleuserEntity roleuser, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(roleuser.getId())) {
			roleuser = roleuserService.getEntity(RoleuserEntity.class, roleuser.getId());
			req.setAttribute("roleuserPage", roleuser);
		}
		return new ModelAndView("com/wm/role/roleuser");
	}
}
