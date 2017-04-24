package com.wm.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wm.controller.order.dto.OrderFromMerchantDTO;

@Controller
@RequestMapping("/call/order")
public class OrderFromCallController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(OrderFromCallController.class);
	
	@Autowired
	private OrderController orderController;
	
	@RequestMapping(value="/create.do", method=RequestMethod.POST)
	@ResponseBody
	private AjaxJson createOrder(@Valid OrderFromMerchantDTO orderDTO, BindingResult result, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		AjaxJson j = new AjaxJson();
		try {
			if(!result.hasErrors()) {
				logger.info("来源："+orderDTO.getFromType()+"----支付状态："+orderDTO.getState());
				j = orderController.createOrderFromMobile(orderDTO, request);
			} else {
				j.setObj(result.getFieldErrors());
				j.setMsg("提交参数有误");
				j.setStateCode("01");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【createOrder】process use time:{}--param:{}", System.currentTimeMillis() - start, JSON.toJSONString(orderDTO));
		return j;
	}
}
