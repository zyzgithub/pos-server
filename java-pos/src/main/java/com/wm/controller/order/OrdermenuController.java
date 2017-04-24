package com.wm.controller.order;
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

import com.wm.entity.order.OrdermenuEntity;
import com.wm.service.order.OrdermenuServiceI;

/**   
 * @Title: Controller
 * @Description: order_menu
 * @author wuyong
 * @date 2015-01-07 10:01:26
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/ordermenuController")
public class OrdermenuController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(OrdermenuController.class);

	@Autowired
	private OrdermenuServiceI ordermenuService;
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
	 * order_menu列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "ordermenu")
	public ModelAndView ordermenu(HttpServletRequest request) {
		return new ModelAndView("com/wm/order/ordermenuList");
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
	public void datagrid(OrdermenuEntity ordermenu,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrdermenuEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, ordermenu);
		this.ordermenuService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除order_menu
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(OrdermenuEntity ordermenu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		ordermenu = systemService.getEntity(OrdermenuEntity.class, ordermenu.getId());
		message = "删除成功";
		ordermenuService.delete(ordermenu);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加order_menu
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrdermenuEntity ordermenu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(ordermenu.getId())) {
			message = "更新成功";
			OrdermenuEntity t = ordermenuService.get(OrdermenuEntity.class, ordermenu.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(ordermenu, t);
				ordermenuService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			ordermenuService.save(ordermenu);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * order_menu列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(OrdermenuEntity ordermenu, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(ordermenu.getId())) {
			ordermenu = ordermenuService.getEntity(OrdermenuEntity.class, ordermenu.getId());
			req.setAttribute("ordermenuPage", ordermenu);
		}
		return new ModelAndView("com/wm/order/ordermenu");
	}
}
