package com.wm.controller.message;
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

import com.wm.entity.message.WmessageEntity;
import com.wm.service.message.WmessageServiceI;

/**   
 * @Title: Controller
 * @Description: message
 * @author wuyong
 * @date 2015-01-07 10:08:22
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/wmessageController")
public class WmessageController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(WmessageController.class);

	@Autowired
	private WmessageServiceI wmessageService;
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
	 * message列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "wmessage")
	public ModelAndView wmessage(HttpServletRequest request) {
		return new ModelAndView("com/wm/message/wmessageList");
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
	public void datagrid(WmessageEntity wmessage,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmessageEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmessage);
		this.wmessageService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除message
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(WmessageEntity wmessage, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wmessage = systemService.getEntity(WmessageEntity.class, wmessage.getId());
		message = "删除成功";
		wmessageService.delete(wmessage);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加message
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WmessageEntity wmessage, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(wmessage.getId())) {
			message = "更新成功";
			WmessageEntity t = wmessageService.get(WmessageEntity.class, wmessage.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(wmessage, t);
				wmessageService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			wmessageService.save(wmessage);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * message列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(WmessageEntity wmessage, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmessage.getId())) {
			wmessage = wmessageService.getEntity(WmessageEntity.class, wmessage.getId());
			req.setAttribute("wmessagePage", wmessage);
		}
		return new ModelAndView("com/wm/message/wmessage");
	}
}
