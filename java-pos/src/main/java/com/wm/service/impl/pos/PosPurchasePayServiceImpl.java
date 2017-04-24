package com.wm.service.impl.pos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.base.config.EnvConfig;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.pos.PurchaseDetail;
import com.wm.service.impl.pay.BarcodePayResponse;
import com.wm.service.impl.pay.WxBarcodePayApi;
import com.wm.service.impl.pay.WxBarcodePayReturnHandler;
import com.wm.service.merchant.MerchantSupplyServiceI;
import com.wm.service.pos.BasicPosOrderServiceI;
import com.wm.service.pos.PosPurchasePayServiceI;
import com.wm.util.HttpUtils;

/**
 * @author  zhanxinming 
 * @date 创建时间：2016年8月26日 上午11:34:09    
 * @return
 * @version 1.0
 */
@Service
@Transactional
public class PosPurchasePayServiceImpl extends CommonServiceImpl implements
		PosPurchasePayServiceI {

	private static final Logger logger = LoggerFactory.getLogger(PosPurchasePayServiceImpl.class);
	
	
	@Autowired
	private MerchantSupplyServiceI merchantSupplyService;
	@Autowired
	private BasicPosOrderServiceI basicPosOrderService;

	@Override
	public BarcodePayResponse onWeixinPay(Map<String, String> params) {
		BarcodePayResponse response = new BarcodePayResponse();
		try{
			Integer mainOrderId = Integer.valueOf(params.get("mainOrderId"));
			Integer merchantId = Integer.valueOf(params.get("merchantId"));
			PurchaseDetail purchaseDetail = basicPosOrderService.getUnUsePurchaseDetail(mainOrderId);
			if(purchaseDetail == null){
				response.setCode(1001);
				response.setMsg("找不到订单信息，mainOrderId: " + mainOrderId);
				logger.info("找不到订单信息，mainOrderId:{}", mainOrderId);
				return response;
			}
			String outTradeNo = purchaseDetail.getPayId();
			String origin = String.valueOf((int)(purchaseDetail.getOrigin()*100));
			MerchantEntity merchantEntity = get(MerchantEntity.class, merchantId);
			if(merchantEntity == null){
				response.setCode(1001);
				response.setMsg("参数错误，商家不存在");
				logger.warn("无法根据merchantId:{}获取商家", merchantId);
				return response;
			}
			params.put("total_fee", origin);
			params.put("out_trade_no", outTradeNo);
			params.put("body", "1号生活715超市--" + merchantEntity.getTitle() + "--采购");
			Map<String, String> result = WxBarcodePayApi.payOrder(params);
			if(result == null){
				logger.error("超市采购微信条码支付，未知错误，供应链订单号:{}, out_trade_no:{}", mainOrderId, outTradeNo);
				response = BarcodePayResponse.FAILURE;
				response.setMsg("未知错误");
				return response;
			}
			//对支付结果进行处理
			response = WxBarcodePayReturnHandler.handle(result);
			//支付成功
			if(response.getCode() == BarcodePayResponse.SUCCESS_CODE){
				logger.info("超市采购微信条码支付成功，供应链订单号:{}, out_trade_no:{}", mainOrderId, outTradeNo);

				//通知供应链
				String partnerTradeNO = result.get("transaction_id");
				supplyMcNotify(Long.valueOf(merchantEntity.getWuser().getId().toString()), Long.valueOf(mainOrderId), "weixinpay", partnerTradeNO, Double.valueOf(origin));
				
            	basicPosOrderService.enablePurchaseDetail(mainOrderId);
			}
	} 
	catch (Exception e) {
		response = BarcodePayResponse.FAILURE;
		e.printStackTrace();
	}
	return response;
	}

	@Override
	public BarcodePayResponse onAliPay(Map<String, Object> params) {
		BarcodePayResponse response = new BarcodePayResponse();
		Integer mainOrderId = Integer.valueOf(params.get("mainOrderId").toString());
		PurchaseDetail purchaseDetail = basicPosOrderService.getUnUsePurchaseDetail(mainOrderId);
		if(purchaseDetail == null){
			response.setCode(1001);
			response.setMsg("找不到订单信息，mainOrderId: " + mainOrderId);
			logger.info("找不到订单信息，mainOrderId:{}", mainOrderId);
			return response;
		}
		Integer merchantId = Integer.valueOf(params.get("merchantId").toString());
		MerchantEntity merchantEntity = get(MerchantEntity.class, merchantId);
		if(merchantEntity == null){
			response.setCode(1001);
			response.setMsg("参数错误，商家不存在");
			logger.warn("无法根据merchantId:{}获取商家", merchantId);
			return response;
		}
		AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        String outTradeNo = purchaseDetail.getPayId();
        String authCode = params.get("auth_code").toString();
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = purchaseDetail.getOrigin().toString();
        String subject = "1号生活715超市--" + merchantEntity.getTitle() + "--采购";

        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "采购商品，共" + totalAmount + "元";
        

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = String.valueOf(merchantEntity.getId());

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = String.valueOf(merchantEntity.getId());

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
        
        logger.info("超市采购，merchantId:{}扫条形码支付宝支付,支付结果:{}", merchantId, JSON.toJSONString(result.getResponse()));
        switch (result.getTradeStatus()) {
            case SUCCESS:
            	logger.info("支付宝支付成功, 供应链订单号:{}, out_trade_no:{} )", mainOrderId, outTradeNo);
            	response = BarcodePayResponse.SUCCESS;
            	//通知供应链
            	String partnerTradeNO = result.getResponse().getTradeNo();
            	supplyMcNotify(Long.valueOf(merchantEntity.getWuser().getId().toString()), Long.valueOf(mainOrderId), "alipay", partnerTradeNO, Double.valueOf(totalAmount));
            	
            	basicPosOrderService.enablePurchaseDetail(mainOrderId);
                break;

            case FAILED:
            	logger.error("支付宝支付付失败!!!, 供应链订单号:{}, out_trade_no:{} )", mainOrderId, outTradeNo);
            	response = BarcodePayResponse.FAILURE;
            	response.setMsg(result.getResponse().getBody());
                break;

            case UNKNOWN:
            	logger.error("支付宝支付付系统异常, 订单状态未知!!!, 供应链订单号:{}, out_trade_no:{} )", mainOrderId, outTradeNo);
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
	public AjaxJson onBalancePay(Integer merchantUserId, String payPassword, Integer mainOrderId, Integer merchantId) {
		AjaxJson json = new AjaxJson();
		json = merchantSupplyService.validatePayPassword(merchantUserId, payPassword);
		logger.info("验证密码是否成功，json="+json.toString());
		if(!json.isSuccess()){
			return json;
		}
		
		PurchaseDetail purchaseDetail = basicPosOrderService.getUnUsePurchaseDetail(mainOrderId);
		if(purchaseDetail == null){
			json.setStateCode("01");
			json.setMsg("找不到订单信息，mainOrderId: " + mainOrderId);
			logger.info("找不到订单信息，mainOrderId:{}", mainOrderId);
			return json;
		}
		
		String detail = "POS一键下单订单:" + mainOrderId +"支付";
		BigDecimal origin = BigDecimal.valueOf(purchaseDetail.getOrigin());
		json = merchantSupplyService.balanceConfirmPay(Long.valueOf(mainOrderId), merchantUserId, origin, "", "", detail);
		basicPosOrderService.enablePurchaseDetail(mainOrderId);
		//通知供应链
		supplyMcNotify(Long.valueOf(merchantUserId.toString()), Long.valueOf(mainOrderId), "balance", null, Double.valueOf(origin.toString()));
		json.setSuccess(true);
		json.setStateCode("00");
		return json;
	}
	
	/**
	 * 支付完成通知供应链，修改订单状态
	 * @param userId       商家用户id
	 * @param parentOrderId 供应链总单id
	 * @param payType		支付方式 ，微信：weixinpay， 余额：balance，支付宝： alipay，威富通：wft_pay
	 * @param partnerTradeNO 第三方流水号
	 * @param paymentAmount  总金额
	 */
	private void supplyMcNotify(Long userId, Long parentOrderId, String payType, String partnerTradeNO, Double paymentAmount){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			params.put("parentOrderId", parentOrderId);
			params.put("payType", payType);
			params.put("partnerTradeNO", partnerTradeNO);
			params.put("paymentAmount", paymentAmount);
			JSONObject paramsObject = new JSONObject(params);
			
			String url = EnvConfig.base.SUPPLY_MC_HOST + "/supplychainOrderController/posPaymentNotify.do?";
			HttpUtils.post(url, paramsObject, true, String.class, false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通知供应链失败，供应链订单号:{}", parentOrderId);
		}
	}
}
