package com.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * MQ配置
 */
@Configuration
public class MqConfig {

	// topic
	@Value("${Topic_MSG}")
	public String TopicMsg;
	@Value("${Topic_PRINT}")
	public String TopicPrint;
	

}
