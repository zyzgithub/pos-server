package com.alipay.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.jeecgframework.core.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.config.EnvConfig;

public class AlipayService {
	
	private static final Logger logger = LoggerFactory.getLogger(AlipayService.class);
	
	/**
	 * create the order info. 创建订单信息
	 * @throws UnsupportedEncodingException
	 */
	public static String getOrderInfo(String subject, String body, String price, String orderNo) throws UnsupportedEncodingException {
		return getOrderInfo(subject, body, price, orderNo, EnvConfig.alipay.notifyUrl);
	}
	
	public static String getOrderInfo(String subject, String body, String price, String orderNo, String notifyUrl) throws UnsupportedEncodingException {
		logger.info("开始拼接orderInfo: partner(配置的数据)={}, seller_id(配置的数据)={}, rsaPrivate(配置的数据)={}, body={}, total_fee={}, notify_url={}, out_trade_no={}"
				, EnvConfig.alipay.partner, EnvConfig.alipay.seller, EnvConfig.alipay.rsaPrivate, body, price, notifyUrl, orderNo);
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + EnvConfig.alipay.partner + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + EnvConfig.alipay.seller + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		orderInfo += "&sign=\"" + URLEncoder.encode(sign(orderInfo), "UTF-8") + "\"&" + getSignType();

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";
		
		// 暂停使用光曦支付宝账号20170111 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!        ////////////////////////////////
//		orderInfo = "";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * @param content 待签名订单信息
	 */
	public static String sign(String content) {
		logger.info("rsaPrivate={}", EnvConfig.alipay.rsaPrivate);
		return SignUtils.sign(content, EnvConfig.alipay.rsaPrivate);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

}