package com.wm.controller.orderincome;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.dto.merchant.MerchantSupplySaleOrderDto;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.service.orderincome.OrderIncomeServiceI;

/**   
 * @Title: Controller
 * @Description: 订单预售如
 * @author wuyong
 * @date 2015-02-12 16:42:53
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/orderIncomeController")
public class OrderIncomeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(OrderIncomeController.class);

	@Autowired
	private OrderIncomeServiceI orderIncomeService;
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
	 * 订单预售如列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "orderIncome")
	public ModelAndView orderIncome(HttpServletRequest request) {
		return new ModelAndView("com/wm/orderincome/orderIncomeList");
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
	public void datagrid(OrderIncomeEntity orderIncome,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrderIncomeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, orderIncome);
		this.orderIncomeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除订单预售如
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(OrderIncomeEntity orderIncome, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		orderIncome = systemService.getEntity(OrderIncomeEntity.class, orderIncome.getId());
		message = "删除成功";
		orderIncomeService.delete(orderIncome);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加订单预售如
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrderIncomeEntity orderIncome, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(orderIncome.getId())) {
			message = "更新成功";
			OrderIncomeEntity t = orderIncomeService.get(OrderIncomeEntity.class, orderIncome.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(orderIncome, t);
				orderIncomeService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			orderIncomeService.save(orderIncome);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 订单预售如列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(OrderIncomeEntity orderIncome, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(orderIncome.getId())) {
			orderIncome = orderIncomeService.getEntity(OrderIncomeEntity.class, orderIncome.getId());
			req.setAttribute("orderIncomePage", orderIncome);
		}
		return new ModelAndView("com/wm/orderincome/orderIncome");
	}
	
	/**
	 * 创建订单预收入
	 * @param orderId
	 * @return
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(params = "createOrderIncome")
	@ResponseBody
	public AjaxJson createOrderIncome(Integer orderId) throws Exception{
		logger.info("1.8调用1.5,创建订单预收入,orderId:{}", orderId);
		AjaxJson j = new AjaxJson();
		if(orderId!=null){
			OrderEntity order = orderIncomeService.get(OrderEntity.class, orderId);
			if(order!=null){
				orderIncomeService.createOrderIncome(order);
				j.setMsg("创建预收入成功");
				j.setStateCode("00");
				j.setSuccess(true);
			}else{
				logger.error("orderId:{} not found !!!!", orderId);
				j.setMsg("未找到订单orderId["+orderId+"]相关的信息");
				j.setStateCode("01");
				j.setSuccess(false);
			}
		}else{
			j.setMsg("订单orderId不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 商家结算
	 * @param orderIncomeId orderIncome预收入id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "unOrderIncome")
	@ResponseBody
	public AjaxJson unOrderIncome(Integer orderIncomeId) throws Exception{
		AjaxJson j = new AjaxJson();
		if(orderIncomeId!=null){
			orderIncomeService.unOrderIncome(orderIncomeId);;
			j.setMsg("结算成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}else{
			j.setMsg("订单预收入orderIncomeId不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 创建订单预收入
	 * @param orderId
	 * @return
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(params = "createSupplyOrderIncome")
	@ResponseBody
	public AjaxJson createSupplyOrderIncome(MerchantSupplySaleOrderDto saleOrderdto){
		AjaxJson j = new AjaxJson(true, "创建预收入成功", "0");
		if(saleOrderdto!=null && saleOrderdto.getOrderId()!=null){
			logger.info("供应链项目调用1.5,创建供应链订单预收入,orderId:{}", saleOrderdto.getOrderId());
			
			try {
				orderIncomeService.createSupplyOrderIncome(saleOrderdto);
				
			} catch (RuntimeException e) {
				j.setMsg("创建预收入失败，失败原因为："+e.getMessage());
				j.setStateCode("01");
				j.setSuccess(false);
			}  catch (Exception e) {
				j.setMsg("创建预收入失败，失败原因为："+e.getMessage());
				j.setStateCode("01");
				j.setSuccess(false);
			}
		}else{
			j.setMsg("订单信息为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 商家结算
	 * @param orderIncomeId orderIncome预收入id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "unSupplyOrderIncome")
	@ResponseBody
	public AjaxJson unSupplyOrderIncome(Integer orderIncomeId) throws Exception{
		AjaxJson j = new AjaxJson();
		if(orderIncomeId!=null){
			orderIncomeService.unOrderIncome(orderIncomeId);;
			j.setMsg("结算成功");
			j.setStateCode("00");
			j.setSuccess(true);
		}else{
			j.setMsg("订单预收入orderIncomeId不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
}
