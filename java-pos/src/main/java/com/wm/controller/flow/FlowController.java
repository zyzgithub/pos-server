package com.wm.controller.flow;
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

import com.wm.entity.flow.FlowEntity;
import com.wm.service.flow.FlowServiceI;

/**   
 * @Title: Controller
 * @Description: flow
 * @author wuyong
 * @date 2015-01-07 09:57:27
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/flowController")
public class FlowController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(FlowController.class);

	@Autowired
	private FlowServiceI flowService;
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
	 * flow列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "flow")
	public ModelAndView flow(HttpServletRequest request) {
		return new ModelAndView("com/wm/flow/flowList");
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
	public void datagrid(FlowEntity flow,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FlowEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, flow);
		this.flowService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除flow
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(FlowEntity flow, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		flow = systemService.getEntity(FlowEntity.class, flow.getId());
		message = "删除成功";
		flowService.delete(flow);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加flow
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(FlowEntity flow, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(flow.getId())) {
			message = "更新成功";
			FlowEntity t = flowService.get(FlowEntity.class, flow.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(flow, t);
				flowService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			flowService.save(flow);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * flow列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(FlowEntity flow, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(flow.getId())) {
			flow = flowService.getEntity(FlowEntity.class, flow.getId());
			req.setAttribute("flowPage", flow);
		}
		return new ModelAndView("com/wm/flow/flow");
	}
}
