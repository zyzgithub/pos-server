package com.twodimensionalcode.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.config.EnvConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.twodimensionalcode.util.MatrixToImageWriter;
import com.wp.ConfigUtil;

@Controller
@RequestMapping("twoDimensionalCodeController")
public class TwoDimensionalCodeController {
	
	private static final Logger logger = Logger.getLogger(TwoDimensionalCodeController.class);

	@Autowired
	private SystemService systemService;
	
	@Value("${isMixVersion}")
	private String isMixVersion;
	
	/**
	 * 获取图片验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(params = "getTwoDimensionalCode")
	@ResponseBody
	public void getTwoDimensionalCode(HttpServletRequest request,HttpServletResponse response,String content,String type,int width,int height) {
		if("merchant".equals(type)){
			if("false".equals(isMixVersion)){
				content = ConfigUtil.MERCHANT_HOME.replace("MERCHANT_ID", content + "");
			}else {
				content = systemService.getSystemConfigValue(type) + content + ".do";
			}
		}
		else if("menu".equals(type)){
			content = systemService.getSystemConfigValue(type) + content;
		}
		else if("merchant_scan_code_url".equals(type)){
			content = EnvConfig.base.DOMAIN18 + systemService.getSystemConfigValue(type).replace("{$merchant_id}", content);	
		}
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
	    try { 
	    	Map hints = new HashMap();
	    	hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	    	logger.info("二维码地址："+content);
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height,hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "jpg", response.getOutputStream());
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
