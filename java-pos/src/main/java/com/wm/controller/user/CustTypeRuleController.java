package com.wm.controller.user;
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

import com.wm.entity.user.CustTypeRuleEntity;
import com.wm.service.user.CustTypeRuleServiceI;

/**   
 * @Title: Controller
 * @Description: 客户类型规则定义表
 * @author wuyong
 * @date 2015-08-27 22:18:31
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/custTypeRuleController")
public class CustTypeRuleController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CustTypeRuleController.class);

	@Autowired
	private CustTypeRuleServiceI custTypeRuleService;
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
	 * 客户类型规则定义表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "custTypeRule")
	public ModelAndView custTypeRule(HttpServletRequest request) {
		return new ModelAndView("com/wm/user/custTypeRuleList");
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
	public void datagrid(CustTypeRuleEntity custTypeRule,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CustTypeRuleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, custTypeRule);
		this.custTypeRuleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除客户类型规则定义表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CustTypeRuleEntity custTypeRule, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		custTypeRule = systemService.getEntity(CustTypeRuleEntity.class, custTypeRule.getId());
		message = "删除成功";
		custTypeRuleService.delete(custTypeRule);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加客户类型规则定义表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CustTypeRuleEntity custTypeRule, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(custTypeRule.getId())) {
			message = "更新成功";
			CustTypeRuleEntity t = custTypeRuleService.get(CustTypeRuleEntity.class, custTypeRule.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(custTypeRule, t);
				custTypeRuleService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			custTypeRuleService.save(custTypeRule);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 客户类型规则定义表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CustTypeRuleEntity custTypeRule, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(custTypeRule.getId())) {
			custTypeRule = custTypeRuleService.getEntity(CustTypeRuleEntity.class, custTypeRule.getId());
			req.setAttribute("custTypeRulePage", custTypeRule);
		}
		return new ModelAndView("com/wm/user/custTypeRule");
	}
}
