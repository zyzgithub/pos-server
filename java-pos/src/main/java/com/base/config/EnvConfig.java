package com.base.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * 配置方式 
 * classpath:/properties/${spring.profiles.active}.properties
 * 配置(web.xml配置spring.profiles.active) 
 * spring.profiles.active = devel(开发)/test(测试)/product(生产)
 * </pre>
 * 
 * @author folo
 */
@Configuration
public class EnvConfig {

	@Autowired
	private BaseConfig baseConfig;

	@Autowired
	private WechatConfig wechatConfig;
	
	@Autowired
	private AlipayConfig alipayConfig;
	
	@Autowired
	private MqConfig mqConfig;
	
	@Autowired
	private UcfConfig ucfConfig;
	

	/** 基础配置 */
	public static BaseConfig base;

	/** 微信配置 */
	public static WechatConfig wechat;
	
	/** 支付宝配置 */
	public static AlipayConfig alipay;
	
	/** MQ配置 */
	public static MqConfig mq;
	
	/** 先锋配置 */
	public static UcfConfig ucf;

	@PostConstruct
	public void init() {
		EnvConfig.base = baseConfig;
		EnvConfig.wechat = wechatConfig;
		EnvConfig.alipay = alipayConfig;
		EnvConfig.mq = mqConfig;
		EnvConfig.ucf = ucfConfig;
	}
}
