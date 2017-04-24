package com.wm.controller.dineorder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.dineorder.DineOrderEntity;
import com.wm.service.dineorder.DineOrderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;

/**   
 * @Title: Controller
 * @Description: 堂食订单
 * @author wuyong
 * @date 2015-04-01 16:11:12
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/dineOrderController")
public class DineOrderController extends BaseController {

	@Autowired
	private DineOrderServiceI dineOrderService;
	@Autowired
	private OrderServiceI orderService;	
	@Autowired
	private SystemService systemService;
	@Autowired
	private PrintServiceI printService;
	
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 创建堂食订单
	 * @param merchantId
	 * @param params
	 * @param timeRemark
	 * @param totalOrigin
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "createDineOrder")
	@ResponseBody
	public AjaxJson createDineOrder(int merchantId,  String params,String timeRemark,double totalOrigin,
			 HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		j = orderService.verifyMenuQuantity(params,0);//验证菜品库存是否还有
		if(j.isSuccess()){//判断菜品库存是否还有
			int orderId = dineOrderService.createDineOrder(merchantId,params,timeRemark,totalOrigin);
			
			DineOrderEntity order = dineOrderService.get(DineOrderEntity.class, orderId);
			
			if (orderId == 0) {
				j.setMsg("订单金额非法");
				j.setStateCode("01");
				j.setSuccess(false);
			} else {
				if(!printService.printDineOrder(order)){
					j.setMsg("打印订单失败");
					j.setStateCode("01");
					j.setSuccess(false);
					return j;
				}else{
					j.setMsg("打印订单成功");
					j.setStateCode("00");
				}
				j.setObj(orderId);
				j.setSuccess(true);
			}
	
		}

		return j;
	}
	/**
	 * 根据传入状态修改堂食订单状态
	 * @param orderId
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dineOrderDone")
	@ResponseBody
	public AjaxJson dineOrderDone(int orderId,int dineType,
			 HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		DineOrderEntity order = dineOrderService.get(DineOrderEntity.class, orderId);
		
		if(dineType<1 || dineType>3){
			j.setStateCode("01");
			j.setMsg("订单状态错误。");
			j.setSuccess(false);
			return j;
		}
		
		if(order==null){
			j.setStateCode("01");
			j.setMsg("订单不存在。");
			j.setSuccess(false);
			return j;
		}
		
		if(dineType==1){//下单成功
			order.setSuccessTime(DateUtils.getSeconds());
		}else if(dineType==2){//制作成功
			order.setAcceptTime(DateUtils.getSeconds());
		}else if(dineType==3){//配送完成
			order.setCompleteTime(DateUtils.getSeconds());
		}
		order.setDineType(dineType);
		systemService.saveOrUpdate(order);
		j.setStateCode("00");
		j.setMsg("操作成功");
		j.setSuccess(true);
		
		return j;
	}
	
	/**
	 * 根据订单状态查询堂食订单，1.下单成功 2.制作成功3.配送成功 其它为查询全部状态
	 * @param dineType
	 * @return
	 */
	@RequestMapping(params="getDineOrderByDineType")
	@ResponseBody
	public AjaxJson getDineOrderByDineType(HttpServletRequest request,int merchantId){
		AjaxJson j=new AjaxJson();
		int pageNo=1;
		int row=100;
		String dineTypeStr = request.getParameter("dineType");
		int dineType = 0;
		if(!StringUtils.isEmpty(dineTypeStr)){
			dineType = Integer.parseInt(dineTypeStr);
		}
		if(request.getParameter("pageNo")!=null &&!"".equals(request.getParameter("pageNo").trim()) ){
			pageNo=Integer.parseInt(request.getParameter("pageNo"));
		}
		if(request.getParameter("row")!=null &&!"".equals(request.getParameter("row").trim()) ){
			row=Integer.parseInt(request.getParameter("row"));
		}
		if(row >= 100){
			row = 100;
		}
		
		List<Map<String, Object>> list=dineOrderService.getDineOrderByDineType(dineType,merchantId,pageNo,row);
		j.setObj(list);
		j.setMsg("操作成功");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
	@RequestMapping(params="printDineOrder")
	@ResponseBody
	public AjaxJson printDineOrder(int did){
		AjaxJson j=new AjaxJson();
		DineOrderEntity dineOrder=systemService.get(DineOrderEntity.class, did);
		if(dineOrder==null){
			j.setMsg("订单不存在");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
		if(!printService.printDineOrder(dineOrder)){
			j.setMsg("打印订单失败");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
		j.setMsg("打印订单成功");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
}
