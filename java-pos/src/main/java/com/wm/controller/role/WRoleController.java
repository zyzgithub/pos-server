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

import com.wm.entity.role.WRoleEntity;
import com.wm.service.role.WRoleServiceI;

/**   
 * @Title: Controller
 * @Description: role
 * @author wuyong
 * @date 2015-01-07 11:35:23
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/wRoleController")
public class WRoleController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(WRoleController.class);

	@Autowired
	private WRoleServiceI wRoleService;
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
	 * role列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "wRole")
	public ModelAndView wRole(HttpServletRequest request) {
		return new ModelAndView("com/wm/role/wRoleList");
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
	public void datagrid(WRoleEntity wRole,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WRoleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wRole);
		this.wRoleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除role
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(WRoleEntity wRole, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wRole = systemService.getEntity(WRoleEntity.class, wRole.getId());
		message = "删除成功";
		wRoleService.delete(wRole);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加role
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WRoleEntity wRole, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(wRole.getId())) {
			message = "更新成功";
			WRoleEntity t = wRoleService.get(WRoleEntity.class, wRole.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(wRole, t);
				wRoleService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			wRoleService.save(wRole);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * role列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(WRoleEntity wRole, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wRole.getId())) {
			wRole = wRoleService.getEntity(WRoleEntity.class, wRole.getId());
			req.setAttribute("wRolePage", wRole);
		}
		return new ModelAndView("com/wm/role/wRole");
	}
}
