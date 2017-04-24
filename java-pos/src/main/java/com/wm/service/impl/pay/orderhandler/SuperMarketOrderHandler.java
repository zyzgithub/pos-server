package com.wm.service.impl.pay.orderhandler;

import com.wm.entity.merchant.MerchantEntity;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.OrderStateEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.util.AliOcs;

@Service("superMarketOrderHandler")
public class SuperMarketOrderHandler implements OrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketOrderHandler.class);

    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private OrderStateServiceI orderStateService;


    @Override
    public void handle(OrderEntity order) throws Exception {
        Integer orderId = order.getId();
        logger.info("handle supermarket. orderId={}", orderId);
        order.setState(OrderStateEnum.PAY.getOrderStateEn());
        order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
        order.setPayTime(DateUtils.getSeconds());
        order.setCompleteTime(DateUtils.getSeconds());
        order.setOrderNum(AliOcs.genOrderNum(String.valueOf(order.getMerchant().getId())));
        orderService.updateEntitie(order);

        orderStateService.payOrderState(order);
        MerchantEntity merchant = order.getMerchant();
        String printCode = merchant.getPrintCode();
        if (!"sunmi".equalsIgnoreCase(printCode)) {
            orderService.autoPrint(order);
        }
        orderService.merchantAcceptOrder(order);
        if (!OrderEntity.OrderType.SUPERMARKET_CODEFREE.equals(order.getOrderType())) {
            menuService.buyCount(orderId);
        }
//		merchantService.pushOrder(orderId);
    }

}
