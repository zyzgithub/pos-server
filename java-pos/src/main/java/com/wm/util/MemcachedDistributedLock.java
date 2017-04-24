package com.wm.util;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;

import net.spy.memcached.MemcachedClient;

public class MemcachedDistributedLock implements IDistributedLock {

	@Override
	public String tryAcquireLock(String lockName, int expire, int tryTime) {
		MemcachedClient client = AliOcs.getClient();
		
		String uuid = UUID.randomUUID().toString();
		
		long time = System.currentTimeMillis() + tryTime*1000;
		while (System.currentTimeMillis() < time) {
			
			try {
				if(client.add(lockName, expire, uuid).get()){
					return uuid;
				}
				Thread.sleep(500);
			} 
			catch (InterruptedException e) {
				Thread.interrupted();
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean releaseLock(String lockName, String uuid) {
		MemcachedClient client = AliOcs.getClient();
		String value = (String)client.get(lockName);
		
		if(StringUtils.equals(uuid, value)){
			try {
				return client.delete(lockName).get();
			} 
			catch (InterruptedException e) {
				Thread.interrupted();
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
