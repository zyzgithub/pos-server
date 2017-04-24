package com.wm.service.impl.pay.orderhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.pay.PayServiceI;
import com.wm.service.recharge.RechargeServiceI;
import com.wm.service.rechargerecord.RechargerecordServiceI;

/**
 * 代理商充值订单回调处理器
 */
@Component("agentRechargeOrderHandler")
public class AgentRechargeOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentRechargeOrderHandler.class);
	
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private PayServiceI payService;
	@Autowired
	private RechargeServiceI rechargeService;
	@Autowired
	private RechargerecordServiceI rechargerecordService;

	@Override
	public void handle(OrderEntity order) throws Exception {
		Integer orderId = order.getId();
		logger.info("handle agent recharge order. orderId={}", orderId);
		WUserEntity user = order.getWuser();
		flowService.agentRechargeFlowCreate(user.getId(), order.getOnlineMoney(), orderId);
		rechargerecordService.recharge(order.getPayId(), user.getId(), order.getOrigin(), order.getPayType());
		//更新0085_recharge表 状态
		rechargeService.updateRechargeState(order.getPayId());
		//更新pay表 状态
		payService.updatePayState(orderId);
	}

}
