package com.wm.service.impl.order.refund;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.dto.order.DineInOrderRefundParam;
import com.wm.dto.order.OrderRefundResult;
import com.wm.entity.order.CashSettlementRelationEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.service.impl.pay.AliBarcodePayApi;
import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.impl.pay.WxBarcodePayApi;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.refund.OrderRefundExecutor;
import com.wm.service.supermarket.SuperMarketServiceI;

@Component("cashOrderRefundExecutor")
public class CashOrderRefundExecutor implements OrderRefundExecutor{

	private static final Logger LOGGER = LoggerFactory.getLogger(CashOrderRefundExecutor.class);
	
	@Resource
	private OrderServiceI orderService;
	
	@Resource
	private SuperMarketServiceI  superMarketService;
	
	private boolean isSettled(CashSettlementRelationEntity entity){
		return entity.getSettlementOrderId() > 0 || StringUtils.equals(entity.getIsSettlemented(), "1");
	}
	
	@Override
	public OrderRefundResult execute(DineInOrderRefundParam orderRefundParam) {
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		try {
			CashSettlementRelationEntity relation = superMarketService.get(CashSettlementRelationEntity.class, orderRefundParam.getOrder().getId());
			
			//如果现金订单结算
			if(isSettled(relation)){
				Integer settlementId = relation.getSettlementOrderId();
				LOGGER.info("现金订单ID:{}已结算, 结算订单Id:{}", relation.getCashOrderId(), settlementId);
				
				//结算订单
				OrderEntity settlementOrder = orderService.get(OrderEntity.class, settlementId);
				//检查支付状态
				if(!StringUtils.equals(settlementOrder.getPayState(), OrderStateEnum.PAY.getOrderStateEn())){
					LOGGER.warn("结算订单还没有支付，结算订单Id:{}", settlementOrder.getId());
					return OrderRefundResult.failResult(outRefundNo); 
				}
				
				//微信支付
				if(StringUtils.equals(settlementOrder.getPayType(), PayEnum.supermarkt_wxbarcode.getEn())){
					return wxRefund(settlementOrder, relation);
				}
				//支付宝
				else if(StringUtils.equals(settlementOrder.getPayType(), PayEnum.supermarkt_alibarcode.getEn())){
					return aliRefund(settlementOrder, relation);
				}
				//加盟店
				else if(StringUtils.equals(settlementOrder.getPayType(), PayEnum.supermarkt_cash.getEn())){
					return OrderRefundResult.successResult("");
				}
				else {
					LOGGER.warn("不支持的结算订单支付方式");
					return OrderRefundResult.failResult(outRefundNo); 
				}
			}
			else {
				superMarketService.delete(relation);
				return OrderRefundResult.successResult(outRefundNo);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("cash order refund exception ......");
		}
		return OrderRefundResult.failResult(outRefundNo);
	}
	
	/**
	 * 支付宝退款
	 * @param settlementOrder 结算订单
	 * @param relation 现金订单与结算订单关联
	 * @return
	 */
	private OrderRefundResult aliRefund(OrderEntity settlementOrder, CashSettlementRelationEntity relation) {
		Integer settlementId = settlementOrder.getId();
		
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		Map<String, String> params = new HashMap<String, String>();
		params.put("out_trade_no", settlementOrder.getPayId());
		params.put("refund_reason", "商家同意退款");
		params.put("refund_amount", String.valueOf(relation.getMoney()));
		params.put("out_request_no", String.valueOf(relation.getCashOrderId()));
		try {
			AlipayTradeRefundResponse response = AliBarcodePayApi.refundOrder(params);
			
			if(StringUtils.equals(AliBarcodePayApi.SUCCESS, response.getCode())){
				LOGGER.info("现金订单， 订单Id:{}, 结算订单Id:{} 支付宝退款成功", relation.getCashOrderId(), settlementId);
				superMarketService.delete(relation);
				return OrderRefundResult.successResult(outRefundNo);
			}
			else {
				LOGGER.info("现金订单， 订单Id:{}, 结算订单Id:{} 微信退款失败, 错误信息:{}", relation.getCashOrderId(), settlementId, JSON.toJSONString(response));
				return OrderRefundResult.failResult(outRefundNo);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("现金订单， 订单Id:{}, 结算订单Id:{} 微信退款失败", relation.getCashOrderId(), settlementId);
			return OrderRefundResult.failResult(outRefundNo);
		}
	}
	
	/**
	 * 微信退款
	 * @param settlementOrder 结算订单
	 * @param relation 现金订单与结算订单关联
	 * @return
	 */
	private OrderRefundResult wxRefund(OrderEntity settlementOrder, CashSettlementRelationEntity relation) {
		Integer settlementId = settlementOrder.getId();
		
		String outRefundNo = String.valueOf(System.currentTimeMillis());
		Map<String, String> params = new HashMap<String, String>();
		params.put("out_trade_no", settlementOrder.getPayId());
		int total_fee = (int)(settlementOrder.getOrigin()*100);
		int refundFee = (int)(relation.getMoney()*100);
		params.put("total_fee", String.valueOf(total_fee));
		params.put("refund_fee", String.valueOf(refundFee));
		Map<String, String> result = WxBarcodePayApi.refundOrder(params);
		if(result == null){
			LOGGER.info("现金订单， 订单Id:{}, 结算订单Id:{} 微信退款失败", relation.getCashOrderId(), settlementId);
			return OrderRefundResult.failResult(outRefundNo);
		}
		else {
			if(StringUtils.equals(result.get("result_code"),  BarcodePayResponse.WX_SUCCESS)
					&& StringUtils.equals(result.get("return_code"), BarcodePayResponse.WX_SUCCESS)){
				LOGGER.info("现金订单， 订单Id:{}, 结算订单Id:{} 微信退款成功", relation.getCashOrderId(), settlementId);
				superMarketService.delete(relation);
				return OrderRefundResult.successResult(outRefundNo);
			}
			else {
				LOGGER.info("现金订单， 订单Id:{}, 结算订单Id:{} 微信退款失败, 错误信息:{}", relation.getCashOrderId(), settlementId, JSON.toJSONString(result));
				return OrderRefundResult.failResult(outRefundNo);
			}
		}
	}
}
