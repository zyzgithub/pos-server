package com.sms.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 【短信发送工具类】
 * 
 * -->当前启用的是：创蓝集团短信接口
 * -->公共调用方法：send()
 * 
 * @author Moshow
 **/
public class SmsUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

//	static String smsServerIp="http://222.73.117.158";//短信服务器
	static String smsServerIp="http://222.73.117.156";//短信服务器 - 备用
	static String account="dianba";//账号
	static String pswd="$2B1oRnUzlLj)xM";//密码
	static String mobile="";//手机号
	static String msg="";//内容
	static String needstatus="true";//状态报告
	
	/**
	 * 【创蓝文化传播】短信接口调用方法
	 * 发送短信
	 * @author Moshow
	 **/
	public static String sendMsg(String msg, String mobile) throws Exception {
		try{
			StringBuffer sb = new StringBuffer(smsServerIp);
//			sb.append("/msg/HttpSendSM?");//单发接口
			sb.append("/msg/HttpBatchSendSM?");//群发接口
			sb.append("&msg=" + URLEncoder.encode(msg, "utf-8"));
			sb.append("&account=" + account);
			sb.append("&pswd=" + pswd);
			sb.append("&mobile=" + mobile);
			sb.append("&needstatus=" + needstatus);
			logger.debug("短信接口发送：{}", sb);
//			http://222.73.117.158/msg/HttpSendSM?&msg=12333&account=&pswd=&mobile=&needstatus=true
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			logger.info("短信接口返回：{}", in.readLine());
			return in.readLine();
		}catch (Exception e){
			logger.error("短信发送失败!", e);
			return null;
		}
	}
	/**
	 * 【创蓝文化传播】短信接口调用方法
	 * 查询额度
	 * @author Moshow
	 **/
	public static String sendQuery() throws Exception {
		try{
			StringBuffer sb = new StringBuffer(smsServerIp);
			sb.append("/msg/QueryBalance?");
			sb.append("&account=" + account);
			sb.append("&pswd=" + pswd);
			logger.info("短信查询额度：{}", sb);
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			logger.info("短信查询额度返回：{}", in.readLine());
			return in.readLine();
		}catch (Exception e){
			logger.error("短信查询额度失败!", e);
			return null;
		}
	}
	/**
	 * -->公共调用方法：send(msg,mobile);
	 * 以后统一调用这个方法
	 * @author Moshow
	 **/
	public static String send(String msg, String mobile) throws Exception {
		return sendMsg(msg,mobile);
	}
	
	public static void main(String[] args) {
		try {
			SmsUtil.send("Hello World", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}