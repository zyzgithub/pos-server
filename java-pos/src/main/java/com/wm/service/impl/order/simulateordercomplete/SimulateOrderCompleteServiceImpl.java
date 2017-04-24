package com.wm.service.impl.order.simulateordercomplete;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.OrderStateEnum;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.credit.CreditEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.CashSettlementRelationEntity;
import com.wm.entity.order.CashierOrderEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntityVo;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.entity.supermarket.CashierEntity;
import com.wm.entity.supermarket.CashierLoginLogEntity;
import com.wm.entity.supermarket.CashierSettlementLogEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.TlmStatisticsServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.impl.pay.OrderHandleFactoryImpl;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.TpmStatisticsRealtimeServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.simulateordercomplete.SimulateOrderCcompleteServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.transfers.TransfersServiceI;
import com.wm.service.user.CashierServiceI;
import com.wm.service.user.TumUserStatisticsServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.PageList;

@Service
@Transactional
public class SimulateOrderCompleteServiceImpl extends CommonServiceImpl
		implements SimulateOrderCcompleteServiceI {

	private static final Logger logger = LoggerFactory
			.getLogger(SimulateOrderCompleteServiceImpl.class);
	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private WUserServiceI wUserService;

	@Autowired
	private TransfersServiceI transfersService;

	@Autowired
	private OrderStateServiceI orderStateService;

	@Autowired
	private FlowServiceI flowService;

	@Autowired
	private OrderIncomeServiceI orderIncomeService;

	@Autowired
	private TlmStatisticsServiceI tlmStatisticsService;

	@Autowired
	private TpmStatisticsRealtimeServiceI tpmStatisticsRealtimeService;

	@Autowired
	private MerchantServiceI merchantService;

	@Autowired
	private OrderHandleFactoryImpl orderHandleService;

	@Autowired
	private TumUserStatisticsServiceI tumUserStatisticsService;
	
	@Autowired 
	private CashierServiceI cashierService;
	@Autowired 
	private SuperMarketServiceI supermarketService;
	
	

	@Override
	public void orderAlipayDone(int orderId) throws Exception {

		OrderEntity order = this.get(OrderEntity.class, orderId);
		WUserEntity user = order.getWuser();

		// 使用积分Log
		if (order.getScore() != 0) {
			CreditEntity credit = new CreditEntity();
			credit.setWuser(user);
			credit.setDetail("[订单支付]-订单支付");
			credit.setScore(order.getScore());
			credit.setAction("buy");
			credit.setDetailId(orderId);
			this.save(credit);
		}

		// 判断是否第一次下单
		OrderEntityVo orderEntityVo = new OrderEntityVo();
		orderEntityVo.setUserId(user.getId());
		PageList<OrderEntity> list = orderService.findOrderList(orderEntityVo,
				1, 5);
		if (!list.getResultList().isEmpty() && list.getResultList().size() == 1) {
			user.setFirstOrderTime(DateUtils.getSeconds());// 新用户第一次下单时间
			wUserService.updateEntitie(user);
		}

		Double onlineMoney = order.getOnlineMoney() * 100; // 单位分
		// tum_user_statistics表 用户累计消费记录
		if (onlineMoney > 0) {
			MerchantEntity merchant = order.getMerchant();
			String orderType = order.getOrderType();
			logger.info("handle normal order. orderId={}", orderId);
			if ("db".equalsIgnoreCase(order.getFromType())
					&& "mobile".equals(orderType)) {
				// 快递员代付订单
				Integer courierId = order.getCourierId();
				WUserEntity courier = wUserService.get(WUserEntity.class,
						courierId);
				logger.info("用支付宝或微信支付的电话订单,订单id:{}，开始确认配送完成...", orderId);
				this.deliveryDone(courierId, orderId);
				logger.info("用支付宝或微信支付的电话订单,订单id:{}，确认配送完成结束。", orderId);
				transfersService.saveTransfers(courier, user,
						order.getOrigin(), order.getOrderNum());
				orderStateService.deliveryPayOrderState(courier.getUsername(),
						user.getNickname(), orderId, order.getOrigin());
			}
			orderStateService.payOrderState(orderId);

			// 用户消费统计,消费金额=总金额+配送费-积分抵扣金额-优惠券金额
			Double consumeMoney = Double.parseDouble(orderService
					.getOrderRealMoney(order)) * 100;
			logger.info("consumeMoney:{}", consumeMoney);
			// 用户、商家统计
			if (OrderEntity.OrderType.RECHARGE.equals(orderType)
					|| OrderEntity.OrderType.MERCHANT_RECHARGE
							.equals(orderType)
					|| OrderEntity.OrderType.AGENT_RECHARGE.equals(orderType)) {
				tumUserStatisticsService.updateStat(user.getId(), 0,
						consumeMoney.intValue());
			} else {
				tumUserStatisticsService.updateStat(user.getId(),
						consumeMoney.intValue(), 0);
				if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
					// 堂食订单，立即统计；外卖订单，要等订单完成动作触发
					tpmStatisticsRealtimeService.updateStat(merchant.getId(),
							consumeMoney.intValue(), 20);
				}
			}
		}

	}

	public boolean deliveryDone(Integer courierId, Integer orderId)
			throws Exception {
		OrderEntity order = this.get(OrderEntity.class, orderId);

		if (order == null) {
			return false;
		}

		if (!courierId.equals(order.getCourierId())) {
			return false;
		}

		String state = order.getState();

		// 状态为已经确认收货，直接返回true
		if (OrderStateEnum.CONFIRM.getOrderStateEn().equals(state)) {
			logger.info("orderid:" + orderId + ", 订单状态:" + state);
			return true;
		}

		// 非代付电话订单， 配送中的订单才能确认收货
		if (!orderService.isTransferMobileOrder(order)
				&& !OrderStateEnum.DELIVERY.getOrderStateEn().equals(state)
				|| !OrderStateEnum.PAY.getOrderStateEn().equals(
						order.getPayState())) {
			logger.info("orderid:" + orderId + ", 订单状态:" + state + ", 支付状态:"
					+ order.getPayState());
			return false;
		}

		order.setState("confirm");
		order.setRstate("normal");
		order.setPayState("pay");
		order.setCompleteTime(DateUtils.getSeconds());
		logger.info("把订单:" + orderId + "置为确认完成状态.");
		this.saveOrUpdate(order);

		orderStateService.deliveryDoneOrderState(orderId);
		orderIncomeService.createOrderIncome(order);

		// 插入数据到tlm_statistics_realtime(统计表) 主要更新快递员累计送单量和累计送单总时长
		Long completeSeconds = (order.getCompleteTime() - order
				.getDeliveryTime()) * 1000L;
		Integer minutes = (int) ((completeSeconds % (1000 * 60 * 60)) / (1000 * 60));
		Integer money = ((int) Math.rint(order.getOrigin() * 100)
				+ (int) Math.rint(order.getDeliveryFee() * 100)
				- (int) Math.rint(order.getScoreMoney() * 100) - (int) Math
				.rint(order.getCard() * 100));
		tlmStatisticsService.updateTotalOrder(courierId, minutes, money);
		// 商家统计
		Integer merchantMoney = ((int) Math.rint(order.getOrigin()
				.doubleValue() * 100)
				+ (int) Math.rint(order.getDeliveryFee().doubleValue() * 100)
				- (int) Math.rint(order.getScoreMoney().doubleValue() * 100) - (int) Math
				.rint(order.getCard().doubleValue() * 100));
		Long merchantCompleteSeconds = (order.getCompleteTime() - order
				.getAccessTime()) * 1000L;
		Integer merchantMinutes = (int) ((merchantCompleteSeconds % (1000 * 60 * 60)) / (1000 * 60));
		tpmStatisticsRealtimeService.updateStat(order.getMerchant().getId(),
				merchantMoney, merchantMinutes);
		// tpmStatisticsRealtimeService.createOrUpdateTSR(order.getMerchant().getId(),
		// merchantMoney, merchantMinutes);
		//
		return true;
	}

	@Override
	public void simulateSettlementOrderHandler(OrderEntity order, String outTraceId, String orderIds, String payType) throws Exception{
		logger.info("开始处理---------");
		Integer orderId = order.getId();
		CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
		if(cashierOrder == null){
			logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
			throw new Exception("无法根据结算订单ID" + orderId + "找到收银员与订单关联信息");
		}
		
		CashierVo cashierVo = cashierService.get(cashierOrder.getCashierId());
				
		//更新订单状态
		logger.info("handle supermarket settelement order. orderId={}", orderId);
		order.setPayType(payType);
		order.setOutTraceId(outTraceId);
		order.setState(OrderStateEnum.CONFIRM.getOrderStateEn());
		order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
		order.setPayTime(DateUtils.getSeconds());
		order.setCompleteTime(DateUtils.getSeconds());
		order.setOrderNum(AliOcs.genOrderNum(String.valueOf(order.getMerchant().getId())));
		orderService.save(order);
		
		
		String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=? and cashOrderId = ? ";
		//查找结算订单对应的现金订单
		for(String cashOrderIdStr : orderIds.split(",")){
			Integer cashOrderId = Integer.parseInt(cashOrderIdStr);
			List<CashSettlementRelationEntity> allUnSettledOrders = supermarketService.findHql(hql, cashierOrder.getCashierId(), cashOrderId);
			CashSettlementRelationEntity allUnSettledOrder = allUnSettledOrders.get(0);
			//更新现金订单的结算订单Id
			logger.info("update allUnSettledOrder,cashOrderId:{}", allUnSettledOrder.getCashOrderId());
			allUnSettledOrder.setSettlementOrderId(orderId);
			allUnSettledOrder.setIsSettlemented("1");
			allUnSettledOrder.setUpdateTime(DateUtils.getSeconds());
			supermarketService.saveOrUpdate(allUnSettledOrder);
			
			logger.info("订单对应的现金预收入进入余额, settleorderId:{}, cashOrderId:{}", orderId, cashOrderId);
			try {
				OrderIncomeEntity orderIncomeEntity 
					= orderIncomeService.getOrderIncomeByOrderIdAndType(cashOrderId, 0);
				orderIncomeService.unOrderIncome(orderIncomeEntity.getId());
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("现金预收入进入余额出错");
			}
		}

		//更新结算日志(老版本保存日志)
		logger.info("开始更新计算日志----------");
		String findCashierSettlementLogHql = " from CashierSettlementLogEntity where isPaid='0' and cashierId=? order by create_time desc";
		List<CashierSettlementLogEntity> cashierSettlements = supermarketService.findHql(findCashierSettlementLogHql, cashierOrder.getCashierId());
		CashierSettlementLogEntity cashierSettlement = null;
		if(CollectionUtils.isEmpty(cashierSettlements)){
			cashierSettlement = new CashierSettlementLogEntity();
			cashierSettlement.setCashierId(cashierVo.getId());
			cashierSettlement.setMerchantId(cashierVo.getMerchantId());
			cashierSettlement.setSettlementOrderId(orderId);
			cashierSettlement.setMoney(order.getOrigin());
			cashierSettlement.setCreateTime(DateUtils.getSeconds());
			cashierSettlement.setCash(order.getOrigin());
			cashierSettlement.setPaidTime(DateUtils.getSeconds());
			cashierSettlement.setIsPaid("1");
			cashierService.save(cashierSettlement);
		}
		else{
			cashierSettlement = cashierSettlements.get(0);
			cashierSettlement.setMoney(order.getOrigin());
			cashierSettlement.setPaidTime(DateUtils.getSeconds());
			cashierSettlement.setIsPaid("1");
			cashierSettlement.setSettlementOrderId(orderId);
			cashierService.saveOrUpdate(cashierSettlement);
			//如果存在多条结算订单，取消之前的结算订单，以当前的结算订单为准
			Integer settleMentId = cashierSettlement.getSettlementOrderId();
			if(!orderId.equals(settleMentId)){
				String updateSql = "update `order` o set state='cancel' " 
						+ " where o.id >= ? and o.id < ? and o.order_type = 'supermarket_settlement'"
						+ " and exists (select order_id from 0085_cashier_order where cashier_id=? and order_id=o.id)";
				this.executeSql(updateSql, settleMentId, orderId, cashierVo.getId());
			}
		}
		
		//保存一条推出日志
		logger.info("保存退出记录");
		CashierEntity cashier = cashierService.get(CashierEntity.class, cashierVo.getId());
		cashierService.saveCashierLoginLog(cashier, CashierLoginLogEntity.EXIT, null);
		
	}
}
