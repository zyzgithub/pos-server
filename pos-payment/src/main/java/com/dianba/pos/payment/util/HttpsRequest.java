package com.dianba.pos.payment.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;


public class HttpsRequest {

    private static Logger logger = LogManager.getLogger(HttpsRequest.class);

    public static String sendPost(String url, String postDataXML) throws IOException, KeyStoreException
            , UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = WmSSLHttpClientFactory.getInstance().getDefaultClient();
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        logger.info("API，POST过去的数据是：{}", postDataXML);
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        //设置请求器的配置
        httpPost.setConfig(WmSSLHttpClientFactory.getRequestConfig());
        logger.info("executing request" + httpPost.getRequestLine());
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            logger.error("http get throw ConnectionPoolTimeoutException(wait time out)");
        } catch (ConnectTimeoutException e) {
            logger.error("http get throw ConnectTimeoutException");
        } catch (SocketTimeoutException e) {
            logger.error("http get throw SocketTimeoutException");
        } catch (Exception e) {
            logger.error("http get throw Exception");
        } finally {
            httpPost.abort();
        }
        return result;
    }

}
