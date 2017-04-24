package com.wm.util;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisDistributedLock implements IDistributedLock {

	private Jedis conn;

	public RedisDistributedLock(Jedis conn) {
		this.conn = conn;
	}

	@Override
	public String tryAcquireLock(String lockName, int expire, int tryTime) {
		String identifier = UUID.randomUUID().toString();
		String lockKey = "lock:" + lockName;

		long end = System.currentTimeMillis() + tryTime * 1000;
		while (System.currentTimeMillis() < end) {
			if (conn.setnx(lockKey, identifier) == 1) {
				conn.expire(lockKey, expire);
				return identifier;
			}
			if (conn.ttl(lockKey) == -1) {
				conn.expire(lockKey, expire);
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		// null indicates that the lock was not acquired
		return null;
	}

	@Override
	public boolean releaseLock(String lockName, String uuid) {
		String lockKey = "lock:" + lockName;

		while (true) {
			conn.watch(lockKey);
			if (uuid.equals(conn.get(lockKey))) {
				Transaction trans = conn.multi();
				trans.del(lockKey);
				List<Object> results = trans.exec();
				if (results == null) {
					continue;
				}
				return true;
			}

			conn.unwatch();
			break;
		}
		return false;
	}
}
