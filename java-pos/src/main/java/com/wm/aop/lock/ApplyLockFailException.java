package com.wm.aop.lock;

/**
 * 申请锁失败异常，当申请锁失败的时候抛出
 */
public class ApplyLockFailException extends RuntimeException {

	private static final long serialVersionUID = -7800595395092585374L;
	
	public ApplyLockFailException() {
		super();
	}
	
	public ApplyLockFailException(String message) {
		super(message);
	}
	
	public ApplyLockFailException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ApplyLockFailException(Throwable cause) {
		super(cause);
	}

}
