package com.alipay.refund;

/**
 * 可校验的接口
 */
public interface Validatable {

	/**
	 * 进行校验
	 * @return 是否通过校验
	 */
	boolean validate();

}
