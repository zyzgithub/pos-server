package com.wxpay.service;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.jeecgframework.core.util.oConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.takeout.vo.WeChatRefundVo;
import com.wp.CommonUtil;
import com.wp.ConfigUtil;
import com.wp.PayCommonUtil;
import com.wp.PayService;
import com.wp.XMLUtil;
import com.wxap.RequestHandler;
import com.wxap.util.Sha1Util;
import com.wxpay.util.ClientCustomSSL;
import com.wxpay.util.SDKRuntimeException;


@Service("wxPayService")
@Transactional
public class WxPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(WxPayService.class);
	
	private static String wxPartner;
	
	private static String wxNotifyUrl;
	
	private static String wxAppId;
	
	private static String wxAppKey;
	
	private static String wxPartnerKey;
	
	private static String wxAppScret;
	
	@Value("${WXPARTNER}")
	public void setWxPartner(String wxPartner) {
		WxPayService.wxPartner = wxPartner;
	}
	
	@Value("${WX_NOTIFY_URL}")
	public void setWxNotifyUrl(String wxNotifyUrl) {
		WxPayService.wxNotifyUrl = wxNotifyUrl;
	}
	
	@Value("${AppId}")
	public void setWxAppId(String wxAppId) {
		WxPayService.wxAppId = wxAppId;
	}
	
	@Value("${AppKey}")
	public void setWxAppKey(String wxAppKey) {
		WxPayService.wxAppKey = wxAppKey;
	}
	
	@Value("${PartnerKey}")
	public void setWxPartnerKey(String wxPartnerKey) {
		WxPayService.wxPartnerKey = wxPartnerKey;
	}
	
	@Value("${AppScret}")
	public void setWxAppScret(String wxAppScret) {
		WxPayService.wxAppScret = wxAppScret;
	}
	
	
	private static String appPayToken = null;
	
	
	public static String getOrderInfo(String body, String money, String orderNo) throws SDKRuntimeException {
		
		WxPayHelper wxPayHelper = new WxPayHelper();
		// 先设置基本信息
		wxPayHelper.SetAppId(wxAppId);// 商户注册具有支付权限的公众号成功后即可获得;
		wxPayHelper.SetAppKey(wxAppKey);// 公众号支付请求中用于加密的密钥 Key，可验证商户唯一身份
		wxPayHelper.SetPartnerKey(wxPartnerKey);// 财付通商户权限密钥 Key。
		wxPayHelper.SetSignType("sha1");// 按照文档中所示填入，目前仅支持 SHA1；
		// 设置请求package信息
		wxPayHelper.SetParameter("bank_type", "WX");// 固定为"WX"；
		wxPayHelper.SetParameter("body", body);// 商品描述;
		wxPayHelper.SetParameter("partner", wxPartner);// 注册时分配的财付通商户号 partnerId;
		wxPayHelper.SetParameter("out_trade_no", orderNo);// 商户系统内部的订单号，32
																				// 个字符内、可包含字母，确保在商户系统唯一；
		wxPayHelper.SetParameter("total_fee", String.valueOf(Double.parseDouble(money)*100));// 订单总金额，单位为分
		wxPayHelper.SetParameter("fee_type", "1");// 取值：1（人民币） ，暂只支持 1；
		wxPayHelper.SetParameter("notify_url", wxNotifyUrl);// notify_url在支付完成后，接收微信通知支付结果的
															// URL,需给绝对路径
															// ,255字符内,格式如:http://wap.tenpay.com/tenpay.asp;
		wxPayHelper.SetParameter("spbill_create_ip", oConvertUtils.getIp());// 指用户浏览器端
																			// IP，
																			// 不是商户服务器
																			// IP，
																			// 格式为IPV4;
		wxPayHelper.SetParameter("input_charset", "GBK");// 取值范围："GBK"、"UTF-8"，默认："GBK"

		String orderInfo = wxPayHelper.CreateAppPackage("body");
		if(orderInfo!=null){
			return orderInfo;
		}
		
		return null;
	}
	
	//web页面微信支付，生成jspackage
	public static String createJSPackage(String body, String money, String orderNo) throws SDKRuntimeException {

		WxPayHelper wxPayHelper = new WxPayHelper();
		// 先设置基本信息
		wxPayHelper.SetAppId(wxAppId);// 商户注册具有支付权限的公众号成功后即可获得;
		wxPayHelper.SetAppKey(wxAppKey);// 公众号支付请求中用于加密的密钥 Key，可验证商户唯一身份
		wxPayHelper.SetPartnerKey(wxPartnerKey);// 财付通商户权限密钥 Key。
		wxPayHelper.SetSignType("sha1");// 按照文档中所示填入，目前仅支持 SHA1；
		// 设置请求package信息
		wxPayHelper.SetParameter("bank_type", "WX");// 固定为"WX"；
		wxPayHelper.SetParameter("body", "text");// 商品描述;
		wxPayHelper.SetParameter("partner", wxPartner);// 注册时分配的财付通商户号 partnerId;
		wxPayHelper.SetParameter("out_trade_no", orderNo);// 商户系统内部的订单号，32
		wxPayHelper.SetParameter("total_fee", String.valueOf(Double.parseDouble(money)*100));// 订单总金额，单位为分
		wxPayHelper.SetParameter("fee_type", "1");// 取值：1（人民币） ，暂只支持 1；
		wxPayHelper.SetParameter("notify_url", wxNotifyUrl);// notify_url在支付完成后，接收微信通知支付结果的
															// URL,需给绝对路径
															// ,255字符内,格式如:http://wap.tenpay.com/tenpay.asp;
		wxPayHelper.SetParameter("spbill_create_ip", oConvertUtils.getIp());// 指用户浏览器端
																			// IP，
																			// 不是商户服务器
																			// IP，
																			// 格式为IPV4;
		wxPayHelper.SetParameter("input_charset", "UTF-8");// 取值范围："GBK"、"UTF-8"，默认："GBK"

		String orderInfo = wxPayHelper.CreateBizPackage();
		if(orderInfo!=null){
			return orderInfo;
		}
		return null;
	}
	
	/**
	 * 微信app支付（非公众号支付）
	 * @param body
	 * @param money
	 * @param orderNo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static TreeMap<String, String> getWxpayOrderInfo(String body, String money, String orderNo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		TreeMap<String, String> outParams = new TreeMap<String, String>();
		RequestHandler reqHandler = new RequestHandler(request, response);
	    //初始化 
		reqHandler.init();
		reqHandler.init(wxAppId, wxAppScret, wxAppKey, wxPartner, wxPartnerKey);
				
		//获取token值 
		String token = WxPayService.appPayToken;
				
		if(token == null){
			token = WxPayService.appPayToken = reqHandler.GetToken();
		}
		if (!"".equals(token)) {
			//=========================
			//生成预支付单
			//=========================
			//设置package订单参数
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			packageParams.put("bank_type", "WX"); //商品描述   
			packageParams.put("body", "测试商品名称"); //商品描述   
			packageParams.put("notify_url", wxNotifyUrl); //接收财付通通知的URL  
			packageParams.put("partner", wxPartner); //商户号    
			packageParams.put("out_trade_no", orderNo); //商家订单号  
			packageParams.put("total_fee", String.valueOf((int)(Double.parseDouble(money) * 100))); //商品金额,以分为单位  
			packageParams.put("spbill_create_ip", request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP  
			packageParams.put("fee_type", "1"); //币种，1人民币   66
			packageParams.put("input_charset", "GBK"); //字符编码

			//获取package包
			String packageValue = reqHandler.genPackage(packageParams);

			String noncestr = Sha1Util.getNonceStr();
			String timestamp = Sha1Util.getTimeStamp();
			String traceid = "mytestid_001";

			//设置支付参数
			SortedMap<String, String> signParams = new TreeMap<String, String>();
			signParams.put("appid", wxAppId);
			signParams.put("appkey", wxAppKey);
			signParams.put("noncestr", noncestr);
			signParams.put("package", packageValue);
			signParams.put("timestamp", timestamp);
			signParams.put("traceid", traceid);

			//生成支付签名，要采用URLENCODER的原始值进行SHA1算法！
			String sign = Sha1Util.createSHA1Sign(signParams);
			//增加非参与签名的额外参数
			signParams.put("app_signature", sign);
			signParams.put("sign_method", "sha1");

			//获取prepayId
			String prepayid = reqHandler.sendPrepay(signParams);
			if(reqHandler.getLasterrCode()=="40001"){
				token = WxPayService.appPayToken = reqHandler.getTokenReal();
		         prepayid = reqHandler.sendPrepay(signParams);
			}
			if (null != prepayid && !"".equals(prepayid)) {
				//签名参数列表
				SortedMap<String, String> prePayParams = new TreeMap<String, String>();
				prePayParams.put("appid", wxAppId);
				prePayParams.put("appkey", wxAppKey);
				prePayParams.put("noncestr", noncestr);
				prePayParams.put("package", "Sign=WXPay");
				prePayParams.put("partnerid", wxPartner);
				prePayParams.put("prepayid", prepayid);
				prePayParams.put("timestamp", timestamp);
				//生成签名
				sign = Sha1Util.createSHA1Sign(prePayParams);

				//输出参数
				outParams.put("retcode", "0");
				outParams.put("retmsg", "OK");
				outParams.put("appid", wxAppId);
				outParams.put("partnerid", wxPartner);
				outParams.put("noncestr", noncestr);
				outParams.put("package", "Sign=WXPay");
				outParams.put("prepayid", prepayid);
				outParams.put("timestamp", timestamp);
				outParams.put("sign", sign);
				//测试帐号多个app测试，需要判断Token是否失效，否则重新获取一次 
				if(reqHandler.getLasterrCode()=="40001"){
					token = WxPayService.appPayToken  = reqHandler.getTokenReal();
				}
			} else {
				outParams.put("retcode", "-2");
				outParams.put("retmsg", "错误：获取prepayId失败");
			}
		} else {
			outParams.put("retcode", "-1");
			outParams.put("retmsg", "错误：获取不到Token");
		}
		
		return outParams;
	}
		
	/**
	 *  App微信支付（非公众号支付）
	 * @param appId 微信开放平台的APP应用ID
	 * @param mchId APP商户号
	 * @param apiKey APP商户号密钥
	 * @param body 商品描述
	 * @param totalFee 订单总金额，只能为整数，单位分
	 * @param outTradeNo 商户订单号
	 * @param spbill_create_ip 终端IP
	 * @param notifyUrl 回调URL
	 * @param tradeType 交易类型取值如下：JSAPI，NATIVE，APP，WAP
	 * @return
	 * @throws Exception
	 */
	public static TreeMap<String, String> getAppWxPay(String appId,String mchId,String apiKey,String body,
			String totalFee, String outTradeNo,String spbillCreateIp,String notifyUrl,String tradeType)
			throws Exception {
		TreeMap<String, String> outParams = new TreeMap<String, String>();
		String noncestr = Sha1Util.getNonceStr();
		String timestamp = Sha1Util.getTimeStamp();
		String requestXML = PayService.createWxPayXml(appId,mchId,apiKey,body, totalFee,
				outTradeNo, spbillCreateIp,notifyUrl,Sha1Util.getNonceStr(),tradeType, null);
		String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL,
				"POST", requestXML);
		Map<String, String> resultMap = XMLUtil.doXMLParse(result);
		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if(return_code.equals("SUCCESS")){
			String result_code = resultMap.get("result_code");
			String err_code = resultMap.get("err_code");
			String err_code_des = resultMap.get("err_code_des");
			if(result_code.equals("SUCCESS")){
				String prepayid = resultMap.get("prepay_id");	
				if (null != prepayid && !"".equals(prepayid)) {
					SortedMap<String, String> prePayParams = new TreeMap<String, String>();
					prePayParams.put("appid", resultMap.get("appid"));
					prePayParams.put("partnerid", resultMap.get("mch_id"));
					prePayParams.put("prepayid", prepayid);
					prePayParams.put("package", "Sign=WXPay");
					prePayParams.put("noncestr", noncestr);
					prePayParams.put("timestamp", timestamp);
					String sign = PayCommonUtil.createSign("UTF-8", prePayParams,apiKey);
					outParams.put("appid", resultMap.get("appid"));
					outParams.put("partnerid", resultMap.get("mch_id"));
					outParams.put("prepayid", prepayid);
					outParams.put("package", "Sign=WXPay");
					outParams.put("noncestr", noncestr);
					outParams.put("timestamp", timestamp);
					outParams.put("sign", sign);
				}
			}
			outParams.put("result_code", result_code);
			outParams.put("err_code", err_code);
			outParams.put("err_code_des", err_code_des);
		}
		outParams.put("return_code", return_code);
		outParams.put("return_msg", return_msg);
		return outParams;
	}
	
	/**
	 * 申请微信退款
	 * 
	 * @param transaction_id 微信订单号
	 * @param out_trade_no 商户订单号
	 * @param out_refund_no 商户退款单号
	 * @param total_fee 订单总金额，单位为分，只能为整数
	 * @param refund_fee 退款总金额，订单总金额，单位为分，只能为整数
	 * @return
	 */
	public static WeChatRefundVo weChatRefund(String transaction_id, String out_trade_no,String out_refund_no,String total_fee,String refund_fee){
		logger.info("微信订单号:{}, 商户订单号:{}, 商户退款单号:{}, 订单总金额:{}分, 退款总金额:{}分", transaction_id, out_trade_no, out_refund_no, total_fee, refund_fee);
		WeChatRefundVo weChatRefundVo = new WeChatRefundVo();
		String requestXML =PayService.createRefundJSPackage(transaction_id, out_trade_no, out_refund_no, total_fee, refund_fee); 
		logger.info("requestXML for transaction_id {} :\n{}", transaction_id, requestXML);
		try {
			String s= ClientCustomSSL.doRefund(ConfigUtil.REFUND_URL, requestXML);
			logger.info("微信退款返回信息：\n{}", s);
			Map<String, String> resultMap = XMLUtil.doXMLParse( s );
			String return_code = resultMap.get("return_code");
			String return_msg = resultMap.get("return_msg");
			weChatRefundVo.setReturn_code(return_code);
			weChatRefundVo.setReturn_msg(return_msg);
			if(return_code.equals("SUCCESS")){
				String result_code = resultMap.get("result_code");
				String err_code = resultMap.get("err_code");
				String err_code_des = resultMap.get("err_code_des");
				String appid = resultMap.get("appid");
				String mch_id = resultMap.get("mch_id");
				String device_info = resultMap.get("device_info");
				String nonce_str = resultMap.get("nonce_str");
				String sign = resultMap.get("sign");
				String refund_id = resultMap.get("refund_id");
				String refund_channel = resultMap.get("refund_channel");
				String fee_type = resultMap.get("fee_type");
				String cash_fee = resultMap.get("cash_fee");
				String cash_refund_fee = resultMap.get("cash_refund_fee");
				String coupon_refund_fee = resultMap.get("coupon_refund_fee");
				String coupon_refund_count = resultMap.get("coupon_refund_count");
				String coupon_refund_id = resultMap.get("coupon_refund_id");
				weChatRefundVo.setResult_code(result_code);
				weChatRefundVo.setErr_code(err_code);
				weChatRefundVo.setErr_code_des(err_code_des);
				weChatRefundVo.setAppid(appid);
				weChatRefundVo.setMch_id(mch_id);
				weChatRefundVo.setDevice_info(device_info);
				weChatRefundVo.setNonce_str(nonce_str);
				weChatRefundVo.setSign(sign);
				weChatRefundVo.setTransaction_id(transaction_id);
				weChatRefundVo.setOut_trade_no(out_trade_no);
				weChatRefundVo.setOut_refund_no(out_refund_no);
				weChatRefundVo.setRefund_id(refund_id);
				weChatRefundVo.setRefund_channel(refund_channel);
				weChatRefundVo.setFee_type(fee_type);
				weChatRefundVo.setCash_fee(NumberUtils.toInt(cash_fee));
				weChatRefundVo.setCash_refund_fee(NumberUtils.toInt(cash_refund_fee));
				weChatRefundVo.setCoupon_refund_fee(NumberUtils.toInt(coupon_refund_fee));
				weChatRefundVo.setCoupon_refund_count(NumberUtils.toInt(coupon_refund_count));
				weChatRefundVo.setCoupon_refund_id(coupon_refund_id);
				weChatRefundVo.setRefund_fee(Integer.parseInt(refund_fee));
				weChatRefundVo.setTotal_fee(Integer.parseInt(total_fee));
			} else {
				logger.error("微信退款返回失败~！return_code={}", return_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信退款失败~！", e);
		}
		return weChatRefundVo;
		
	}
	
}
