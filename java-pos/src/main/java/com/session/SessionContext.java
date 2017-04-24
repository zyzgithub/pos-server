package com.session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.config.EnvConfig;
import com.wm.util.AliOcs;

public class SessionContext {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
	
	public static void putSessionVO(String sessionKey, SessionVO vo) {
		String sessionInterval = EnvConfig.base.sessionTimeout;
		logger.debug("sessionInterval : {}", sessionInterval);
		if(StringUtils.isEmpty(sessionInterval)){
			sessionInterval = "1800000";
		}
		Long intervalSeconds = Long.parseLong(sessionInterval) / 3600;
		logger.debug("intervalSeconds : {}", intervalSeconds);
		AliOcs.syncSet(sessionKey, vo, intervalSeconds.intValue());
	}

	public static SessionVO getSessionVO(String sessionKey) {
		Object sessionObj = AliOcs.syncGetObject(sessionKey);
		if(sessionObj != null){
			return (SessionVO) sessionObj;
		}
		return null;
	}
	
}
