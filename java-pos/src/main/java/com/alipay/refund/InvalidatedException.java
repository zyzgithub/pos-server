package com.alipay.refund;

/**
 * 未通过{@link com.alipay.refund.Validatable#validate()}校验时可以抛出的异常
 */
public class InvalidatedException extends RuntimeException {

	private static final long serialVersionUID = -6565632601723369980L;

	public InvalidatedException() {
		super();
	}
	
	public InvalidatedException(String message) {
		super(message);
	}
	
	public InvalidatedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidatedException(Throwable cause) {
		super(cause);
	}
	
}
