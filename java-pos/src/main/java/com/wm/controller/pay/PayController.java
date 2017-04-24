package com.wm.controller.pay;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.order.OrderEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.PayServiceI;

/**   
 * @Title: Controller
 * @Description: pay
 * @author wuyong
 * @date 2015-01-07 10:01:54
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/payController")
public class PayController extends BaseController {
	

	@Autowired
	private PayServiceI payService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private MenuServiceI menuService;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@RequestMapping(params = "orderPay")
	@ResponseBody
	public AjaxJson orderPay(HttpServletRequest request,HttpServletResponse response,int userid,int orderid,String mobile,
			String cardid,int cardMoney,int score,double balance,double alipayBalance,String payType,String timeRemark) throws Exception {
		
		AjaxJson j = new AjaxJson();
		
		OrderEntity order = orderService.get(OrderEntity.class, orderid);
		if (order != null && OrderEntity.OrderType.NORMAL.equals(order.getOrderType())) {
			if (!orderService.verifyMenuRepertoryquantity(orderid)) {// 验证菜单库存是否充足
				j.setMsg("菜单库存不足");
				j.setStateCode("01");
				j.setSuccess(false);
				return j;
			}

			j = menuService.verificationPayMenuPromotion(orderid);// 验证促销菜品信息
			if (!j.isSuccess()) {
				return j;
			}
		}
		j = payService.orderPay(orderid,userid, mobile, cardid, cardMoney, score, balance, alipayBalance,payType,timeRemark,request,response);
		return j;
	}
	
}
