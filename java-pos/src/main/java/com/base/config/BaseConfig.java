package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 基础配置
 */
@Configuration
public class BaseConfig {

	// 1.5项目域名
	@Value("${domain15}")
	public String DOMAIN;

	// 1.8项目域名
	@Value("${domain18}")
	public String DOMAIN18;
	
	// 支付域(微富通)
	@Value("${domain.pay}")
	public String DOMAIN_PAY;
	
	// 供应链通知地址
	@Value("${supply_erp_host}")
	public String SUPPLY_ERP_HOST;

	// 供应链通知地址
	@Value("${supply_mc_host}")
	public String SUPPLY_MC_HOST;

	// 是否调试模式
	@Value("${test.istest}")
	public Boolean ISTEST;

	// 微信测试账号
	@Value("${test.userid}")
	public String TEST_USERID;
	
	// 是否调试模式-第三方对接
	@Value("${test.istest.third}")
	public Boolean ISTEST_THIRD;

	// 欢迎语
	@Value("${welcome.title}")
	public String WELCOME_TITLE;

	// 缓存IP地址
	@Value("${memcached_host}")
	public String MEMCACHED_HOST;

	// 缓存端口
	@Value("${memcached_port}")
	public String MEMCACHED_PORT;

	// 开放平台主机
	@Value("${openapi_hxkj_host}")
	public String OPENAPI_HXKJ_PORT;

	// 退款证书文件名
	@Value("${apiclient_cert_file}")
	public String APICLIENT_CERT_FILE;
	
	
	@Value("${session.interval}") 
	public String sessionTimeout;
	
	//用户端域名
	@Value("${user_app}")
	public String USER_APP;

}
