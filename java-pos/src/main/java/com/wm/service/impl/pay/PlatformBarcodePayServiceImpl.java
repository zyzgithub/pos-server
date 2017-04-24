package com.wm.service.impl.pay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.base.enums.OrderStateEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.user.WUserServiceI;
import com.wm.util.HttpUtils;
import com.wp.ConfigUtil;


@Service("platformBarcodePayService")
@Transactional
public class PlatformBarcodePayServiceImpl extends CommonServiceImpl implements BarcodePayServiceI {

	private final static Logger logger = LoggerFactory.getLogger(PlatformBarcodePayServiceImpl.class);
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private OrderHandleFactoryImpl orderHandleService;
	
	@Override
	public String getOpenId(String authCode){
		return null;
	}
	
	
	@Override
	public BarcodePayResponse payOrder(Integer userId, Integer orderId, Map<String, String> otherParams) {
		String source = otherParams.get("receive_from_source");
		if(source!=null && source.equals("merchant")){
			return payOrderForMerchant(otherParams);
		}
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
			response.setMsg("订单已支付");
			logger.info("订单id:{}已支付,不需重复支付", orderId);
			return response;
		}
		
		Double totalFee =order.getOrigin();
		otherParams.put("orderId", orderId.toString());
		otherParams.put("merchantId", order.getMerchant().getId().toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(otherParams);
		try {
			String barcodeNumber = otherParams.get("barcodeNumber");
			BarcodePayResponse payResponse = isPaySuccess(params, barcodeNumber,ConfigUtil.POS_PLATFORM_BARCODE_PAY_URL);
			if(payResponse.getCode()  == 0){
				order.setOutTraceId(barcodeNumber);
				Map<String, Object> payMap = payResponse.getResponse().getBody();
				userId = Integer.valueOf(payMap.get("userId").toString());
				if(userId.intValue() != 0){
					WUserEntity user = this.get(WUserEntity.class, userId);
					if(user != null){
						order.setWuser(user);
					}
					//支付方式
					String payType = payMap.get("payType").toString();
					//实际支付金额
					Double credit = Double.valueOf(payMap.get("payMoney").toString());
					//优惠金额
					Double discountMoney = Double.valueOf(String.format("%.2f", totalFee - credit));
					//立减优惠
			        Double minusDiscountMoney = 0.00;
			        //会员优惠
			        Double memberDiscountMoney = 0.00;
					//商家会员支付
					if(OrderEntity.PayType.MERCHANTPAY.equals(payType)){
						order.setMerchantMemberDiscountMoney(discountMoney);
					}
					//平台会员支付
					if(OrderEntity.PayType.BALANCE.equals(payType)){
						order.setMemberDiscountMoney(discountMoney);
					}
					order.setCredit(credit);
					order.setPayType(payType);
					this.saveOrUpdate(order);
					
					//更新订单的状态，及其他业务处理
				    OrderHandler orderPayBackHandler = orderHandleService.getHandler(order.getOrderType());
			        orderPayBackHandler.handle(order);
			        //会员优惠
			        if("vipDiscount".equals(payMap.get("discountType").toString())){
			        	memberDiscountMoney = discountMoney;
			        }
			        //立减优惠
			        if("randomDiscount".equals(payMap.get("discountType").toString())){
			        	minusDiscountMoney = discountMoney;
			        }
			        Map<String, Object> bodyMap = new HashMap<String, Object>();
			        bodyMap.put("memberDiscountMoney", memberDiscountMoney);
			        bodyMap.put("minusDiscountMoney", minusDiscountMoney);
			        PlatformBarcodePayResponse pResponse = new PlatformBarcodePayResponse();
			        pResponse.setBody(bodyMap);
			        response.setResponse(pResponse);
				}
			}
			else{
				response = BarcodePayResponse.FAILURE;
				response.setMsg(payResponse.getMsg());
				
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
	
	private BarcodePayResponse postRequest(String url, Map<String, Object> params){
		JSONObject paramsObject = new JSONObject(params);
		String responseStr = HttpUtils.post(url, paramsObject, true, String.class, false);
		BarcodePayResponse  response = JSONHelper.toBean(responseStr, BarcodePayResponse.class);
		return response;
	}
	
	private BarcodePayResponse getRequest(String url){
		String responseStr = HttpUtils.get(url);
		BarcodePayResponse  response = JSONHelper.toBean(responseStr, BarcodePayResponse.class);
		return response;
	}
	
	public BarcodePayResponse payOrderForMerchant(Map<String, String> otherParams){
		BarcodePayResponse response = new BarcodePayResponse();
		Map<String, Object> params = new HashMap<String, Object>();
		
		otherParams.remove("receive_from_source");
		params.putAll(otherParams);
		try {
			BarcodePayResponse payResponse = postRequest(ConfigUtil.PLATFORM_BARCODE_PAY_URL, params);
			if(payResponse == null){
				response = BarcodePayResponse.FAILURE;
				response.setMsg("未知错误");
				return response;
			}
			
			//支付成功
			if(payResponse.getCode()== 0){
				response.setResponse(payResponse.getResponse());
			}
			else{
				response = BarcodePayResponse.FAILURE;
				response.setMsg(payResponse.getMsg());
			}
		} 
		catch (Exception e) {
			response = BarcodePayResponse.FAILURE;
			e.printStackTrace();
		}
		return response;
	}

	private BarcodePayResponse isPaySuccess(Map<String, Object> params, String barcodeNumber, String payUrl){
		BarcodePayResponse response = postRequest(payUrl, params);
		if(response == null){
			response = BarcodePayResponse.FAILURE;
			response.setMsg("未知错误");
			return response;
		}
		
		//支付成功
		if(response.getCode() == 0){
			return getRequest(ConfigUtil.PLATFORM_BARCODE_QUERY_URL + barcodeNumber);
		}
		
		//等待用户输入密码
		else if(response.getCode() == 7060){
			//轮询次数为12
			for(int i = 0; i < 12; i++){
				try {
					//5秒查一次
					Thread.sleep(5000);
					//用户端用户输入密码正确，不做业务处理，需要再次调支付接口去激活用户端处理业务
					BarcodePayResponse qureyResponse = getRequest(ConfigUtil.PLATFORM_BARCODE_QUERY_URL + barcodeNumber);
					if(qureyResponse != null){
						if(qureyResponse.getCode() == 0 && "pay".equals(qureyResponse.getResponse().getBody().get("payState").toString())){
							logger.info("第{}次查询，支付成功...", i+1);
							return qureyResponse;
						}
						else {
							logger.info("第{}次查询，支付失败...", i+1);
						}
					}
				} 
				catch (Exception e) {
					return BarcodePayResponse.FAILURE;
				}
			}
			//轮询完，未返回成功
			return  BarcodePayResponse.FAILURE;
		}
		else {
			return  BarcodePayResponse.FAILURE;
		}
	}
}