package com.wp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.base.config.EnvConfig;

/**
 * 
* @ClassName: WftAdvertisementUtils
* @Description: 威富通微信公众号渠道广告
* @author wxd
* @date 2016年10月18日 下午6:10:02
*
 */
public class WftAdvertisementUtils {

	private static final Logger logger = Logger.getLogger(WftAdvertisementUtils.class);
	
	private static final String WFT_LIST_ADV_KEY = "wft_list_adv_key";
	private static MemcachedClient client;
	
	private WftAdvertisementUtils() {}
	
	static {
		try {
			client  = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT));
		} catch (Exception e) {
			logger.error("初始化Memcached失败：" + EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT, e);
		}
	}
	
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
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
			httpUrlConn.setRequestProperty("distributionid", ConfigUtil.WFT_DISTRIBUTIONID);
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
		} catch (ConnectException ce) {
			logger.error("连接超时：{}", ce);
		} catch (Exception e) {
			logger.error("https请求异常：{}", e);
		}
		return buffer.toString();
	}
	
	/**
	 * 获取下发的广告素材
	 * @return
	 */
	public static String setAdBody() {
		JSONObject json = new JSONObject();
		json.put("request_type", 0);
		String responseJson = httpRequest(ConfigUtil.WFT_ADVURL, "POST", json.toString());
		logger.info("通过威富通接口获取广告：" + responseJson);
		if (StringUtils.isNotBlank(responseJson) && JSONObject.fromObject(responseJson).getBoolean("success")) {
			while(true) {
				CASValue<Object> cas = client.gets(WFT_LIST_ADV_KEY);
				if(null != cas) {
					CASResponse response = client.cas(WFT_LIST_ADV_KEY, cas.getCas(), responseJson);
					if(CASResponse.OK.name().equals(response.name())) {
						logger.info("================reset AdBody：" + cas.getCas() + " 为 " + responseJson);
						break;
					}
				} else {
					client.set(WFT_LIST_ADV_KEY, 60*5, responseJson);
					logger.info("================首次初始化 AdBody为 " + responseJson);
					break;
				}
			}
		} else {
			logger.error("通过威富通接口获取广告失败：" + responseJson);
		}
		return responseJson;
	}
	
	public static String getAdBody(){
		Object obj = client.get(WFT_LIST_ADV_KEY);
		logger.info("getAdBody " + obj + " is not null ? : " + (null != obj));
		if(null != obj){
			return obj.toString();
		} else {
			return setAdBody();
		}
	}
	
}
