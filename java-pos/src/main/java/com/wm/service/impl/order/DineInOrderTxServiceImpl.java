package com.wm.service.impl.order;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jeecgframework.core.common.model.json.MsgResp;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.constant.order.OrderType;
import com.base.enums.PayEnum;
import com.wm.dao.agent.AgentIncomeTimerDao;
import com.wm.dao.dineorder.DineOrderDao;
import com.wm.dao.order.OrderDao;
import com.wm.dao.orderrefund.OrderRefundDao;
import com.wm.dao.orderstate.OrderStateDao;
import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.CashSettlementRelationEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.entity.orderrefund.OrderRefundEntity;
import com.wm.entity.orderstate.OrderStateEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.impl.flow.NotEnoughBalanceException;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.DineInOrderTxServiceI;
import com.wm.service.order.refund.OrderRefundDelegateService;
import com.wm.service.order.refund.OrderRefundExecutor;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;
import com.wm.util.DateUtil;

/**
 * 堂食订单服务实现（事务处理部分）
 */
@Service("dineInOrderTxService")
@Transactional
public class DineInOrderTxServiceImpl extends CommonServiceImpl implements DineInOrderTxServiceI {
	private static final Logger LOGGER = LoggerFactory.getLogger(DineInOrderTxServiceImpl.class);

