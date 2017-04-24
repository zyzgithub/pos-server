package com.alipay.refund;

import java.util.Map;

/**
 * 可签名的接口
 */
public interface Signable {

	/**
	 * 进行签名，返回包含签名在内的参数map
	 * @return 包含签名在内的参数map
	 */
	Map<String, String> sign();
	
}
