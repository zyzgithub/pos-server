package com.validate.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionThread extends Thread{
	private HttpServletRequest request = null;
	
	public SessionThread(HttpServletRequest request){
		this.request = request;  
	}
	
	public void run(){
		try {
			Thread.sleep(1000 * 60 * 10);
			HttpSession session = this.request.getSession();
			if(session != null){
				session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
