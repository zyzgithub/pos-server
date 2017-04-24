package com.wm.controller.courier;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

	public static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();
	 
	public static void put(String key, Session session){
		clients.put(key, session);
	}
	 
	public static Session get(String key){
		 return clients.get(key);
	}
	 
	public static void remove(String key){
		clients.remove(key);
	}
	 
	/**
	 * 判断是否有连接
	 * @param relationId
	 * @param userCode
	 * @return
	*/
	public static boolean hasConnection(String key) {
		return clients.containsKey(key);
	} 
	 
	public static void broadcast(String message) {
		if(clients.isEmpty()) return; 
		Set<String> keys = clients.keySet();
		for(String key : keys){
			try {
				get(key).getAsyncRemote().sendText(message);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("websocket broadcast fail !!! key=" + key + ",message=" + message);
			}
		}
		  			 
	}
}
