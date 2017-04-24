package com.wm.controller.order;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.PrintKLLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.controller.order.dto.KelailePayResultDTO;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 打印相关
 * @author Simon
 */
@Controller
@RequestMapping("openapi/printController")
public class PrintController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(PrintController.class);
	
	@Autowired
	private PayServiceI payService;
	@Autowired
	private WUserServiceI userService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	
	
	/**
	 * 客来乐远程下单打印支付回调
	 * @param KelailePayResultDTO 客来乐回调参数集
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/kelaile/notify", method=RequestMethod.GET)
	@ResponseBody
	public String orderPrintNotify(HttpServletResponse response, KelailePayResultDTO payResult) throws Exception {
		logger.info("========    客来乐远程下单打印支付回调开始	=========");
		String result = "fail";
		logger.info("KelailePayResultDTO:" + payResult);
		String appId = payResult.getApp_id();
		if(PrintKLLUtils.appid.equals(appId)){
			Integer tradeState = payResult.getTrade_state();
			String payId = payResult.getOut_trade_no();
			if(null != tradeState && tradeState.equals(4)){
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if(order != null && OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getPayState())){
					Integer payType = payResult.getPay_type();
					order.setPayType(payType.equals(1) ? PayEnum.weixinpay.getEn() : payType.equals(8) ? PayEnum.alipay.getEn() : "");
					String openId = payResult.getPay_user_id();
					if(payType.equals(1)){
						logger.info("微信支付，openId=" + openId);
						WUserEntity user = userService.findByOpenId(openId);
						if(null != user){
							order.setWuser(user);
						} else {
							logger.warn("不存在该微信用户!openId=" + openId);
						}
					} else {
						logger.info("支付宝支付，openId=" + openId);
					}
					order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
					Float totalPrice = payResult.getTotal_price();
					order.setOnlineMoney(totalPrice.doubleValue());
					order.setPayTime(payResult.getPay_time());
					order.setOutTraceId(payResult.getTransaction_id());
					// 置订单已完成
					order.setState("confirm");
					order.setCompleteTime(DateUtils.getSeconds());
					orderService.saveOrUpdate(order);
					orderStateService.payOrderState(order);
					menuService.buyCount(order.getId());// 销量统计
					orderIncomeService.createOrderIncome(order);
					result = "success";
				} else {
					logger.error("订单不存在，或者订单已支付！" + payId);
				}
			} else {
				logger.error("超市订单支付失败！payId：" + payId);
			}
		} else {
			logger.error("调用来源非法！appId：" + appId);
		}
		logger.info("========    客来乐远程下单打印支付回调结束	=========");
		return result;
	}
	
}
