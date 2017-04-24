package com.wp;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.StringUtil;


public class PayService {
	public static String createJSPackage(String body, String money, String orderNo, String openId,HttpServletRequest request){   //参数：商品描述、金额、订单号
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", body);
		parameters.put("out_trade_no", orderNo);
		parameters.put("total_fee", money);
		parameters.put("spbill_create_ip",request.getRemoteAddr()); 
		parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
		parameters.put("trade_type", "JSAPI");
		parameters.put("openid", openId);
		String sign = PayCommonUtil.createSign("UTF-8", parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		if(requestXML == null){
			return null;
		}
		return requestXML;
	}
	
	public static String createJSPackage(String body, String money, String orderNo, String openId,String ip){   //参数：商品描述、金额、订单号
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", body);
		parameters.put("out_trade_no", orderNo);
		parameters.put("total_fee", money);
		parameters.put("spbill_create_ip",ip); 
		parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
		parameters.put("trade_type", "JSAPI");
		parameters.put("openid", openId);
		String sign = PayCommonUtil.createSign("UTF-8", parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}

	/**
	 * 生成微信统一下单的XML
	 * @param appId 公众账号ID
	 * @param mchId 商户号
	 * @param apiKey 商户密钥
	 * @param body 商品描述
	 * @param totalFee 总金额
	 * @param outTradeNo 商户订单号
	 * @param spbillCreateIp 终端IP
	 * @param notifyUrl 微信通知回调地址
	 * @param nonceStr 随机字符串
	 * @param tradeType 交易类型取值如下：JSAPI，NATIVE，APP，WAP
	 * @param openId 用户标识trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
	 * @return
	 */
	public static String createWxPayXml(String appId,String mchId,String apiKey,String body, String totalFee, String outTradeNo, String spbillCreateIp,String notifyUrl,String nonceStr,String tradeType,String openId){   
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", appId);
		parameters.put("mch_id", mchId);
		parameters.put("nonce_str", nonceStr);
		parameters.put("body", body);
		parameters.put("out_trade_no", outTradeNo);
		parameters.put("total_fee", totalFee);
		parameters.put("spbill_create_ip",spbillCreateIp); 
		parameters.put("notify_url", notifyUrl);
		parameters.put("trade_type", tradeType);
		if(StringUtil.isNotEmpty(openId)){
			parameters.put("openid", openId);
		}
		String sign = PayCommonUtil.createSign("UTF-8", parameters,apiKey);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	/**
	 * 退款申请请求生成xml
	 * 
	 * @param transactionId 微信订单号
	 * @param orderNo 商户订单号
	 * @param outRefundNo 商户退款单号
	 * @param totalFee 总金额
	 * @param refundFee 退款金额
	 * @return
	 */
	public static String createRefundJSPackage(String transactionId,
			String orderNo, String outRefundNo, String totalFee,
			String refundFee) {
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		if(StringUtils.isNotBlank(transactionId)){
			parameters.put("transaction_id", transactionId);
		}
			
		if(StringUtils.isNotBlank(orderNo)){
			parameters.put("out_trade_no", orderNo);
		}
		
		parameters.put("out_refund_no", outRefundNo);
		parameters.put("total_fee", totalFee);
		parameters.put("refund_fee", refundFee);
		parameters.put("op_user_id", ConfigUtil.MCH_ID_KFZ);
		String sign = PayCommonUtil.createSign("UTF-8", parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	public static String createQRCodeJSPackage(String body, String money, String orderNo, String openId,String ip,String notifyUrl){   //参数：商品描述、金额、订单号
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", body);
		parameters.put("out_trade_no", orderNo);
		parameters.put("total_fee", money);
		parameters.put("spbill_create_ip",ip); 
		parameters.put("notify_url", notifyUrl);
		parameters.put("trade_type", "JSAPI");
		parameters.put("openid", openId);
		String sign = PayCommonUtil.createSign("UTF-8", parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	
	/**
	 * 创建扫用户微信条形码支付参数的xml
	 * @param authCode
	 * @param body
	 * @param outTradeNo
	 * @param attach
	 * @param totalFee
	 * @param deviceInfo
	 * @param spBillCreateIP
	 * @param timeStart
	 * @param timeExpire
	 * @param goodsTag
	 * @return
	 */
	public static String createBarcodePayXml(String authCode, String body, String outTradeNo, 
			String attach, String totalFee, String deviceInfo,  String spBillCreateIP,
			String timeStart, String timeExpire, String goodsTag){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("auth_code", authCode);
		parameters.put("body", body);
		parameters.put("out_trade_no", outTradeNo);
		if(StringUtils.isNotBlank(attach)){
			parameters.put("attach", attach);
		}
		parameters.put("total_fee", totalFee);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		if(StringUtils.isNotBlank(deviceInfo))
			parameters.put("device_info",deviceInfo);
		
		if(StringUtils.isNotBlank(spBillCreateIP)){
			parameters.put("spbill_create_ip",spBillCreateIP); 
		}
		parameters.put("time_start",timeStart); 
		parameters.put("time_expire",timeExpire); 
		if(StringUtils.isNotBlank(goodsTag)){
			parameters.put("goods_tag",goodsTag);
		}
		String sign = PayCommonUtil.createSign(null, parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	/**
	 * 创建微信退款支付参数
	 * @param outTradeNo
	 * @param totalFee
	 * @param refundFee
	 * @param deviceInfo
	 * @return
	 */
	public static String createRefundXml(String outTradeNo, int totalFee, int refundFee, 
			String deviceInfo){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("out_trade_no", outTradeNo);
		parameters.put("out_refund_no", UUID.randomUUID().toString());
		parameters.put("total_fee", String.valueOf(totalFee));
		parameters.put("refund_fee", String.valueOf(refundFee));
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("op_user_id", ConfigUtil.MCH_ID_KFZ);
		
		if(StringUtils.isNotBlank(deviceInfo)){
			parameters.put("device_info", deviceInfo);
		}
		
		String sign = PayCommonUtil.createSign(null, parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	/**
	 * 微信订单支付状态查询
	 * @param outTradeNo 订单的payId
	 * @return
	 */
	public static String createOrderPayQueryXml(String outTradeNo){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("out_trade_no", outTradeNo);
		String sign = PayCommonUtil.createSign(null, parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}
	
	/**
	 * 撤销订单
	 * @param outTradeNo 订单的payId
	 * @return
	 */
	public static String createReverseOrderXml(String outTradeNo){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("out_trade_no", outTradeNo);
		String sign = PayCommonUtil.createSign(null, parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);
	}

	
	/**
	 * 根据条形码获取openId
	 * @param authCode
	 * @return
	 */
	public static String createGetOpenIdXml(String authCode){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", ConfigUtil.APPID_KFZ);
		parameters.put("mch_id", ConfigUtil.MCH_ID_KFZ);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("auth_code", authCode);
		String sign = PayCommonUtil.createSign(null, parameters,ConfigUtil.API_KEY);
		parameters.put("sign", sign);
		return PayCommonUtil.getRequestXml(parameters);

	}
	
	/**
	 * 模拟错误返回
	 * @return
	 */
	public static String createFailXml(String errCode){
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("result_code", "FAIL");
		parameters.put("return_code", "FAIL");
		parameters.put("err_code", errCode);
		return PayCommonUtil.getRequestXml(parameters);

	}

}
