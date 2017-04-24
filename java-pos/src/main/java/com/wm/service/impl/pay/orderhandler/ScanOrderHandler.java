package com.wm.service.impl.pay.orderhandler;

import java.math.BigDecimal;
import java.util.Date;

import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntity.PayType;
import com.wm.entity.order.OrderLimitEntity;
import com.wm.entity.orderlimitlog.OrderLimitLogEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.OrderLimitLogServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.ScanDiscountLogServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.util.AliOcs;
import com.wm.util.StringUtil;

/**
 * 扫码订单回调处理器
 */
@Component("scanOrderHandler")
public class ScanOrderHandler implements OrderHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ScanOrderHandler.class);

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
	private ScanDiscountLogServiceI scanDiscountLogService;
	@Autowired
	private OrderLimitLogServiceI orderLimitLogService;//限额日志表

	@Override
	public void handle(OrderEntity order) throws Exception {
		long startTime = System.currentTimeMillis();
		Integer orderId = order.getId();
		logger.info("handle scan order. orderId={}", orderId);
		
		//TODO 应该提取到payService一进来的地方
		orderStateService.payOrderState(order);
		
		if(StringUtils.isEmpty(order.getOrderNum())){
			String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
			order.setOrderNum(orderNum);
		}
		order.setIsMerchantDelivery("courier");
		order.setAccessTime(DateUtils.getSeconds());
		order.setState("confirm");
		order.setCompleteTime(DateUtils.getSeconds());
		orderService.updateEntitie(order);
		
		orderStateService.accessOrderState(orderId);
		
		orderIncomeService.createOrderIncome(order);
		
		orderStateService.doneOrderState(orderId);
		
		scanDiscountLogService.processSanDiscount(order);
		
		//TODO 统计支付宝/微信扫码订单日志
		if(PayType.ALIPAY.equals(order.getPayType())){
			logger.info("统计支付宝当日限额开始");
			Integer amount = (new BigDecimal(order.getOrigin().toString()).multiply(new BigDecimal("100"))).intValue();
			//根据用户ID商家ID查询限额日志表
			OrderLimitLogEntity orderLimitLog = orderLimitLogService.getOrderLimitLogToday(order.getWuser().getId(),order.getMerchant().getId(),"alipay");
			if(StringUtil.isEmpty(orderLimitLog)){
				orderLimitLog = new OrderLimitLogEntity();
				orderLimitLog.setAmount(amount);
				orderLimitLog.setCreateTime(Integer.parseInt((System.currentTimeMillis()/1000L+"")));
				orderLimitLog.setMerchantId(order.getMerchant().getId());
				orderLimitLog.setPayType("alipay");
				orderLimitLog.setUserId(order.getWuser().getId());
				orderLimitLogService.createOrderLimitLog(orderLimitLog);
				logger.info("新建当日限额"+orderLimitLog.getId());
			}else{
				orderLimitLog.setAmount(orderLimitLog.getAmount() + amount);
				orderLimitLogService.updateOrderLimitLog(orderLimitLog);
				logger.info("更新当日限额"+orderLimitLog.getId());
			}
			logger.info("统计支付宝当日限额结束,amount:"+amount+"MerchantId:"+orderLimitLog.getMerchantId()+"userId:"+orderLimitLog.getUserId());
		}else if(PayType.WEIXINPAY.equals(order.getPayType()) ||  PayType.WFTPAY.equals(order.getPayType())){
			logger.info("统计微信当日限额开始");
			Integer amount = (new BigDecimal(order.getOrigin().toString()).multiply(new BigDecimal("100"))).intValue();
			//根据用户ID商家ID查询限额日志表
			OrderLimitLogEntity orderLimitLog = orderLimitLogService.getOrderLimitLogToday(order.getWuser().getId(),order.getMerchant().getId(),"weixinpay");
			if(StringUtil.isEmpty(orderLimitLog)){
				orderLimitLog = new OrderLimitLogEntity();
				orderLimitLog.setAmount(amount);
				orderLimitLog.setCreateTime(Integer.parseInt((System.currentTimeMillis()/1000L+"")));
				orderLimitLog.setMerchantId(order.getMerchant().getId());
				orderLimitLog.setPayType("weixinpay");
				orderLimitLog.setUserId(order.getWuser().getId());
				orderLimitLogService.createOrderLimitLog(orderLimitLog);
				logger.info("新建当日限额"+orderLimitLog.getId());
			}else{
				orderLimitLog.setAmount(orderLimitLog.getAmount() + amount);
				orderLimitLogService.updateOrderLimitLog(orderLimitLog);
				logger.info("更新当日限额"+orderLimitLog.getId());
			}
			logger.info("统计微信当日限额结束,amount:"+amount+"MerchantId:"+orderLimitLog.getMerchantId()+"userId:"+orderLimitLog.getUserId());
		}
		
		orderService.autoPrint(order);
		merchantService.pushOrder(orderId);
		messageService.sendMessage(order, "done");
		
		long costTime = System.currentTimeMillis() - startTime;
		logger.info("orderId:{} scanOrder handle costtime:{} ms", orderId, costTime);
	}

}
