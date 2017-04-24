package com.wm.service.impl.pay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.user.WUserServiceI;


@Service("wxBarcodePayService")
@Transactional
public class WxBarcodePayServiceImpl extends CommonServiceImpl implements BarcodePayServiceI {

	private final static Logger logger = LoggerFactory.getLogger(WxBarcodePayServiceImpl.class);
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private OrderHandleFactoryImpl orderHandleService;
	
	
	@Override
	public String getOpenId(String authCode){
		Map<String, String> result = WxBarcodePayApi.getOpenId(authCode);
		if(result != null && StringUtils.equals(result.get("return_code"), "SUCCESS") 
				&& StringUtils.equals(result.get("result_code"), "SUCCESS")){
			return result.get("openid");
		}
		else{
			if(result != null){
				logger.info("根据authCode获取用户信息失败，返回信息:{}", JSON.toJSONString(result));
			}
		}
		return null;
	}
	
	
	@Override
	public BarcodePayResponse payOrder(Integer userId, Integer orderId, Map<String, String> otherParams) {
		BarcodePayResponse response = new BarcodePayResponse();
		OrderEntity order = this.get(OrderEntity.class, orderId);
		if(order == null){
			response.setCode(1001);
			response.setMsg("参数错误，订单不存在");
			logger.warn("无法根据订单id:{}获取订单", orderId);
			return response;
		}
		
		if(StringUtils.equals(OrderStateEnum.PAY.getOrderStateEn(), order.getPayState())){
			response = BarcodePayResponse.FAILURE;
			response.setCode(1001);
			response.setMsg("订单已付款");
			logger.info("订单id:{}已付款,不需重复付款", orderId);
			return response;
		}
		
		otherParams.put("out_trade_no", order.getPayId());
		if("merchantQcCode".equals(order.getFromType())){//商家扫用户条形码付款
			otherParams.put("body", "1号生活--" + order.getMerchant().getTitle());
		}else{
			otherParams.put("body", "1号生活715超市--" + order.getMerchant().getTitle());
		}
		int totalFee = (int)(order.getOrigin()*100);
		otherParams.put("total_fee", String.valueOf(totalFee));
		try {
			Map<String, String> result = WxBarcodePayApi.payOrder(otherParams);
			if(result == null){
				response = BarcodePayResponse.FAILURE;
				response.setMsg("未知错误");
				return response;
			}
			
			result.put("out_trade_no", order.getPayId());
			//对付款结果进行处理
			response = WxBarcodePayReturnHandler.handle(result);
			//付款成功
			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
				
				String outTraceId = result.get("transaction_id");
				order.setOutTraceId(outTraceId);
				//更新订单对应的用户及订单的付款类型
				if(userId.intValue() != 0){
					WUserEntity user = this.get(WUserEntity.class, userId);
					if(user != null){
						order.setWuser(user);
					}
				}
				if("merchantQcCode".equals(order.getFromType())){//商家扫用户条形码付款
					order.setPayType(OrderEntity.PayType.WEIXINPAY);
				}else{
					order.setPayType(PayEnum.supermarkt_wxbarcode.getEn());
				}
				order.setOnlineMoney(order.getOrigin());
				order.setPayState("pay");
				order.setPayTime(DateUtils.getSeconds());
				orderService.updateEntitie(order);
				
				//更新订单的状态，及其他业务处理
			    OrderHandler orderPayBackHandler = orderHandleService.getHandler(order.getOrderType());
		        orderPayBackHandler.handle(order);
			}
		} 
		catch (Exception e) {
			response = BarcodePayResponse.FAILURE;
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public BarcodePayResponse refundOrder(Integer orderId){
		BarcodePayResponse response = new BarcodePayResponse();
		OrderEntity order = this.get(OrderEntity.class, orderId);
		if(order == null){
			response.setCode(1001);
			response.setMsg("参数错误，订单不存在");
			logger.warn("无法根据订单id:{}获取订单", orderId);
			return response;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("out_trade_no", order.getPayId());
		params.put("total_fee", String.valueOf(((int)(order.getOrigin()*100))));
		params.put("refund_fee",  String.valueOf(((int)(order.getOrigin()*100))));
		Map<String, String> result = WxBarcodePayApi.refundOrder(params);
		if(result == null){
			response = BarcodePayResponse.FAILURE;
			response.setMsg("内部异常");
		}
		else {
			if(StringUtils.equals(result.get("result_code"),  BarcodePayResponse.WX_SUCCESS)
					&& StringUtils.equals(result.get("return_code"), BarcodePayResponse.WX_SUCCESS)){
				response = BarcodePayResponse.SUCCESS;
			}
			else {
				response = BarcodePayResponse.FAILURE;
				response.setMsg("退款失败");
			}
		}
		return response;
		
	}
	
}