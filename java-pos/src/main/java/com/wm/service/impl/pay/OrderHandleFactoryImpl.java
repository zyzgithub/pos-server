package com.wm.service.impl.pay;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.base.constant.order.OrderType;
import com.wm.entity.order.OrderEntity;
import com.wm.service.pay.OrderHandleFactory;
import com.wm.service.pay.OrderHandler;

@Component("orderHandleService")
public class OrderHandleFactoryImpl implements OrderHandleFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderHandleFactoryImpl.class);
	
	@Resource
	private OrderHandler defaultOrderHandler;
	@Resource
	private OrderHandler normalOrderHandler;
	@Resource
	private OrderHandler directOrderHandler;
	@Resource
	private OrderHandler eatinOrderHandler;
	@Resource
	private OrderHandler mobileOrderHandler;
	@Resource
	private OrderHandler scanOrderHandler;
	@Resource
	private OrderHandler thirdOrderHandler;
	@Resource
	private OrderHandler rechargeOrderHandler;
	@Resource
	private OrderHandler merRechargeOrderHandler;
	@Resource
	private OrderHandler agentRechargeOrderHandler;
	@Resource
	private OrderHandler superMarketOrderHandler;
	@Resource 
	private OrderHandler settleOrderHandler;

	public OrderHandler getHandler(String orderType) {
		logger.info("handle orderType={}", orderType);
		if(OrderEntity.OrderType.NORMAL.equals(orderType)){
			return normalOrderHandler;
		} else if(OrderEntity.OrderType.SCAN_ORDER.equals(orderType)){
			return scanOrderHandler;
		} else if(OrderEntity.OrderType.EAT_IN_ORDER.equals(orderType)){
			return eatinOrderHandler;
		} else if (OrderEntity.OrderType.RECHARGE.equals(orderType)) {
			return rechargeOrderHandler;
		} else if (OrderEntity.OrderType.MERCHANT_RECHARGE.equals(orderType)) {
			return merRechargeOrderHandler;
		} else if (OrderEntity.OrderType.AGENT_RECHARGE.equals(orderType)) {
			return agentRechargeOrderHandler;
		} else if (OrderEntity.OrderType.MOBILE.equals(orderType)) {
			return mobileOrderHandler;
		} else if (OrderEntity.OrderType.DIRECT_PAY.equals(orderType)) {
			return directOrderHandler;
		} else if(OrderEntity.OrderType.THIRD_PART.equals(orderType)){
			return thirdOrderHandler;
		} else if(OrderEntity.OrderType.SUPERMARKET.equals(orderType) || OrderEntity.OrderType.SUPERMARKET_CODEFREE.equals(orderType)){
			return superMarketOrderHandler;
		}
		else if(OrderType.SUPERMARKET_SETTLEMENT.getName().equals(orderType)){
			return settleOrderHandler;
		}
		return defaultOrderHandler;
	}

}
