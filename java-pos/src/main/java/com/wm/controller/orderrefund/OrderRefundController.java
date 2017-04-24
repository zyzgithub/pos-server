package com.wm.controller.orderrefund;
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

import com.wm.entity.orderrefund.OrderRefundEntity;
import com.wm.service.orderrefund.OrderRefundServiceI;

/**   
 * @Title: Controller
 * @Description: 订单退款记录表
 * @author wuyong
 * @date 2015-08-29 15:40:43
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/orderRefundController")
public class OrderRefundController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OrderRefundController.class);

	@Autowired
	private OrderRefundServiceI orderRefundService;
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
	 * 订单退款记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "orderRefund")
	public ModelAndView orderRefund(HttpServletRequest request) {
		return new ModelAndView("com/wm/orderrefund/orderRefundList");
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
	public void datagrid(OrderRefundEntity orderRefund,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrderRefundEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, orderRefund);
		this.orderRefundService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除订单退款记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(OrderRefundEntity orderRefund, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		orderRefund = systemService.getEntity(OrderRefundEntity.class, orderRefund.getId());
		message = "删除成功";
		orderRefundService.delete(orderRefund);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加订单退款记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrderRefundEntity orderRefund, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(orderRefund.getId())) {
			message = "更新成功";
			OrderRefundEntity t = orderRefundService.get(OrderRefundEntity.class, orderRefund.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(orderRefund, t);
				orderRefundService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			orderRefundService.save(orderRefund);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 订单退款记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(OrderRefundEntity orderRefund, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(orderRefund.getId())) {
			orderRefund = orderRefundService.getEntity(OrderRefundEntity.class, orderRefund.getId());
			req.setAttribute("orderRefundPage", orderRefund);
		}
		return new ModelAndView("com/wm/orderrefund/orderRefund");
	}
}
