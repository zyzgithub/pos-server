package com.wxpay.refund;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxRefundSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WxRefundSender.class);

	public String send(String refundXml) {
		try (FileInputStream fis = new FileInputStream(new File(WxRefundConfig.CER))) {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(fis, WxRefundConfig.MCH_ID.toCharArray());

			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, WxRefundConfig.MCH_ID.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			HttpPost httpPost = new HttpPost(WxRefundConfig.URL);
			httpPost.setEntity(new StringEntity(refundXml, WxRefundConfig.CHARSET));
			
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			LOGGER.info("statusCode: {}", statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String entityString = EntityUtils.toString(entity, WxRefundConfig.CHARSET);
				EntityUtils.consume(entity);
				return entityString;
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

}
