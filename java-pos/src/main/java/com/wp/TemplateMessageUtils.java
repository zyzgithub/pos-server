package com.wp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.base.config.EnvConfig;

public class TemplateMessageUtils {
	private static final Logger logger = Logger.getLogger(TemplateMessageUtils.class);
	
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("连接超时：{}", ce);
		} catch (Exception e) {
			logger.error("https请求异常：{}", e);
		}
		return jsonObject;
	}
	

	/**
	 * 发送模版消息到微信公众号
	 * @param openId 用户的openId
	 * @param m  数据模版
	 */
	public static void sendTemplateMessage(String openId, String url, Map<String,TemplateData> m){		
		logger.info("==========发送模版消息到微信公众号==========");
		WxTemplate t = new WxTemplate();
		t.setUrl(url); //调转URL
		t.setTouser(openId);
		t.setTemplate_id(EnvConfig.wechat.TEMPLATE_ID_PAY_DONE);  //模版ID
		t.setTopcolor("green");		//顶部颜色
		t.setData(m);
		
		String accessToken = AccessTokenContext.getAccessToken();
		logger.info("********AccessToken:" + accessToken);
		JSONObject jsonObject = httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, "POST",JSONObject.fromObject(t).toString());
		logger.info("********微信返回推送JSON:" + jsonObject);
		if(null != jsonObject){
			if(!"0".equals(jsonObject.get("errcode").toString())){
				// {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest hint: [n2h6CA0988age8]"}
				logger.warn("发送微信消息模板失败：" + EnvConfig.wechat.TEMPLATE_ID_PAY_DONE + "," + jsonObject.get("errmsg").toString());
			} else {
				// {"errcode":0,"errmsg":"ok","msgid":402847405}
			}
		} else {
			logger.error("发送微信消息模板失败：链接超时或者https请求异常！");
		}
	}
	
	public static void sendTemplateMessage(String templateId, String openId, String url, Map<String,TemplateData> m){		
		logger.info("==========发送模版消息到微信公众号==========");
		WxTemplate t = new WxTemplate();
		t.setUrl(url); //调转URL
		t.setTouser(openId);
		t.setTemplate_id(templateId);  //模版ID
		t.setTopcolor("green");		//顶部颜色
		
		t.setData(m);
		String accessToken = AccessTokenContext.getAccessToken();
		logger.info("********AccessToken:" + accessToken);
		JSONObject jsonObject = httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, "POST",JSONObject.fromObject(t).toString());
		logger.info("********微信返回推送JSON:" + jsonObject);
		if(null != jsonObject){
			if(!"0".equals(jsonObject.get("errcode").toString())){
				// {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest hint: [n2h6CA0988age8]"}
				logger.warn("发送微信消息模板失败templateId：" + templateId + "," + jsonObject.get("errmsg").toString());
			} else {
				// {"errcode":0,"errmsg":"ok","msgid":402847405}
			}
		} else {
			logger.error("发送微信消息模板失败：链接超时或者https请求异常！");
		}
//		httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + AccessTokenContext.getAccessToken(), "POST",JSONObject.fromObject(t).toString());
	}
	
	/**
	 * 发送客服消息
	 * @param openId
	 * @param order
	 */
	public static void sendCustomMessage(String openId, String title, String content) {		
		JSONObject msgJson = new JSONObject();
		msgJson.put("touser", openId);
		msgJson.put("msgtype", "news");
		
		JSONObject newsJson = new JSONObject();
		JSONArray articlesJson = new JSONArray();
		JSONObject articleJson = new JSONObject();
		JSONObject advJson = null; 
		try {
			advJson = JSONObject.fromObject(WftAdvertisementUtils.getAdBody());
		} catch (JSONException e) {
			logger.error("发送客服消息失败：获取威富通广告素材失败,"+e.getMessage());
			return;
		}
		content += "\n"+advJson.getString("desc");
		articleJson.put("title", title);
		articleJson.put("description", content);
		articleJson.put("url", advJson.getString("url"));
		articleJson.put("picurl", advJson.getString("imgPath"));
		articlesJson.add(articleJson);
		newsJson.put("articles", articlesJson);
		msgJson.put("news", newsJson);
		
		logger.info("==========发送客服消息到微信公众号==========");
		String accessToken = AccessTokenContext.getAccessToken();
		logger.info("********AccessToken:" + accessToken);
		JSONObject jsonObject = httpRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, "POST",msgJson.toString());
		logger.info("********微信返回推送JSON:" + jsonObject);
		if(null != jsonObject){
			if(!"0".equals(jsonObject.get("errcode").toString())){
				logger.warn("发送客服消息失败," + jsonObject.get("errmsg").toString());
			} else {
				// {"errcode":0,"errmsg":"ok","msgid":402847405}
			}
		} else {
			logger.error("发送客服消息失败：链接超时或者https请求异常！");
		}
	}
	
}
