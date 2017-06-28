package com.dianba.pos.payment.util;

import com.dianba.pos.payment.config.WechatConfig;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public final class SSLHttpClientFactory {

    private static Logger logger = LogManager.getLogger(SSLHttpClientFactory.class);

    //连接超时时间，默认10秒
    private static int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private static int connectTimeout = 30000;

    //SSL连接工厂
    private static SSLConnectionSocketFactory sslsf;

    private static SSLHttpClientFactory instance = null;

    //请求器的配置
    private static RequestConfig requestConfig;

    public static synchronized SSLHttpClientFactory getInstance(WechatConfig wechatConfig) throws IOException
            , KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        if (instance == null) {
            return new SSLHttpClientFactory(wechatConfig);
        }
        return instance;
    }

    private void init(WechatConfig wechatConfig) throws IOException, KeyStoreException, UnrecoverableKeyException
            , NoSuchAlgorithmException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String path = HttpsRequest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        InputStream inputStream = this.getClass().getResourceAsStream("/apiclient_cert_product.p12");
        if (inputStream == null) {
            logger.error("证书文件不存在,路径:{},无法初始化...apiclient_cert_product.p12");
            throw new RuntimeException("证书文件不存在");
        }
        try {
            logger.info("ConfigUtil.MCH_ID_KFZ:" + wechatConfig.getKfzMerchantId());
            keyStore.load(inputStream, wechatConfig.getKfzMerchantId().toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, wechatConfig.getKfzMerchantId().toCharArray())
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


    private SSLHttpClientFactory(WechatConfig wechatConfig) throws IOException, KeyStoreException
            , UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        init(wechatConfig);
    }

    public CloseableHttpClient getDefaultClient() {
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

}
