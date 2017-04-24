package com.wm.controller.open_api.iwash;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * http request 工具类
 * 
 * @author folo
 *
 */
public class HttpUtils {
	public static Logger log = LoggerFactory.getLogger(HttpUtils.class.getName());
	public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
	public static OkHttpClient client = new OkHttpClient();
	
	/**
	 * get请求
	 * @param url 请求url
	 * @param params 需要附加的参数
	 * @return
	 */
	public static JSONObject get(String url, JSONObject params){
		String ret = "";
		try {
			ret = getStr(url, params);
			return null == ret ? null : JSON.parseObject(ret, JSONObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ret);
			return null;
		}
	}
	
	public static String getStr(String url, JSONObject params){
		try {
			url = encodUrl(url, params);
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();
			String ret = response.body().string();
			log.info(String.format("----- 【get】 url=%s \n result:%s", url, ret));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 组装url
	 * @param url
	 * @param params
	 * @return
	 */
	public static String encodUrl(String url, JSONObject params){
		if(null != params){
			StringBuilder sb = new StringBuilder();
			Iterator<Entry<String,Object>> keys = params.entrySet().iterator();
			while (keys.hasNext()) {
				Entry<String,Object> entry = keys.next();
				sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
			if(sb.length() > 0){
				sb.deleteCharAt(0);
				url += "?" + sb.toString();
			}
		}
		return url;
	}
	
	public static JSONObject get(String url){
		return get(url, null);
	}
	
	/**
	 * 异步请求
	 * @param url
	 * @param params
	 */
	public void postAsyc(String url, JSONObject params) {
        try {
            RequestBody body = null;
            FormEncodingBuilder builder = new FormEncodingBuilder();
            Iterator<Entry<String,Object>> keys = params.entrySet().iterator();
            while (keys.hasNext()) {
                Entry<String,Object> entry = keys.next();
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            body = builder.build();
            Request request = new Request.Builder().url(url).post(body).build();
            
            client.setConnectTimeout(5, TimeUnit.SECONDS);
            client.setReadTimeout(5, TimeUnit.SECONDS);
            client.setWriteTimeout(5, TimeUnit.SECONDS);
            
            final String asycUrl = url;
            final JSONObject asycParams = params;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    if(response.isSuccessful())
                        log.info(String.format("----- 【postAsyc】 url={} \n params:{} \n Success", asycUrl, asycParams));
                }
                
                @Override
                public void onFailure(Request request, IOException e) {
                    log.info(String.format("----- 【postAsyc】 url={} \n params:{} \n Failed", asycUrl, asycParams));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * post请求
	 * 
	 * @param url 请求url
	 * @param params 参数
	 * @param isFrom  isFrom=true form方式请求/false body方式请求
	 * @return 失败返回null,成功返回json格式字符串
	 */
	public static JSONObject post(String url, JSONObject params, boolean isFrom) {
		try {
			RequestBody body = null;
			if(isFrom){
				FormEncodingBuilder builder = new FormEncodingBuilder();
				Iterator<Entry<String,Object>> keys = params.entrySet().iterator();
				while (keys.hasNext()) {
					Entry<String,Object> entry = keys.next();
					builder.add(entry.getKey(), String.valueOf(entry.getValue()));
				}
				body = builder.build();
			}else{
				body = RequestBody.create(mediaType, params.toJSONString());
			}
			
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			String ret = response.body().string();
			log.info(String.format("----- 【post】 url={} \n params:{} \n result:{}", url, params, ret));
			return null == ret ? null : JSON.parseObject(ret, JSONObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String postStr(String url, JSONObject params, boolean isFrom) {
		try {
			RequestBody body = null;
			if(isFrom){
				FormEncodingBuilder builder = new FormEncodingBuilder();
				Iterator<Entry<String,Object>> keys = params.entrySet().iterator();
				while (keys.hasNext()) {
					Entry<String,Object> entry = keys.next();
					builder.add(entry.getKey(), String.valueOf(entry.getValue()));
				}
				body = builder.build();
			}else{
				body = RequestBody.create(mediaType, params.toJSONString());
			}
			
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			String ret = response.body().string();
			log.info(String.format("----- 【post】 url={} \n params:{} \n result:{}", url, params, ret));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * post form方式请求
	 * 
	 * @param url 请求url
	 * @param params 参数
	 * @return 失败返回null,成功返回json格式字符串
	 */
	public static JSONObject postForm(String url, JSONObject params) {
		return post(url, params, true);
	}
	/**
	 * post body请求
	 * 
	 * @param url 请求url
	 * @param params 参数
	 * @return 失败返回null,成功返回json格式字符串
	 */
	public static JSONObject postBody(String url, JSONObject params) {
		return post(url, params, false);
	}
}
