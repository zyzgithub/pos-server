package com.wm.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http request 工具类
 * 
 * @author folo
 *
 */
public class HttpUtils {
    private static Logger logger= LoggerFactory.getLogger(HttpUtils.class);

    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

    public static ConnectionSpec allSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).allEnabledTlsVersions()
                    .allEnabledCipherSuites().build();

    public static OkHttpClient httpsClient = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).connectionSpecs(Arrays.asList(allSpec))
                    .build();

    /**
     * 组装url
     * @param url
     * @param params
     * @return
     */
    public static String encodUrl(String url, JSONObject params) {
        if (null != params) {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, Object>> keys = params.entrySet().iterator();
            while (keys.hasNext()) {
                Entry<String, Object> entry = keys.next();
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(0);
                url += "?" + sb.toString();
            }
        }
        return url;
    }

    /**
     * 异步下载文件
     * @param fileUrl url地址
     * @param downloadCallBack 回调
     */
    public static byte[] downloadFile(String fileUrl) {
        try {
            Request request = new Request.Builder().url(fileUrl).build();
            Response response = client.newCall(request).execute();
            return response.body().bytes();
        }
        catch (Exception e) {
            logger.warn("错误请求 url:{} 异常:{}", fileUrl, e);
        }
        return null;
    }

    public static String getStr(String url, Map<String, Object> params) {
        return getStr(url, JSON.parseObject(JSON.toJSONString(params)), false);
    }

    public static JSONObject get(String url, JSONObject params) {
        String ret = getStr(url, params);
        return null == ret ? null : JSON.parseObject(ret, JSONObject.class);
    }

    public static String get(String url) {
        return getStr(url, null);
    }

    /**
     * get请求
     * @param url 请求url
     * @param params 需要附加的参数
     * @param useHttps 使用https
     * @return
     */
    public static String getStr(String url, JSONObject params, boolean useHttps) {
        try {
            long start = System.currentTimeMillis();
            url = buildGetUrl(url, params);
            Request request = new Request.Builder().url(url).build();
            Response response = null;
            if (useHttps) response = httpsClient.newCall(request).execute();
            else response = client.newCall(request).execute();
            String ret = response.body().string();
            if (logger.isInfoEnabled())
                logger.info("【get({}ms)】 url------>{} result------>{}", System.currentTimeMillis() - start, url, ret);
            return ret;
        }
        catch (Exception e) {
            logger.warn("错误请求 url:{},params:{} 异常:{}", url, params.toJSONString(), e);
            return null;
        }
    }
    
    /**
     * 重试请求
     * @param url
     * @param params
     * @param reTry 重试次数
     * @param useHttps
     * @return
     */
    public static String getRetryStr(String url, JSONObject params, int reTry, boolean useHttps) {
        for (int i = 0; i < reTry; i++) {
            String ret = null;
            try {
                ret = getStr(url, params, useHttps);
                return ret;
            }
            catch (Exception e) {
                logger.warn("错误请求 次数:{} url:{},params:{} 异常:{}", i, url, params.toJSONString(), e);
            }
        }
        return null;
    }
    
    public static String buildGetUrl(String url, JSONObject params) {
        if (null != params) {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, Object>> keys = params.entrySet().iterator();
            while (keys.hasNext()) {
                Entry<String, Object> entry = keys.next();
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(0);
                if (url.indexOf("?") != -1) url += "&" + sb.toString();
                else url += "?" + sb.toString();
            }
        }
        return url;
    }

    /**
     * 异步get方法
     * @param url
     * @param params
     * @param useHttps
     */
    public static void getStrAsyn(String url, JSONObject params, boolean useHttps) {
        final long start = System.currentTimeMillis();
        url = buildGetUrl(url, params);
        Request request = new Request.Builder().url(url).build();
        final String finalUrl = url;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && logger.isInfoEnabled())
                    logger.info("【get({}ms)】  url------>{}", System.currentTimeMillis() - start, finalUrl);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (logger.isInfoEnabled())
                    logger.info("【get({}ms)】  url------>{}   exception------>{}", System.currentTimeMillis() - start, finalUrl, e);
            }
        });
    }

    public static JSONObject post(String url, JSONObject params, boolean isFrom) {
        return post(url, params, isFrom, JSONObject.class, false);
    }
    
    public static JSONObject post(String url, JSONObject params, boolean isFrom, boolean showLog) {
        return post(url, params, isFrom, JSONObject.class, false, showLog);
    }

    /**
     * post请求
     * @param url 请求url
     * @param params 参数
     * @param isFrom isFrom=true form方式请求/false body方式请求
     * @param c 返回类型
     * @return 失败返回null,成功返回json格式字符串
     */
    public static <T> T post(String url, JSONObject params, boolean isFrom, Class<T> c, boolean useHttps) {
        String retStr = postStr(url, params, isFrom, false, true);
        try {
            return JSON.parseObject(retStr, c);
        }
        catch (Exception e) {
            logger.warn("错误请求 url:{},params:{} 异常:{}", url, params.toJSONString(), e);
            return null;
        }
    }
    
    /**
     * post请求
     * @param url 请求url
     * @param params 参数
     * @param isFrom isFrom=true form方式请求/false body方式请求
     * @param c 返回类型
     * @return 失败返回null,成功返回json格式字符串
     */
    public static <T> T post(String url, JSONObject params, boolean isFrom, Class<T> c, boolean useHttps, boolean showLog) {
        String retStr = postStr(url, params, isFrom, false, showLog);
        try {
            return JSON.parseObject(retStr, c);
        }
        catch (Exception e) {
            logger.warn("错误请求 url:{},params:{} 异常:{}", url, params.toJSONString(), e);
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

    /**
     * post请求
     * @param url 地址
     * @param params 参数
     * @param isFrom 是否form编码
     * @param useHttps 是否https
     * @return
     */
    public static String postStr(String url, JSONObject params, boolean isFrom, boolean useHttps, boolean showLog) {
        try {
            long start = System.currentTimeMillis();
            RequestBody body = buildBody(url, params, isFrom);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            String ret = response.body().string();
            if (logger.isInfoEnabled() && showLog) logger.info("【post({}ms)】  url------>{}  params------>{}  result------> {}",
                            System.currentTimeMillis() - start, url, params, ret);
            return ret;
        }
        catch (Exception e) {
            logger.warn("错误请求 url:{},params:{} 异常:{}", url, params.toJSONString(), e);
            return null;
        }
    }

    /**
     * 生成post body
     * @param url
     * @param params
     * @param isFrom
     * @return
     */
    public static RequestBody buildBody(String url, JSONObject params, boolean isFrom) {
        if (isFrom) {
            Builder fbuilder = new FormBody.Builder();
            Iterator<Entry<String, Object>> keys = params.entrySet().iterator();
            while (keys.hasNext()) {
                Entry<String, Object> entry = keys.next();
                fbuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return fbuilder.build();
        }
        else {
            return RequestBody.create(mediaType, params.toJSONString());
        }
    }

    /**
     * post异步请求
     * @param url 地址
     * @param params 参数
     * @param isFrom 是否form编码
     * @param useHttps 是否https
     * @return
     */
    public static void postStrAsyc(String url, JSONObject params, boolean isFrom, boolean useHttps) {
        final long start = System.currentTimeMillis();
        RequestBody body = buildBody(url, params, isFrom);
        Request request = new Request.Builder().url(url).post(body).build();

        final String finalUrl = url;
        final JSONObject finalParams = params;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("【post({}ms)】  url------>{}  params------>{}  status------> {}", System.currentTimeMillis() - start,
                                finalUrl, finalParams, response.isSuccessful());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (logger.isWarnEnabled()) logger.warn("【post({}ms)】  url------>{}  params------>{}  exception------> {}",
                                System.currentTimeMillis() - start, finalUrl, finalParams, e);
            }
        });
    }

    /**
     * json串转换成请求参数格式 例如：{name:"张三",age:22} 转换后为: name=张三&age=22
     * @param jsonStr
     * @return
     */
    public static String toRequestPrams(String jsonStr) {
        jsonStr = jsonStr.replace("{\"", "").replace("\",\"", "&").replace("\":\"", "=");
        jsonStr = jsonStr.replace(",\"", "&").replace("\":", "=");
        jsonStr = jsonStr.substring(0, jsonStr.length() - 2);
        return jsonStr;
    }

    public static String toRequestPrams(Map<String, Object> params) {
        String paramStr = "";
        for (String key : params.keySet()) {
            Object value = params.get(key);
            paramStr += "&" + key + "=" + value;
        }
        if (paramStr.indexOf("&") != -1) paramStr = paramStr.substring(1);
        return paramStr;
    }
}
