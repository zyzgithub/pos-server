package com.wm.service.impl.pay.orderhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.transfers.TransfersServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 电话订单回调处理器
 */
@Component("mobileOrderHandler")
public class MobileOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(MobileOrderHandler.class);

	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private TransfersServiceI transfersService;
	@Autowired
	private OrderStateServiceI orderStateService;
	
	@Override
	public void handle(OrderEntity order) throws Exception {
		Integer orderId = order.getId();
		logger.info("handle mobile order. orderId={}", orderId);
		WUserEntity user = order.getWuser();
		if("db".equalsIgnoreCase(order.getFromType())){
			// 快递员代付订单
			Integer courierId = order.getCourierId();
			WUserEntity courier = wUserService.get(WUserEntity.class, courierId);
			logger.info("用支付宝或微信支付的电话订单,订单id:{}，开始确认配送完成...", orderId);
			orderService.deliveryDone(courierId, orderId);
			logger.info("用支付宝或微信支付的电话订单,订单id:{}，确认配送完成结束。", orderId);
			transfersService.saveTransfers(courier, user, order.getOrigin(), order.getOrderNum());
			orderStateService.deliveryPayOrderState(courier.getUsername(),	user.getNickname(), orderId, order.getOrigin());
		}
		orderStateService.payOrderState(orderId);
	}

}
