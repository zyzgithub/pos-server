package com.base.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
	
	@Value("${redis_server}")
	public String redisHost;
	
	@Value("${redis_port}")
	public int redisPort;
	
	@Value("${redis_password}")
	public String password;
	
	@Value("${redis_db}")
	public int redisDatabase;
	
	public static String host;
	public static int port;
	public static String auth;
	public static int database;
	
	@PostConstruct
	public void init() {
		host = this.redisHost;
		port = this.redisPort;
		auth = this.password;
		database = this.redisDatabase;
	}
}
