package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * 先锋支付配置
 */
@Configuration
public class UcfConfig {

	// 先锋支付分配的商户号
	@Value("${merId}")
	public String merId;

	// 商户号密钥
	@Value("${merRSAKey}")
	public String merRSAKey;
	
	// 接口地址
	@Value("${gateway}")
	public String gateway;

	
}
