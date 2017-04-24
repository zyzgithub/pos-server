package com.wm.controller.order;

import org.apache.commons.lang.RandomStringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wm.controller.order.dto.OrderFromSupplyDTO;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.SupplyOrderServiceI;
import com.wm.util.StringUtil;

@Controller
@RequestMapping("ci/supplyOrderController")
public class SupplyOrderController {
	
	private static Logger logger = LoggerFactory.getLogger(SupplyOrderController.class);
	
	@Autowired
	private SupplyOrderServiceI supplyOrderService;
	
	@Autowired
	private OrderServiceI orderService;
	
	@RequestMapping(params = "receiveMerchantOrder")
	@ResponseBody
	public AjaxJson receiveMerchantOrder(OrderFromSupplyDTO orderFromSupplyDTO) {
		long time = System.currentTimeMillis();
		String payId = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderFromSupplyDTO.getSrcId()).substring(2);

		return supplychainMerchantOrder(orderFromSupplyDTO, payId);
	}
	
	@RequestMapping(params = "supplychainMerchantOrder")
	@ResponseBody
	public AjaxJson supplychainMerchantOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) {
		logger.info("supplychainMerchantOrder >>>>>>>>>>>>>>>>>>>>>>>>>>接收来自供应链的商家订单，id is {}, pay id is {}", orderFromSupplyDTO.getOrderId(), payId);
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		try {
			j = supplyOrderService.receiveSupplyChainMerchantOrder(orderFromSupplyDTO, payId);
			orderService.pushOrder(Integer.parseInt(j.getObj().toString()));
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("推送订单失败");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		logger.info("supplychainMerchantOrder >>>>>>>>>>>>>>>>>>>>>>>>>>接收来自供应链的商家订单,return:" + JSON.toJSONString(j));
		return j; 
	}
	
	@RequestMapping(params = "receiveWarehouseOrder")
	@ResponseBody
	public AjaxJson receiveWarehouseOrder(OrderFromSupplyDTO orderFromSupplyDTO) {
		long time = System.currentTimeMillis();
		String payId = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderFromSupplyDTO.getSrcId()).substring(2);
		return supplychainWarehouseOrder(orderFromSupplyDTO, payId);
	}
	
	@RequestMapping(params = "supplychainWarehouseOrder")
	@ResponseBody
	public AjaxJson supplychainWarehouseOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) {
		logger.info("supplychainWarehouseOrder >>>>>>>>>>>>>>>>>>>>>>>>>>接收来自供应链的仓库订单，id is {}, pay id is {}", orderFromSupplyDTO.getOrderId(), payId);
		orderFromSupplyDTO.setOrderDetails(orderFromSupplyDTO.getOrderDetails().replaceAll("num", "quantity"));
		if (StringUtil.isEmpty(orderFromSupplyDTO.getSrcAddress())) {
			orderFromSupplyDTO.setSrcAddress("");
		}
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		try {
			j = supplyOrderService.receiveSupplyChainWarehouseOrder(orderFromSupplyDTO, payId);
			orderService.pushOrder(Integer.parseInt(j.getObj().toString()));
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("推送订单失败");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		logger.info("supplychainWarehouseOrder >>>>>>>>>>>>>>>>>>>>>>>>>>接收来自供应链的仓库订单,return:" + JSON.toJSONString(j));
		return j;
	}
	
	@RequestMapping(params = "deliveryOrder")
	@ResponseBody
	public AjaxJson deliveryOrder(int courierId, int supplychainOrderId) {
		logger.info(">>>>>>>>>>>>>>>>>>>>> 扫码配送 courierId is " + courierId + ", supplyerchainOrderId is " + supplychainOrderId);
		return orderService.supplyOrderDistribution(courierId, supplychainOrderId, true);
	} 
	
	@RequestMapping(params = "changeOrderId")
	@ResponseBody
	public AjaxJson changeOrderId(int courierId, int orderId) {
		int supplyOrderId = orderService.matchSupplyOrderId(orderId);
		AjaxJson ajaxJson = new AjaxJson();
		if (supplyOrderId == 0) {
			ajaxJson.setMsg("没有匹配的供应链订单！");
			ajaxJson.setStateCode("01");
			ajaxJson.setSuccess(false);
		} else {
			ajaxJson.setMsg("订单号正确！");
			ajaxJson.setStateCode("00");
			ajaxJson.setSuccess(true);
			ajaxJson.setObj(supplyOrderId);
		}
		return ajaxJson;
	}
	
	@RequestMapping(params = "notifyOrderFinished")
	@ResponseBody
	public AjaxJson notifyOrderFinished(int courierId, int orderId) {
		logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 供应链确认订单状态{}，{} ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓", courierId, orderId);
		AjaxJson ajaxJson = new AjaxJson();
		try {
			boolean result = orderService.supplyOrderFinish(courierId, orderId);

			if (result) {
				ajaxJson.setMsg("修改订单状态成功");
				ajaxJson.setStateCode("00");
				ajaxJson.setSuccess(true);
				logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 供应链确认订单状态成功{}，{} ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", courierId, orderId);
				return ajaxJson;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ajaxJson.setMsg("修改订单状态失败");
		ajaxJson.setStateCode("01");
		ajaxJson.setSuccess(false);
		logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 供应链确认订单状态失败{}，{} ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", courierId, orderId);
		return ajaxJson;
	}
}