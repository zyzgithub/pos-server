package com.wm.controller.credit;
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

import com.wm.entity.credit.CreditEntity;
import com.wm.service.credit.CreditServiceI;

/**   
 * @Title: Controller
 * @Description: credit
 * @author wuyong
 * @date 2015-01-07 09:56:32
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/creditController")
public class CreditController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(CreditController.class);

	@Autowired
	private CreditServiceI creditService;
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
	 * credit列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "credit")
	public ModelAndView credit(HttpServletRequest request) {
		return new ModelAndView("com/wm/credit/creditList");
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
	public void datagrid(CreditEntity credit,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CreditEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, credit);
		this.creditService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除credit
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CreditEntity credit, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		credit = systemService.getEntity(CreditEntity.class, credit.getId());
		message = "删除成功";
		creditService.delete(credit);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加credit
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CreditEntity credit, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(credit.getId())) {
			message = "更新成功";
			CreditEntity t = creditService.get(CreditEntity.class, credit.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(credit, t);
				creditService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			creditService.save(credit);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * credit列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CreditEntity credit, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(credit.getId())) {
			credit = creditService.getEntity(CreditEntity.class, credit.getId());
			req.setAttribute("creditPage", credit);
		}
		return new ModelAndView("com/wm/credit/credit");
	}
}
