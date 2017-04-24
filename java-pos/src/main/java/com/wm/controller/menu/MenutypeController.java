package com.wm.controller.menu;
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

import com.wm.entity.menu.MenutypeEntity;
import com.wm.service.menu.MenutypeServiceI;

/**   
 * @Title: Controller
 * @Description: menu_type
 * @author wuyong
 * @date 2015-01-07 09:59:20
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/menutypeController")
public class MenutypeController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(MenutypeController.class);

	@Autowired
	private MenutypeServiceI menutypeService;
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
	 * menu_type列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "menutype")
	public ModelAndView menutype(HttpServletRequest request) {
		return new ModelAndView("com/wm/menu/menutypeList");
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
	public void datagrid(MenutypeEntity menutype,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MenutypeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, menutype);
		this.menutypeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除menu_type
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MenutypeEntity menutype, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		menutype = systemService.getEntity(MenutypeEntity.class, menutype.getId());
		message = "删除成功";
		menutypeService.delete(menutype);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加menu_type
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MenutypeEntity menutype, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(menutype.getId())) {
			message = "更新成功";
			MenutypeEntity t = menutypeService.get(MenutypeEntity.class, menutype.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(menutype, t);
				menutypeService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			menutypeService.save(menutype);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * menu_type列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(MenutypeEntity menutype, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(menutype.getId())) {
			menutype = menutypeService.getEntity(MenutypeEntity.class, menutype.getId());
			req.setAttribute("menutypePage", menutype);
		}
		return new ModelAndView("com/wm/menu/menutype");
	}
}
