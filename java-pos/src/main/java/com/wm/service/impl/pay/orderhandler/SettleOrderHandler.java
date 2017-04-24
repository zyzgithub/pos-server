package com.wm.service.impl.pay.orderhandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.OrderStateEnum;
import com.wm.entity.order.CashSettlementRelationEntity;
import com.wm.entity.order.CashierOrderEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.util.AliOcs;

@Service("settleOrderHandler")
public class SettleOrderHandler implements OrderHandler {

	private final static Logger logger = LoggerFactory.getLogger(SettleOrderHandler.class);
	
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private SuperMarketServiceI supermarketService;
	
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	
	
	
	@Override
	public void handle(OrderEntity order) throws Exception {
		Integer orderId = order.getId();
		
		//更新订单状态
		logger.info("handle supermarket settelement order. orderId={}", orderId);
		order.setState(OrderStateEnum.CONFIRM.getOrderStateEn());
		order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
		order.setPayTime(DateUtils.getSeconds());
		order.setCompleteTime(DateUtils.getSeconds());
		order.setOrderNum(AliOcs.genOrderNum(String.valueOf(order.getMerchant().getId())));
		orderService.updateEntitie(order);
		
		CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
		if(cashierOrder == null){
			logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
			throw new Exception("无法根据结算订单ID" + orderId + "找到收银员与订单关联信息");
		}
		
		//查找结算订单对应的现金订单
		String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=?";
		List<CashSettlementRelationEntity> allUnSettledOrders = supermarketService.findHql(hql, cashierOrder.getCashierId());
		List<Integer> cashOrderIds = new ArrayList<Integer>();
		if(CollectionUtils.isNotEmpty(allUnSettledOrders)){
			for(CashSettlementRelationEntity allUnSettledOrder: allUnSettledOrders){
				cashOrderIds.add(allUnSettledOrder.getCashOrderId());
				//更新现金订单的结算订单Id
				allUnSettledOrder.setSettlementOrderId(orderId);
				allUnSettledOrder.setIsSettlemented("1");
				allUnSettledOrder.setUpdateTime(DateUtils.getSeconds());
				supermarketService.saveOrUpdate(allUnSettledOrder);
			}
		}
		logger.info("查找结算订单对应的现金订单, settleorderId:{}, cashOrderIds:{}", orderId, StringUtils.join(cashOrderIds, ","));
		if(CollectionUtils.isNotEmpty(cashOrderIds)){
			for(Integer cashOrderId: cashOrderIds){
				logger.info("订单对应的现金预收入进入余额, settleorderId:{}, cashOrderId:{}", orderId, cashOrderId);
				OrderIncomeEntity orderIncomeEntity = orderIncomeService.getOrderIncomeByOrderIdAndType(cashOrderId, 0);
				try {
					orderIncomeService.directCashierUnOrderIncome(orderIncomeEntity.getId());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("settlement unorderincome:{} failed !!!!", orderIncomeEntity.getId());
				}
			}
		}
		
	}

}
