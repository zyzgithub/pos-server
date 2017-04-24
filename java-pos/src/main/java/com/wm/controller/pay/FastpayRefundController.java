package com.wm.controller.pay;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.refund.nopwd.NopwdFastpayRefundAction;
import com.alipay.refund.nopwd.NopwdFastpayRefundAsynchronousNotify;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wm.util.JacksonUtil;

@Controller
@RequestMapping("fastpayRefundController")
public class FastpayRefundController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FastpayRefundController.class);

	/**
	 * 异步通知校验通过并处理完业务逻辑时应该返回的字符串
	 */
	private static final String SUCCESS_NOTIFY_RESP = "SUCCESS";

	/**
	 * 异步通知校验失败或业务逻辑失败时应该返回的字符串，支付宝会在一定时间后重新进行异步通知
	 */
	private static final String FAIL_NOTIFY_RESP = "FAIL";

	@Resource
	private NopwdFastpayRefundAction nopwdFastpayRefundAction;

	/**
	 * 即时到账批量退款无密接口-异步通知<br>
	 * 参考官方文档:<br>
	 * <a>http://wiki.0085.com:8090/pages/viewpage.action?pageId=2719776</a>
	 * @param notify 异步通知参数
	 * @return 成功或失败的响应
	 */
	@RequestMapping("/nopwdRefundNotify")
	@ResponseBody
	public String nopwdRefundNotify(NopwdFastpayRefundAsynchronousNotify notify) {
		logNopwdFastpayRefundAsynchronousNotify(notify);
		if (nopwdFastpayRefundAction.verifyAsynchronousNotify(notify)) {
			LOGGER.info("verify nopwdRefundNotify success");
			// 根据返回结果解析每笔交易是否退款成功，并视情况修改数据库状态。如：
			// {@link com.wm.entity.orderrefund.OrderRefundEntity}，{@link com.wm.entity.orderrefund.OrderEntity}等。
			// 因为即时到账批量退款接口的流程与微信支付退款的流程不一致，目前采用乐观策略，认定退款请求成功即视为退款业务成功。
			// 此处的异步通知只是做记录，看情况可修改此处逻辑
			return SUCCESS_NOTIFY_RESP;
		}
		return FAIL_NOTIFY_RESP;
	}

	private void logNopwdFastpayRefundAsynchronousNotify(NopwdFastpayRefundAsynchronousNotify notify) {
		try {
			LOGGER.info("receive NopwdFastpayRefundAsynchronousNotify: {}", JacksonUtil.writeValueAsString(notify));
		} catch (JsonProcessingException e) {
			LOGGER.info("", e);
		}
	}
	
}
