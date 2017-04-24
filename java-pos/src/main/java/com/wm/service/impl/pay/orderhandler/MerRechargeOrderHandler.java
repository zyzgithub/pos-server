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
 * 商家充值订单回调处理器
 */
@Component("merRechargeOrderHandler")
public class MerRechargeOrderHandler implements OrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(MerRechargeOrderHandler.class);

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
        logger.info("handle merchant recharge order. orderId={}", orderId);
        double totalMoney = order.getMerchantMemberDiscountMoney() + order.getOnlineMoney();
        WUserEntity user = order.getWuser();
        flowService.merchantRechargeFlowCreate(user.getId(), totalMoney, orderId);
        rechargerecordService.recharge(order.getPayId(), user.getId(), totalMoney, order.getPayType());
        //更新0085_recharge表 状态
        rechargeService.updateRechargeState(order.getPayId());
        //更新pay表 状态
        payService.updatePayState(orderId);
    }

}
