package com.wm.util;

/**
 * 
 * 分布式锁
 *
 */
public interface IDistributedLock {
	
	/**
	 * 获取锁
	 * @param lockName 锁名
	 * @param expire 过期时间(单位：秒)
	 * @param tryTime 尝试时间(单位：秒)
	 * @return uuid
	 */
	String tryAcquireLock(String lockName, int expire, int tryTime);
	
	/**
	 * 释放锁
	 * @param lockName 锁名
	 * @param uuid 
	 * @return
	 */
	boolean releaseLock(String lockName, String uuid);
}
