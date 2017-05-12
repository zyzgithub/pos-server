package com.dianba.pos.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpProxy {
    private final static String CHARSET = "UTF-8";
    private final Logger logger = LoggerFactory.getLogger(HttpProxy.class);

    private final String url;
    private final List<Header> headers;
    private final List<NameValuePair> params;

    public static HttpProxy createInstance(String url, List<Header> headers, List<NameValuePair> params) {
        return new HttpProxy(url, headers, params);
    }

    public static HttpProxy createInstance(String url, List<Header> headers, Map<String, Object> params) {
        return new HttpProxy(url, headers, params);
    }

    public HttpProxy(String url, List<Header> headers, List<NameValuePair> params) {
        this.url = url;
        this.headers = headers;
        this.params = params;
    }

    public HttpProxy(String url, List<Header> headers, Map<String, Object> params) {
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        if(params != null){
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                NameValuePair param = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                paramList.add(param);
            }
        }
        this.url = url;
        this.headers = headers;
        this.params = paramList;
    }

    public String getUrl() {
        return url;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    /**
     * 设置header
     *
     * @param request
     * @return
     */
    public void setHeader(AbstractHttpMessage httpRequest) {
        if (CollectionUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                httpRequest.addHeader(header);
            }
        }
    }

    private String buildFullUrl() {

        StringBuilder urlBuilder = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.size(); i++) {
                NameValuePair param = params.get(i);
                urlBuilder.append("&");
                urlBuilder.append(param.getName());
                urlBuilder.append("=");
                urlBuilder.append(param.getValue());
            }
        }

        String fullUrl = url;
        if (StringUtils.contains(url, "?")) {
            fullUrl += urlBuilder.toString();
        } else {
            if (urlBuilder.length() > 0) {
                fullUrl += urlBuilder.toString().replaceFirst("&", "?");
            }
        }
        return fullUrl;

    }

    public String doGet() throws HttpAccessException {

        HttpClient httpClient = null;
        HttpGet httpGet = null;
        HttpEntity entity = null;
        try {
//			httpClient =  HttpClientBuilder.create().build();
            httpClient = new DefaultHttpClient();
            HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
            String url = buildFullUrl();

            httpGet = new HttpGet(url);
            setHeader(httpGet);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            entity = httpResponse.getEntity();

            if (statusCode == HttpStatus.SC_OK) {

                if (entity != null) {
                    String responseText = EntityUtils.toString(entity, Consts.UTF_8);

                    return responseText;
                }
            } else {
                logger.info("url:{}, statusCode:{}", new Object[] { url, statusCode });
            }
        } catch (Exception e) {
            if (e instanceof ClientProtocolException || e instanceof IOException) {
                throw new HttpAccessException("http访问失败，url:" + url, e);
            } else {
                e.printStackTrace();
                throw new HttpAccessException("内部异常", e);
            }
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {

                    logger.error("doGet IOException:", e);
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    ((CloseableHttpClient)httpClient).close();
                } catch (IOException e2) {
                }
            }

        }
        return null;
    }

    public String doPost() throws HttpAccessException {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpEntity entity = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
            httpPost = new HttpPost(url); // 创建HttpPost
            setHeader(httpPost);

            if (CollectionUtils.isNotEmpty(params)) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, CHARSET));
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            entity = httpResponse.getEntity();
            if (statusCode == HttpStatus.SC_OK) {

                if (null != entity) {
                    String reponseText = EntityUtils.toString(entity, CHARSET);

                    return reponseText;
                }
            } else {
                logger.info("url:{}, params:{}, statusCode:{}", new Object[]{url, params, statusCode});
            }

        } catch (Exception e) {
            if (e instanceof ClientProtocolException || e instanceof IOException) {
                throw new HttpAccessException("http访问失败，url:" + url, e);
            } else {
                e.printStackTrace();
                throw new HttpAccessException("内部异常", e);
            }
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    ((CloseableHttpClient) httpClient).close();
                } catch (IOException e2) {
                }
            }
        }
        return null;
    }

    public String httpInvoke() throws HttpAccessException {
        return doPost();
    }

    @SuppressWarnings("serial")
    public static class HttpAccessException extends Exception {
        public HttpAccessException(String msg) {
            super(msg);
        }

        public HttpAccessException(Throwable cause) {
            initCause(cause);
        }

        public HttpAccessException(String msg, Throwable cause) {
            super(msg);
            initCause(cause);
        }
    }

}
