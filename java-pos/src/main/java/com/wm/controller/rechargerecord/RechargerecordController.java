package com.wm.controller.rechargerecord;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.rechargerecord.RechargerecordServiceI;

/**   
 * @Title: Controller
 * @Description: recharge_record
 * @author wuyong
 * @date 2015-01-07 10:02:46
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/rechargerecordController")
public class RechargerecordController extends BaseController {

	@Autowired
	private RechargerecordServiceI rechargerecordService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private PayServiceI	payService;

	/**
	 * 支付宝充值
	 * @param userid
	 * @param money
	 * @param payType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "recharge")
	@ResponseBody
	public AjaxJson recharge(int userid,double money,String payType,HttpServletRequest request,HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String title = "充值";
		int orderId = orderService.createRechargeOrder(userid,money,title);//创建充值订单并返回订单ID

		if (orderId == 0) {
			j.setMsg("非法支付");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			j = payService.rechargeOrderPay(orderId, userid, money,payType,request,response);
			j.setSuccess(true);
		}
		return j;
	}
	
}
