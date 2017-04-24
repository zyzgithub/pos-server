package com.wm.service.impl.pay;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.wm.entity.order.OrderEntity;
import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.pay.OrderHandler;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;

@Service("aliBarcodePayService")
@Transactional
public class AliBarcodePayServiceImpl extends CommonServiceImpl implements BarcodePayServiceI {

	private final static Logger logger = LoggerFactory.getLogger(AliBarcodePayServiceImpl.class);
	
	@Autowired
	private OrderServiceI orderService;
	
	@Resource
	private OrderHandleFactoryImpl orderHandleService;
	
	static{
		String path = AliBarcodePayServiceImpl.class.getClassLoader().getResource("").getPath();
		Configs.init(path + "properties/" + "alipay_web.properties");
	}
	
	@Override
	public BarcodePayResponse payOrder(Integer userId, Integer orderId,
			Map<String, String> otherParams) {
		
		BarcodePayResponse response = null;
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		if(order == null){
			response = BarcodePayResponse.FAILURE;
			response.setMsg("参数错误");
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
		
		AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        String outTradeNo = order.getPayId();
        String subject = "1号生活715超市--" + order.getMerchant().getTitle() + "购物";
        String authCode = otherParams.get("auth_code");
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getOrigin());

        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品" + "1" + "件共" + totalAmount + "元";
        if("merchantQcCode".equals(order.getFromType())){//商家扫用户条形码付款
        	subject = "1号生活--" + order.getMerchant().getTitle();
        	body = "本次消费共" + totalAmount + "元";
        }
        

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = String.valueOf(order.getMerchant().getId());

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = String.valueOf(order.getMerchant().getId());

        String timeExpress = "5m";

        // 创建请求builder，设置请求参数
        AlipayTradePayContentBuilder builder = new AlipayTradePayContentBuilder()
                .setOutTradeNo(outTradeNo)
                .setSubject(subject)
                .setAuthCode(authCode)
                .setTotalAmount(totalAmount)
                .setStoreId(storeId)
                .setUndiscountableAmount(undiscountableAmount)
                .setBody(body)
                .setOperatorId(operatorId)
                .setSellerId(sellerId)
                .setTimeExpress(timeExpress);
        
        // 调用tradePay方法获取当面付应答
        AlipayF2FPayResult result = tradeService.tradePay(builder);
        
        logger.info("超市订单{}扫条形码支付宝付款,付款结果:{}", orderId, JSON.toJSONString(result.getResponse()));
        switch (result.getTradeStatus()) {
            case SUCCESS:
            	logger.info("支付宝付款成功, 订单号:{}, out_trade_no:{} )", orderId, outTradeNo);
            	response = BarcodePayResponse.SUCCESS;
            	
            	
            	
    			//更新订单的状态，及其他业务处理
            	OrderHandler orderPayBackHandler = orderHandleService.getHandler(order.getOrderType());
            	try {
            		if("merchantQcCode".equals(order.getFromType())){//商家扫用户条形码付款
            			order.setPayType(OrderEntity.PayType.ALIPAY);
            		}else{
            			order.setPayType(PayEnum.supermarkt_alibarcode.getEn());
            		}
            		String outTraceId = result.getResponse().getTradeNo();
                	order.setOutTraceId(outTraceId);
                	order.setPayState("pay");
    				order.setPayTime(DateUtils.getSeconds());
    				order.setOnlineMoney(order.getOrigin());
            		orderService.updateEntitie(order);
            		orderPayBackHandler.handle(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
                break;

            case FAILED:
            	logger.error("支付宝付款失败!!!, 订单号:{}, out_trade_no:{} )", orderId, outTradeNo);
            	response = BarcodePayResponse.FAILURE;
            	response.setMsg(result.getResponse().getBody());
                break;

            case UNKNOWN:
            	logger.error("支付宝付款系统异常, 订单状态未知!!!, 订单号:{}, out_trade_no:{} )", orderId, outTradeNo);
            	response = BarcodePayResponse.FAILURE;
            	response.setMsg("系统异常");
                break;

            default:
            	response = BarcodePayResponse.FAILURE;
            	response.setMsg("不支持的交易状态");
            	logger.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        
		return response;

	}

	@Override
	public String getOpenId(String authCode) {
		return "";
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
		params.put("refund_amount", String.valueOf(order.getOrigin()));
		params.put("refund_reason", "商家同意退款");
		try {
			AlipayTradeRefundResponse ret = AliBarcodePayApi.refundOrder(params);
			
			if(StringUtils.equals(AliBarcodePayApi.SUCCESS, ret.getCode())){
				response = BarcodePayResponse.SUCCESS;
                return response;
			}
			else {
				response = BarcodePayResponse.FAILURE;
				response.setMsg(ret.getMsg());
			}
		} 
		catch (Exception e) {
			response = BarcodePayResponse.FAILURE;
			response.setMsg("内部异常");
		}
		return response;
	}
	
	
}
