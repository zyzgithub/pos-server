package com.courier_mana.remote;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteClient {
	private static Logger logger = LoggerFactory.getLogger(RemoteClient.class);
	private static CloseableHttpClient httpClinet = HttpClients.createDefault();
	
	public static String get(String url){
		HttpGet httpGet = new HttpGet(url);
		logger.info("准备发送GET请求: " + url);
		String responseStr = null;
		try {
			CloseableHttpResponse response = httpClinet.execute(httpGet);
			HttpEntity entity = response.getEntity();
			responseStr = EntityUtils.toString(entity);
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseStr;
	}
}
