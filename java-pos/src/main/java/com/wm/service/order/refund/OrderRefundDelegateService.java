package com.wm.service.order.refund;

/**
 * 订单退款委托类
 */
public interface OrderRefundDelegateService {

	/**
	 * 根据输入订单返回退款执行对象
	 * @param payType 支付类型
	 * @return 退款执行类对象
	 */
	OrderRefundExecutor getExecutor(String payType);

}
