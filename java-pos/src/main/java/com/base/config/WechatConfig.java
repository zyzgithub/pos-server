package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微信开发者账号配置
 */
@Configuration
public class WechatConfig {

	// 公众号APPID
	@Value("${wechat.appid}")
	public String APPID;

	// 公众号应用密码
	@Value("${wechat.appsecrect}")
	public String APP_SECRECT;

	// 公众号的商户号
	@Value("${wechat.merchantid}")
	public String MCH_ID;
	
	// 公众号APPID
	@Value("${wechat.kfz.appid}")
	public String APPID_KFZ;

	// 公众号应用密码
	@Value("${wechat.kfz.appsecrect}")
	public String APP_SECRECT_KFZ;

	// 公众号的商户号
	@Value("${wechat.kfz.merchantid}")
	public String MCH_ID_KFZ;

	// API密钥，在公众平台上设置好api证书好，自己设置的密码
	@Value("${wechat.kfz.apikey}")
	public String API_KEY;

	// 微信退款PKCS12证书文件
	@Value("${wechat.apicert}")
	public String API_REFUND_CERT;

	// 微信消息推送模板ID-支付完成
	@Value("${wechat.templateid.paydone}")
	public String TEMPLATE_ID_PAY_DONE;

	// 微信消息推送模板ID-堂食订单
	@Value("${wechat.templateid.eatin.order}")
	public String TEMPLATE_ID_EATIN_ORDER;
	
	// 闪购：微信消息推送模板ID- 商家发货
	@Value("${wechat.templateid.flashsale.delivery}")
	public String TEMPLATE_ID_FLASHSALE_DELIVERY;
	
	// 闪购：微信消息推送模板ID- 退款 申请结果
	@Value("${wechat.templateid.flashsale.refundapply}")
	public String TEMPLATE_ID_FLASHSALE_REFUNDAPPLY;
	
	// 闪购：微信消息推送模板ID- 退款/退款退货 申请结果
	@Value("${wechat.templateid.flashsale.returnproductapply}")
	public String TEMPLATE_ID_FLASHSALE_RETURNPRODUCTAPPLY;
	
	// 闪购：微信消息推送模板ID- 退款成功
	@Value("${wechat.templateid.flashsale.refundSuccess}")
	public String TEMPLATE_ID_FLASHSALE_REFUNDSUCCESS;
	
	// 威富通微信公众号广告url
	@Value("${wechat.wft.advurl}")
	public String WFT_ADVURL;
	
	// 威富通微信公众号广告渠道ID
	@Value("${wechat.wft.distributionid}")
	public String WFT_DISTRIBUTIONID;
	
}
