package com.base.util;

import java.util.Random;

 /**
  * @author zhenjunzhuo
  * SessionKey生成器
  */

public class SessionKeyGenerator {
	private static Random random = new Random();
	
	//生产机制 当前时间 + 随即数
	public static String getSessionKey(){
		StringBuffer sessionKey = new StringBuffer("");
		int rand = random.nextInt();
		sessionKey.append(new Long(System.currentTimeMillis()).toString()).append(new Integer(rand).toString().replace("-", ""));
		return sessionKey.toString();
	}
}

