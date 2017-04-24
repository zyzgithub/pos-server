package com.wm.service.impl.pay.orderhandler;

import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.enums.PayEnum;
import com.courier_mana.common.Constants;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.DineInDiscountLogServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.TomOrderTimerServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;

/**
 * 普通订单回调处理器
 */
@Component("normalOrderHandler")
public class NormalOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(NormalOrderHandler.class);

	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private  MenuServiceI menuService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private WmessageServiceI messageService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
    private OrderIncomeServiceI orderIncomeService;
	@Autowired
	private TomOrderTimerServiceI tomOrderTimerService;
	@Autowired
	private DineInDiscountLogServiceI dineInDiscountLogService;

	@Override
	public void handle(OrderEntity order) throws Exception {
		long startTime = System.currentTimeMillis();
		Integer orderId = order.getId();
		logger.info("handle normal order. orderId={}", orderId);
		WUserEntity user = order.getWuser();
		if(PayEnum.balance.getEn().equals(order.getPayType())){
			flowService.balancePayFlowCreate(orderId, user, order.getCredit());
		}
		orderStateService.payOrderState(order);
		
		MerchantEntity merchant = order.getMerchant();
		String source = merchantService.getMerchantSource(merchant.getId());
		if(Constants.MERCHANT_SOURCE_PRIVATE.equals(source)){
			orderStateService.accessOrderStateChoice(orderId);
		}
		//缓存给商家端扫描
		tomOrderTimerService.createOrUpdate(orderId, merchant.getId(), order.getCreateTime());
		
		messageService.sendMessage(order, "paySuccess");
		orderService.autoPrint(order);
		
		Integer saleType = order.getSaleType();
		if(OrderEntity.SaleType.TAKEOUT.equals(saleType)){
			logger.info("外卖订单接单[订单id:{},商家id:{},商家来源:{}]", orderId, merchant.getId(), source);
			if (Constants.MERCHANT_SOURCE_PRIVATE.equals(source) || orderService.isMerchantDelivery(merchant)) {
				merchantService.pushOrder(orderId);
			} else {
				order.setIsMerchantDelivery("courier");
                order.setState("accept");
                order.setAccessTime(DateUtils.getSeconds());
                orderService.updateEntitie(order);
                orderStateService.accessOrderState(orderId);
                messageService.sendMessage(order, "accept");
                orderService.pushOrder(orderId);
			}
		} else if (OrderEntity.SaleType.DINE.equals(saleType)) {
			logger.info("堂食订单接单[订单id:{},商家id:{},商家来源:{}]", orderId, merchant.getId(), source);
			order.setAccessTime(DateUtils.getSeconds());
			order.setState("accept");
        	orderService.updateEntitie(order);
        	orderStateService.accessOrderState(orderId);
        	messageService.sendMessage(order, "accept");
        	
        	if(order.getDineInDiscountMoney() > 0.00){
        		//堂食折扣插入记录表如果有打折的话
        		dineInDiscountLogService.createLog(orderId, order.getOnlineMoney());
        	}

        	if (!Constants.MERCHANT_SOURCE_PRIVATE.equals(source)) {
                order.setState("confirm");
                order.setCompleteTime(DateUtils.getSeconds());
                orderService.updateEntitie(order);
                orderIncomeService.createOrderIncome(order);
            }
            
			merchantService.pushOrder(orderId);
		} else {
			logger.error("unknown saletype orderId:{}", orderId);
		}
		
		menuService.buyCount(orderId);
		
		long costTime = System.currentTimeMillis() - startTime;
		logger.info("orderId:{} normal order handle costtime:{} ms", orderId, costTime);
	}

}
