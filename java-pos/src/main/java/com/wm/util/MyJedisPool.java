package com.wm.util;


import com.base.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MyJedisPool {
	private static MyJedisPool intstance = null;
	private static JedisPool pool = null;
	
	private MyJedisPool(){
		try {
			JedisPoolConfig config = new JedisPoolConfig();  
			config.setMaxIdle(10);
			config.setMaxWaitMillis(2000);
			config.setTestOnBorrow(false);
			config.setTestOnReturn(false);
			config.setTestWhileIdle(true);
			config.setTimeBetweenEvictionRunsMillis(30000);
			config.setNumTestsPerEvictionRun(10);
			config.setMinEvictableIdleTimeMillis(60000);
			pool = new JedisPool(config, RedisConfig.host, RedisConfig.port, 2000);
	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized MyJedisPool getInstance(){
		if(intstance == null){
			intstance = new MyJedisPool();
		}
		return intstance;
	}
	public Jedis getJedis(){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(RedisConfig.database);
		} catch (Exception e) {
			e.printStackTrace();
			pool.returnBrokenResource(jedis);
		}
		return jedis;
	}
	
	public Jedis getJedis(String password) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(password);
			jedis.select(RedisConfig.database);
			
		} catch (Exception e) {
			e.printStackTrace();
			pool.returnBrokenResource(jedis);
		}
		return jedis;
	}
	
	public void close(Jedis jedis){
		if(jedis != null){
			pool.returnResource(jedis);
		}
	}
	
	public JedisPool getPool(){
		return pool;
	}
	
}
