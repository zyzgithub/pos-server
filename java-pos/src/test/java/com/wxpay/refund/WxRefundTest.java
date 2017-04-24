package com.wxpay.refund;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.util.JacksonUtil;

/**
 * 微信app支付退款官方文档：<a>https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_4&index=6</a>
 */
public class WxRefundTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(WxRefundTest.class);

	public static void main(String[] args) throws Exception {
		// 生产环境2016-03-02 11:56:11,608参数
		// String refundXml = new RefundXmlBuilder("1", "3300",
		// "1001870720201603023663869896").getRefundXml();

		// 王蕴衡提供2016-3-22测试服务器参数
		String refundXml = new WxRefundXmlBuilder("100", "100", "4002002001201603224181319518").getRefundXml();

		String responseString = new WxRefundSender().send(refundXml);
		LOGGER.info("responseString:\n{}", responseString);

		WxRefundResponse refundResponse = new WxRefundResponseParser().parse(responseString);
		Map<String, String> map = BeanUtils.describe(refundResponse);
		LOGGER.info("response to map: {}", map);
		LOGGER.info("RefundResponse: {}", JacksonUtil.writeValueAsString(refundResponse));

		boolean validate = new WxRefundResponseValidator().validate(refundResponse);
		LOGGER.info("validate sign: {}", validate);
	}

}