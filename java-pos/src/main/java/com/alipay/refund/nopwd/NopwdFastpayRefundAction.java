package com.alipay.refund.nopwd;

import com.alipay.refund.FastpayRefundParam;

/**
 * 支付宝即时到账批量退款无密接口（适用于移动支付）<br>
 * 参考官方文档:<br>
 * <a>http://wiki.0085.com:8090/pages/viewpage.action?pageId=2719776</a>
 */
public interface NopwdFastpayRefundAction {

	/**
	 * 申请即时到账批量退款
	 * @param fastpayRefundParams 即时到账批量退款-请求退款的参数
	 * @return 申请退款结果
	 */
	NopwdFastpayRefundApplyResult apply(FastpayRefundParam... fastpayRefundParams);
	
	/**
	 * 校验即时到账批量退款异步响应是否合法（校验notify_id和sign）
	 * @param notify 即时到账批量退款异步通知对象
	 * @return 是否通过校验
	 */
	boolean verifyAsynchronousNotify(NopwdFastpayRefundAsynchronousNotify notify);
	
}
