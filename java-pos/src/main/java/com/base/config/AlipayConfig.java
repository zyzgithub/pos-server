package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */
@Configuration
public class AlipayConfig {

	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	@Value("${PARTNER}")
	public String partner;

	@Value("${SELLER}")
	public String seller;

	@Value("${NOTIFY_URL}")
	public String notifyUrl;

	// 商户的私钥
	@Value("${RSA_PRIVATE}")
	public String rsaPrivate;
	
	// 即时到账批量退款无密接口异步通知URL
	@Value("${NOPWD_FASTPAY_REFUND_NOTIFY_URL}")
	public String nopwdFastpayRefundNotifyUrl;

	// description 手机支付时使用,手机端使用了RSA进行加密
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMmX/dNiDuBe6byC3o6akA2afq2SZrrC16YkjRnFOSj8+7MrdG31uAprGAxLYvinGpYR3QRJp2kxRzdiSg7NgHUHkjG0JU5XRXf0LYMvB7R5KqmuxdjR9EhCeX8zdW56AruoqKgcoidOyTEVNoN7Gs/8p1wEO2/TlvB99Io3Ur9QIDAQAB";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 签名方式 不需修改
	public static String sign_type = "RSA";

 	@Value("${face_to_face_ali_pid}")
	public String face_to_face_ali_pid;
	@Value("${face_to_face_ali_appid}")
	public String face_to_face_ali_appid;
	@Value("${face_to_face_ali_public_key}")
	public String face_to_face_ali_public_key;
	@Value("${face_to_face_app_private_key}")
	public String face_to_face_app_private_key;


}
