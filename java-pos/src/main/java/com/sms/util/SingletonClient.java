package com.sms.util;


import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SingletonClient {
	
	private final static Logger logger = LoggerFactory.getLogger(SingletonClient.class);
	
	private static Client verifyClient=null;
	private static Client marketingClient=null;
	
	@Value("${key_verify}")
	private static String smsKeyVerify;
	
	@Value("${softwareSerialNo_verify}")
	private static String softwareSerialNoVerify;
	
	@Value("${key_marketing}")
	private static String keyMarketing;
	
	
	private SingletonClient(){
	}
	
	
	public synchronized static Client getVerifyClient(String softwareSerialNo,String key){
		if(verifyClient==null){
			try {
				verifyClient=new Client(softwareSerialNo,key);
				try {
					int i = SingletonClient.getVerifyClient().registEx(smsKeyVerify);
					logger.info("验证短信注册：{}", i);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return verifyClient;
	}
	
	public synchronized static Client getVerifyClient(){
		if(verifyClient==null){
			try {
				verifyClient=new Client(softwareSerialNoVerify, smsKeyVerify);
				try {
					int i = SingletonClient.getVerifyClient().registEx(smsKeyVerify);
					logger.info("验证短信注册：{}", i);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return verifyClient;
	}

	public synchronized static Client getMarketingClient(String softwareSerialNo,String key){
		if(marketingClient==null){
			try {
				marketingClient=new Client(softwareSerialNo,key);
				try {
					int i = SingletonClient.getMarketingClient().registEx(keyMarketing);
					logger.info("营销短信注册：{}", i);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return marketingClient;
	}
	
	public synchronized static Client getMarketingClient(){
		if(marketingClient==null){
			try {
				marketingClient=new Client(softwareSerialNoVerify, keyMarketing);
				try {
					int i = SingletonClient.getMarketingClient().registEx(keyMarketing);
					logger.info("营销短信注册：{}", i);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return marketingClient;
	}
	
}
