package com.ucf.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.life.ucf.UCFService;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.AESCoder;

/**
 * 先锋支付
 * @author Simon
 */
@Controller
@RequestMapping("/takeOutController/ucf")
public class UcfNoticeController {

	private static final Logger logger = LoggerFactory.getLogger(UcfNoticeController.class);

	private final String SIGN = "sign";//保存签名字段
    private final String SECID = "RSA";//签名算法
    
    @Autowired
    private UCFService ucfService;

	/**
	 * 微信支付回调处理
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	@RequestMapping("/withdrawNotice.do")
	public void withdrawNotice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("-------------服务器端通知-接收到先锋支付返回报文：--------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type","text/html;charset=UTF-8");
		
		String data = request.getParameter("data");
		String signValue = ""; // 保存签名值
		
		Map<String,String> signParameters = new HashMap<String,String>(); // 保存参与验签字段
		
		if (data != null && !data.isEmpty()) {
			logger.info("======== 解密参数内容 ========");
			JSONObject parameters = JSONObject.parseObject(decrypt(data));
			
			signValue = parameters.getString(SIGN);
			for (Iterator<Entry<String, Object>> entrys = parameters.entrySet().iterator(); entrys.hasNext();) {
				Entry<String, Object> entry = entrys.next();
				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());
				
				logger.info(key + "-------------" + value);
				
				if (SIGN.equals(key)) {
					continue;
				}
				signParameters.put(key, value);
			}
		} else {
			logger.info("======== 读取参数内容 ========");
			// 打印先锋支付返回值
			Iterator<String> paiter = request.getParameterMap().keySet().iterator();
			while (paiter.hasNext()) {
				String key = paiter.next().toString();
				String value = request.getParameter(key);
				logger.info(key + "-------------" + value);
				if (SIGN.equals(key)) {
					signValue = value;
				} else {
					signParameters.put(key, value);
				}
			}
		}
        boolean verifyResult = false;
		try {
			// 调用先锋支付类库中验签方法
			verifyResult = UcfForOnline.verify(EnvConfig.ucf.merRSAKey, SIGN, signValue, signParameters, SECID);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (CoderException e) {
			e.printStackTrace();
		}
		if (!verifyResult) {
			logger.info("sign verify FAIL:验签失败");
			return;
		}
		logger.info("sign verify SUCCESS:验签通过");
		PrintWriter writer = response.getWriter();
		// 验签成功需返回先锋支付“SUCCESS”
		writer.write("SUCCESS");
		
		verifyResult = "S".equals(signParameters.get("status").toString());
		
		JSONObject responseResult = new JSONObject();
		responseResult.put("tradeNo", signParameters.get("tradeNo"));
		responseResult.put("merchantNo", signParameters.get("merchantNo"));
		responseResult.put("resMessage", signParameters.get("resMessage"));

		ucfService.updatePaymentFlow(responseResult, verifyResult ? 1 : 2);
	}
	
	private String decrypt(String data) {
		try {
			return new String(AESCoder.decrypt(data, EnvConfig.ucf.merRSAKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}