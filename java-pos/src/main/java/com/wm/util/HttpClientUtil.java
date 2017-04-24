package com.wm.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient工具类
 * 
 * @author 王喜文
 * @since 2016-3-15
 */
public class HttpClientUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final CloseableHttpClient HTTP_CLIENT;

	static {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(2);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10000)
				.setConnectTimeout(30000).setSocketTimeout(60000).build();
		HTTP_CLIENT = HttpClients.custom().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig).build();
	}
	
	/**
	 * 执行http请求, 将response的content(按指定charset读取)转化成期望的class并返回
	 * @param request HttpUriRequest
	 * @param c 期望返回的class, 必须是可反序列化的类型
	 * @param charset 读取response的content的编码
	 * @return 期望返回的类型对象
	 * @throws IOException 
	 */
	public static <T> T execute(HttpUriRequest request, Class<? extends T> c, String charset) throws IOException {
		T result = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = HTTP_CLIENT.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String entityString = EntityUtils.toString(entity, charset);
				LOGGER.info("entityString: {}", entityString);
				result = convert(entityString, c);
				EntityUtils.consume(entity);
			} else {
				LOGGER.info("statusCode: {}", statusCode);
			}
		} catch (IOException e) {
			LOGGER.error("http request fail", e);
			request.abort();
			throw e;
		}
		return result;
	}

	/**
	 * 执行http请求, 将response的content转化成期望的class并返回
	 * @param request HttpUriRequest
	 * @param c 期望返回的class, 必须是可反序列化的类型
	 * @return 期望返回的类型对象
	 * @throws IOException 
	 */
	public static <T> T execute(HttpUriRequest request, Class<? extends T> c) throws IOException {
		return execute(request, c, null);
	}

	/**
	 * 执行http请求, 将response的content(按指定charset读取)作为字符串返回
	 * @param request HttpUriRequest
	 * @param charset 读取response的content的编码
	 * @return content字符串
	 * @throws IOException
	 */
	public static String execute(HttpUriRequest request, String charset) throws IOException {
		return execute(request, String.class, charset);
	}

	/**
	 * 执行http请求, 将response的content作为字符串返回
	 * @param request HttpUriRequest
	 * @return content字符串
	 * @throws IOException
	 */
	public static String execute(HttpUriRequest request) throws IOException {
		return execute(request, String.class);
	}

	/**
	 * 将字符串转化为期望的类型
	 * @param string
	 * @param c 期望的类型class
	 * @return 期望的类型对象
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convert(String string, Class<? extends T> c) {
		T result = null;
		if (c == String.class) {
			result = (T) string;
		} else {
			try {
				result = JacksonUtil.readValue(string, c);
			} catch (Exception e) {
				LOGGER.error("can not deserialize entityString to class " + c.getName(), e);
			}
		}
		return result;
	}

}
