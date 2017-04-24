/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.wxpay.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
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

import com.base.config.EnvConfig;
import com.wp.ConfigUtil;

/**
 * 微信PKCS12证书(注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的)
 * 证书文件位置：WEB-INF/apiclient_cert_test.p12
 * @author 黄聪
 */
public class ClientCustomSSL {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientCustomSSL.class);

    public static String doRefund(String url,String data) throws Exception {

    	//指定读取证书格式为PKCS12
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        //读取本机存放的PKCS12证书文件
//        String certUrl=ClientCustomSSL.class.getClassLoader().getResource( EnvConfig.base.APICLIENT_CERT_FILE ).getPath();//P12文件目录
        // certUrl=~/WEB-INF/classes/
		String certUrl = ClientCustomSSL.class.getClassLoader().getResource("").getPath();
		logger.info("certUrl={}", certUrl);
		certUrl = certUrl.substring(0, certUrl.indexOf("classes/")) + EnvConfig.base.APICLIENT_CERT_FILE;
		logger.info("certUrl={}", certUrl);
        FileInputStream instream = new FileInputStream(new File(certUrl));//P12文件目录
        try {
        	//指定PKCS12的密码(商户ID,默认是你的MCHID)
            keyStore.load(instream, ConfigUtil.MCH_ID_KFZ.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        //指定PKCS12的密码(商户ID,默认是你的MCHID)
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, ConfigUtil.MCH_ID_KFZ.toCharArray())
                .build();
        //指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
        	// 设置响应头信息
        	HttpPost httpost = new HttpPost(url); 
        	httpost.addHeader("Connection", "keep-alive");
        	httpost.addHeader("Accept", "*/*");
        	httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        	httpost.addHeader("Host", "api.mch.weixin.qq.com");
        	httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        	httpost.addHeader("Cache-Control", "max-age=0");
        	httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    		httpost.setEntity(new StringEntity(data, "UTF-8"));
    		//设置httpclient的SSLSocketFactory
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
               return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
}