	@Resource
	private FlowServiceI flowService;
	@Resource
	private OrderDao orderDao;
	@Resource
	private OrderStateDao orderStateDao;
	@Resource
	private OrderRefundDao orderRefundDao;
	@Resource
	private DineOrderDao dineOrderDao;
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	@Resource
	private AgentIncomeTimerDao agentIncomeTimerDao;
	@Resource
	private OrderRefundDelegateService orderRefundDelegateService;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, NotEnoughBalanceException.class, OrderRefundFailException.class })
	public MsgResp chargeback(Integer orderId, Integer opUserId) throws NotEnoughBalanceException, OrderRefundFailException {
		OrderEntity order = orderDao.get(OrderEntity.class, orderId);
		MsgResp precheck = chargebackPrecheck(order);
		if (!precheck.isSuccess()) {
			return precheck;
		}

		merchantMoneyReduce(order);
		agentIncomeTimerClear(order);
		dineOrderClear(order);
		updateOrder(order);
		
		//恢复库存
		if(!OrderEntity.OrderType.SUPERMARKET_CODEFREE.equals(order.getOrderType())){
			menuService.revertRepertory(orderId);
		}
		// 在线支付（微信、支付宝等）的退款请求是无法回滚的，尽可能放后面
		userMoneyRefund(order, opUserId);
		return MsgResp.successResp("退单成功");
	}

	/**
	 * 修改订单状态为：已完成（已退款）
	 * @param order 订单对象
	 */
	private void updateOrder(OrderEntity order) {
		order.setState(OrderEntity.State.CONFIRM);
		order.setRstate(OrderEntity.Rstate.BEREFUND);
		orderDao.save(order);
	}

	/**
	 * 根据订单信息，如果订单预收入信息存在，则对商家余额进行扣除（扣除的是订单对应的预收入）
	 * @param order 订单对象
	 * @throws NotEnoughBalanceException 余额不足异常
	 */
	private void merchantMoneyReduce(OrderEntity order) throws NotEnoughBalanceException {
		//判断订单是否是现金订单
		if(StringUtils.equals(order.getOrderType(), OrderType.SUPERMARKET.getName()) 
				&& StringUtils.equals(order.getPayType(), PayEnum.supermarkt_cash.getEn())){
			CashSettlementRelationEntity relation = this.get(CashSettlementRelationEntity.class, order.getId());
			MerchantInfoEntity merchantInfoEntity = findUniqueByProperty(MerchantInfoEntity.class, "merchantId", order.getMerchant().getId());
			//如果是现金订单，判断订单是否已经结算，如果没有结算,不需要扣商家用户余额,加盟店现金订单退款不扣除商家用户余额
			if(relation != null && StringUtils.equals(relation.getIsSettlemented(), "0") || 1 == merchantInfoEntity.getShopFromType().intValue()){
				return;
			}
		}
		OrderIncomeEntity orderIncome = orderIncomeService.getOrderIncomeByOrderIdAndType(order.getId(), 0);
		if (orderIncome == null) {
			throw new RuntimeException("can't find orderIncomeEntity.");
		}
		BigDecimal reducedMoney = BigDecimal.valueOf(orderIncome.getMoney());
		flowService.dineInOrderMerchantRefund(order.getId(), order.getMerchant().getWuser().getId(), reducedMoney);
	}

	/**
	 * 根据订单信息选择对应的退款渠道，对用户支付的金额进行退款，保存退款记录
	 * @param order 订单对象
	 * @param opUserId 操作人ID
	 * @throws OrderRefundFailException 订单退款失败
	 */
	private void userMoneyRefund(OrderEntity order, Integer opUserId) throws OrderRefundFailException {
//		LOGGER.info("userMoneyRefund order : " + JSONObject.toJSONString(order));
		DineInOrderRefundParam param = new DineInOrderRefundParam(order, getRefundFee(order));
		OrderRefundExecutor executor = orderRefundDelegateService.getExecutor(order.getPayType());
		OrderRefundResult result = executor.execute(param);
		LOGGER.info("result : " + result);
		if (!result.isSuccess()) {
			throw new OrderRefundFailException();
		}
		saveOrderRefundRecord(order, opUserId, param, result);
		saveOrderStateEntity(order);
	}

	private void saveOrderStateEntity(OrderEntity order) {
		OrderStateEntity orderState = new OrderStateEntity();
		orderState.setDealTime(DateUtils.getSeconds());
		orderState.setOrderId(order.getId());
		orderState.setState("已同意退款");
		orderState.setDetail("商家" + order.getMerchant().getWuser().getUsername() + "同意取消订单");
		orderStateDao.save(orderState);
	}

	private void saveOrderRefundRecord(OrderEntity order, Integer opUserId, DineInOrderRefundParam param,
			OrderRefundResult result) {
		OrderRefundEntity orderRefund = new OrderRefundEntity();
		orderRefund.setOutRefundNo(result.getOutRefundNo());
		orderRefund.setOutTradeNo(order.getPayId());
		orderRefund.setPayType(order.getPayType());
		orderRefund.setRefundFee(yuanToFen(param.getRefundFee()));
		orderRefund.setTotalFee(yuanToFen(param.getRefundFee()));
		String outTraceId = order.getOutTraceId();
		if (StringUtils.isEmpty(outTraceId)) {
			outTraceId = String.valueOf(System.currentTimeMillis());
		}
		orderRefund.setTransactionId(outTraceId);
		orderRefund.setOpUserId(opUserId);
		orderRefund.setCreateTime(DateUtils.getSeconds());
		orderRefund.setStatus(OrderRefundEntity.STATUS_SUCCESS);
		orderRefundDao.save(orderRefund);
	}

	private String getRefundFee(OrderEntity order) {
		BigDecimal refundFee = BigDecimal.valueOf(order.getOnlineMoney()).add(BigDecimal.valueOf(order.getCredit()));
		return refundFee.toPlainString();
	}

	private Integer yuanToFen(String yuan) {
		return new BigDecimal(yuan).multiply(new BigDecimal(100)).intValue();
	}

	/**
	 * 根据订单信息清理对应的代理商预收入定时器表的记录（如果存在）
	 * @param order 订单对象
	 */
	private void agentIncomeTimerClear(OrderEntity order) {
		agentIncomeTimerDao.deleteByOrderId(order.getId());
	}

	/**
	 * 根据订单信息修改对应的电视排号记录状态，并情况缓存
	 * @param order 订单对象
	 */
	private void dineOrderClear(OrderEntity order) {
		int deleteRows = dineOrderDao.deleteByOrderId(order.getId());
		if (deleteRows > 0) {
			// 0085_dinein_order表变动清缓存。实现方式待重构
			AliOcs.syncRemove(CacheKeyUtil.getMealPreList(order.getMerchant().getId())); // 清除备餐列表缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealList(order.getMerchant().getId())); // 清除出餐列表缓存
		}
	}

	@Override
	public MsgResp chargebackPrecheck(OrderEntity order) {
		if (order == null) {
			return MsgResp.failResp("订单不存在");
		}
		if (!OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
			return MsgResp.failResp("订单不是堂食订单");
		}
		if (!DateUtil.isToday(order.getCreateTime() * 1000L)) {
			return MsgResp.failResp("堂食订单只能当天退单");
		}
		final String[] unmatchOrderTypes = { OrderEntity.OrderType.SCAN_ORDER, OrderEntity.OrderType.ALI_SCAN_ORDER };
		if (ArrayUtils.contains(unmatchOrderTypes, order.getOrderType())) {
			return MsgResp.failResp("该订单类型不支持退单");
		}
		final String[] matchStates = { OrderEntity.State.PAY, OrderEntity.State.ACCEPT, OrderEntity.State.DONE,
				OrderEntity.State.EVALUATED, OrderEntity.State.CONFIRM };
		if (!ArrayUtils.contains(matchStates, order.getState())) {
			return MsgResp.failResp("该订单状态不支持退单");
		}
		final String[] matchRstates = { OrderEntity.Rstate.NORMAL };
		if (!ArrayUtils.contains(matchRstates, order.getRstate())) {
			return MsgResp.failResp("该订单状态不支持退单");
		}
		return MsgResp.successResp();
	}
	
	@Override
	public MsgResp chargebackPrecheck(Integer orderId) {
		OrderEntity order = orderDao.findUniqueByProperty(OrderEntity.class, "id", orderId);
		return chargebackPrecheck(order);
	}

}
