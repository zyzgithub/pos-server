package org.jeecgframework.core.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import jeecg.system.service.SystemService;


import com.log.entity.OperateLog;

public class OperateLogContext {
	private static Hashtable<String,OperateLog> operateLogVOMap = new Hashtable<String,OperateLog>();
	
	@SuppressWarnings("unused")
	private static SystemService  systemService = null;

	
	@SuppressWarnings("rawtypes")
	public static  void putOperateVO(String operateLogId, OperateLog operateLog, SystemService  systemService) {
		if(operateLogVOMap.size() > 50){
			synchronized(operateLogVOMap){
				Set set = operateLogVOMap.keySet();
				Iterator it = set.iterator();
				while(it.hasNext()){
					String key = (String) it.next();
					OperateLog vo = (OperateLog) operateLogVOMap.get(key);

					try{
						systemService.save(vo);
					}catch(Exception ex){
						ex.printStackTrace();
						operateLogVOMap.clear();
					}
				}
				operateLogVOMap.clear();
			}
		}
		operateLogVOMap.put(operateLogId, operateLog);
	}
}
