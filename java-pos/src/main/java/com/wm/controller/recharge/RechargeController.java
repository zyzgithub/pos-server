package com.wm.controller.recharge;
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

import com.wm.entity.recharge.RechargeEntity;
import com.wm.service.recharge.RechargeServiceI;

/**   
 * @Title: Controller
 * @Description: 充值记录表
 * @author wuyong
 * @date 2015-09-01 10:09:01
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/rechargeController")
public class RechargeController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RechargeController.class);

	@Autowired
	private RechargeServiceI rechargeService;
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
	 * 充值记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "recharge")
	public ModelAndView recharge(HttpServletRequest request) {
		return new ModelAndView("com/wm/recharge/rechargeList");
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
	public void datagrid(RechargeEntity recharge,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RechargeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, recharge);
		this.rechargeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除充值记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RechargeEntity recharge, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		recharge = systemService.getEntity(RechargeEntity.class, recharge.getId());
		message = "删除成功";
		rechargeService.delete(recharge);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加充值记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(RechargeEntity recharge, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(recharge.getId())) {
			message = "更新成功";
			RechargeEntity t = rechargeService.get(RechargeEntity.class, recharge.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(recharge, t);
				rechargeService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			rechargeService.save(recharge);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 充值记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(RechargeEntity recharge, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(recharge.getId())) {
			recharge = rechargeService.getEntity(RechargeEntity.class, recharge.getId());
			req.setAttribute("rechargePage", recharge);
		}
		return new ModelAndView("com/wm/recharge/recharge");
	}
}
