package com.wm.service.impl.order;

/**
 * 订单退款失败异常，抛出此异常说明相关的退款操作失败
 */
public class OrderRefundFailException extends Exception {

	private static final long serialVersionUID = -9017320388597251247L;

	public OrderRefundFailException() {
		super();
	}

	public OrderRefundFailException(String message) {
		super(message);
	}

	public OrderRefundFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderRefundFailException(Throwable cause) {
		super(cause);
	}

}
