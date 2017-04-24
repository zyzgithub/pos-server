package com.wm.service.impl.pay.orderhandler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.courier_mana.common.Constants;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.deduct.CourierRechargePromotionServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.pay.PayServiceI;
import com.wm.service.recharge.RechargeServiceI;
import com.wm.service.rechargerecord.RechargerecordServiceI;

/**
 * 充值订单回调处理器
 */
@Component("rechargeOrderHandler")
public class RechargeOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RechargeOrderHandler.class);
	
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private PayServiceI payService;
	@Autowired
	private RechargeServiceI rechargeService;
	@Autowired
	private RechargerecordServiceI rechargerecordService;
	@Autowired
	private CourierRechargePromotionServiceI courierRechargePromotionService;

	@Override
	public void handle(OrderEntity order) throws Exception {
		Integer orderId = order.getId();
		logger.info("handle recharge order. orderId={}", orderId);
		WUserEntity user = order.getWuser();
		flowService.rechargeFlowCreate(user.getId(), order.getOnlineMoney(), orderId, order.getMerchant().getId());
		rechargerecordService.recharge(order.getPayId(), user.getId(), order.getOrigin(), order.getPayType());
		//更新0085_recharge表 状态
		rechargeService.updateRechargeState(order.getPayId());
		//更新pay表 状态
		payService.updatePayState(orderId);
		//如果是快递员推广的充值
		if(StringUtils.equals(Constants.COURIER_PROMOTION_RECHARGE, order.getRechargeSrc())){
			Integer userId = order.getInviteId();
			if(userId == null){
				logger.error("充值订单对应的推广快递员ID为空, orderId:{}", orderId);
				return;
			}
			
			//先查看本次应该奖励的金额
			Double money = courierRechargePromotionService.getRechargePromotionDeduct(userId, order.getOrigin(), user.getId());
			if(money > 0){
				flowService.courierRechargePromotion(orderId, userId, money);
			}
			//保存推广充值明细
			courierRechargePromotionService.saveCourierScanPromotion(order);
			
		}
	}

}
