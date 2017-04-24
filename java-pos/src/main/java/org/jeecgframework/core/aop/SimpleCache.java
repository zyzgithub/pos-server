package org.jeecgframework.core.aop;

import java.util.HashMap;

public final class SimpleCache{
	 private static HashMap<String,Object> cacheMap;
	 private static SimpleCache simpleCache;

	 private SimpleCache(){
		 
	 }
	 
	 public static SimpleCache getInstance(){ 
        if(simpleCache == null) 
        { 
        	simpleCache = new SimpleCache(); 
        } 
        return simpleCache; 
    }
	 
	 public synchronized void saveCache(String key, Object value){
        cacheMap=new HashMap<String,Object>();
        cacheMap.put(key, value);
    } 
	 
	public synchronized HashMap<String,Object> getCache(String key, Object value){  
		return cacheMap;
	}
	
	public synchronized void clearCache(){
		if(cacheMap != null && cacheMap.size() > 0){
			cacheMap.clear();
		}
	}
	 
	public static HashMap<String, Object> getCacheMap() {
		return cacheMap;
	}

	public static void setCacheMap(HashMap<String, Object> cacheMap) {
		SimpleCache.cacheMap = cacheMap;
	}

	public static SimpleCache getSimpleCache() {
		return simpleCache;
	}

	public static void setSimpleCache(SimpleCache simpleCache) {
		SimpleCache.simpleCache = simpleCache;
	}
}
