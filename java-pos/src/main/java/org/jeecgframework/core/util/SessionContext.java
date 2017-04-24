package org.jeecgframework.core.util;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.base.entity.SessionKey;


/**
 * 
 * 
 * @author ber
 * 
 * 
 */
public class SessionContext {
	@SuppressWarnings("rawtypes")
	private static Hashtable sessionKeyVOMap = new Hashtable();
	@SuppressWarnings("rawtypes")
	private static Hashtable sessionKeyTimeMap = new Hashtable();

	static {
		new Thread(new Runnable() {
			@SuppressWarnings({ "unused", "rawtypes", "static-access" })
			public void run() {
				while (true) {
					long now = System.currentTimeMillis();
					long interval = 30 * 60 * 1000;
					Long intStr = 600000L;


					Set set = sessionKeyTimeMap.keySet();
					Iterator it = set.iterator();

					while (it.hasNext()) {
						String key = (String) it.next();
						Date t = (Date) sessionKeyTimeMap.get(key);
						long value = t.getTime();
						if (now - value > interval) {// 则从map中清除掉
							sessionKeyVOMap.remove(key);
							sessionKeyTimeMap.remove(key);
						}
					}

					try {
						Thread.currentThread().sleep(60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public static boolean isValidSession(String sessionKey) {
		Date date = (Date) sessionKeyTimeMap.get(sessionKey);
		if (date == null) {
			return false;
		}
		long time = date.getTime();
		long now = System.currentTimeMillis();

		if (now - time > 1000 * 60 * 30) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static void updateSession(String sessionKey) {
		sessionKeyTimeMap.put(sessionKey, new Date());
		// sessionKeyVOMap.put(sessionKey, new Date());
	}

	public static SessionKey getSessionKey(String sessionKey) {
		return (SessionKey) sessionKeyVOMap.get(sessionKey);
	}

	@SuppressWarnings("unchecked")
	public static void putSessionVO(String sessionKey, SessionKey vo) {
		sessionKeyVOMap.put(sessionKey, vo);
		sessionKeyTimeMap.put(sessionKey, new Date());
	}

	public static void remove(String sessionKey) {
		sessionKeyVOMap.remove(sessionKey);
		sessionKeyTimeMap.remove(sessionKey);
	}

}
