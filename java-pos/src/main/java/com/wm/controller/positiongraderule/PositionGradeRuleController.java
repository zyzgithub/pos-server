package com.wm.controller.positiongraderule;
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

import com.wm.entity.positiongraderule.PositionGradeRuleEntity;
import com.wm.service.positiongraderule.PositionGradeRuleServiceI;

/**   
 * @Title: Controller
 * @Description: 岗位变更规则表
 * @author wuyong
 * @date 2015-09-01 16:59:57
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/positionGradeRuleController")
public class PositionGradeRuleController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PositionGradeRuleController.class);

	@Autowired
	private PositionGradeRuleServiceI positionGradeRuleService;
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
	 * 岗位变更规则表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "positionGradeRule")
	public ModelAndView positionGradeRule(HttpServletRequest request) {
		return new ModelAndView("com/wm/positiongraderule/positionGradeRuleList");
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
	public void datagrid(PositionGradeRuleEntity positionGradeRule,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PositionGradeRuleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, positionGradeRule);
		this.positionGradeRuleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除岗位变更规则表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(PositionGradeRuleEntity positionGradeRule, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		positionGradeRule = systemService.getEntity(PositionGradeRuleEntity.class, positionGradeRule.getId());
		message = "删除成功";
		positionGradeRuleService.delete(positionGradeRule);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加岗位变更规则表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(PositionGradeRuleEntity positionGradeRule, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(positionGradeRule.getId())) {
			message = "更新成功";
			PositionGradeRuleEntity t = positionGradeRuleService.get(PositionGradeRuleEntity.class, positionGradeRule.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(positionGradeRule, t);
				positionGradeRuleService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			positionGradeRuleService.save(positionGradeRule);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 岗位变更规则表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(PositionGradeRuleEntity positionGradeRule, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(positionGradeRule.getId())) {
			positionGradeRule = positionGradeRuleService.getEntity(PositionGradeRuleEntity.class, positionGradeRule.getId());
			req.setAttribute("positionGradeRulePage", positionGradeRule);
		}
		return new ModelAndView("com/wm/positiongraderule/positionGradeRule");
	}
}
