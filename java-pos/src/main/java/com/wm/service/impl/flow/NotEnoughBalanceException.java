package com.wm.service.impl.flow;

/**
 * 余额不足异常，抛出此异常说明相关的操作涉及到的余额不足
 */
public class NotEnoughBalanceException extends Exception {

	private static final long serialVersionUID = 7581632208842879848L;

	public NotEnoughBalanceException() {
		super();
	}
	
	public NotEnoughBalanceException(String message) {
		super(message);
	}
	
	public NotEnoughBalanceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NotEnoughBalanceException(Throwable cause) {
		super(cause);
	}
	
}
