package com.wm.controller.merchant;
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

import com.wm.entity.merchant.MerchantvisitEntity;
import com.wm.service.merchant.MerchantvisitServiceI;

/**   
 * @Title: Controller
 * @Description: merchant_visit
 * @author wuyong
 * @date 2015-01-07 10:00:15
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/merchantvisitController")
public class MerchantvisitController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(MerchantvisitController.class);

	@Autowired
	private MerchantvisitServiceI merchantvisitService;
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
	 * merchant_visit列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "merchantvisit")
	public ModelAndView merchantvisit(HttpServletRequest request) {
		return new ModelAndView("com/wm/merchant/merchantvisitList");
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
	public void datagrid(MerchantvisitEntity merchantvisit,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MerchantvisitEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, merchantvisit);
		this.merchantvisitService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除merchant_visit
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MerchantvisitEntity merchantvisit, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		merchantvisit = systemService.getEntity(MerchantvisitEntity.class, merchantvisit.getId());
		message = "删除成功";
		merchantvisitService.delete(merchantvisit);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加merchant_visit
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MerchantvisitEntity merchantvisit, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(merchantvisit.getId())) {
			message = "更新成功";
			MerchantvisitEntity t = merchantvisitService.get(MerchantvisitEntity.class, merchantvisit.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(merchantvisit, t);
				merchantvisitService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			merchantvisitService.save(merchantvisit);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * merchant_visit列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(MerchantvisitEntity merchantvisit, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(merchantvisit.getId())) {
			merchantvisit = merchantvisitService.getEntity(MerchantvisitEntity.class, merchantvisit.getId());
			req.setAttribute("merchantvisitPage", merchantvisit);
		}
		return new ModelAndView("com/wm/merchant/merchantvisit");
	}
}
