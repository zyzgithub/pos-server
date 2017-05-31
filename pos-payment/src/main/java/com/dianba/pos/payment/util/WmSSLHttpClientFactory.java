package com.dianba.pos.payment.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public final class WmSSLHttpClientFactory {

    private static Logger logger = LoggerFactory.getLogger(WmSSLHttpClientFactory.class);

    //连接超时时间，默认10秒
    private static int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private static int connectTimeout = 30000;

    //SSL连接工厂
    private static SSLConnectionSocketFactory sslsf;

    private static WmSSLHttpClientFactory instance = null;

    //请求器的配置
    private static RequestConfig requestConfig;

    public static synchronized WmSSLHttpClientFactory getInstance()
            throws IOException, KeyStoreException
            , UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        if (instance == null) {
            return new WmSSLHttpClientFactory();
        }
        return instance;
    }

    private void init()
            throws IOException, KeyStoreException
            , UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String certLocalPath = WmHttpsRequest.class.getClassLoader().getResource("").getPath();
        certLocalPath = certLocalPath.substring(0, certLocalPath.indexOf("classes/")) + "apiclient_cert_product.p12";
        File file = new File(certLocalPath);
        if (!file.isFile() || !file.exists()) {
            logger.error("证书文件不存在,路径:{},无法初始化...", certLocalPath);
            throw new RuntimeException("证书文件不存在");
        }

        FileInputStream instream = new FileInputStream(file);//加载本地的证书进行https加密传输
        try {
            logger.info("ConfigUtil.MCH_ID_KFZ:" + ConfigUtil.MCH_ID_KFZ);
            keyStore.load(instream, ConfigUtil.MCH_ID_KFZ.toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, ConfigUtil.MCH_ID_KFZ.toCharArray())
                .build();

        // Allow TLSv1 protocol only
        sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();
    }


    private WmSSLHttpClientFactory() throws IOException, KeyStoreException
            , UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        init();
    }

    public CloseableHttpClient getDefaultClient() {
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public static void setRequestConfig(RequestConfig requestConfig) {
        WmSSLHttpClientFactory.requestConfig = requestConfig;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public static void setSocketTimeout(int socketTimeout) {
        WmSSLHttpClientFactory.socketTimeout = socketTimeout;
    }

    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        WmSSLHttpClientFactory.connectTimeout = connectTimeout;
    }
}
