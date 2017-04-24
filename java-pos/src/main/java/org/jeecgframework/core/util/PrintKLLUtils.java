package org.jeecgframework.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PrintKLLUtils {
	
	private static final Logger logger = Logger.getLogger(PrintKLLUtils.class);
	
	public static String appid = "Ka7c0ac75be707710";
	private static String appsecret = "f5c425575e1ba81eb65c5b358f9b293fe4352fd6";
//	private static String pay_notify_url = "http://no1.0085.com/openapi/printController/kelaile/notify.do";
//	private static String pay_notify_url_test = "http://apptest.0085.com/openapi/printController/kelaile/notify.do";
	
	
	public static void printTest(String content){
		System.out.println(content);
		print("KP001951",content);
//		print("DEV11404,DEV11530",content,appid,appsecret);
//		orderPrint("DEV11530",content,appid,appsecret);
	}
	
	/**
	 * 客来乐签名验证
	 * 签名规则 key1=value1&key2=value2appsecret sha算法
	 * @param paramsMap
	 * @return
	 */
	
	public static boolean signCheck(TreeMap<String, String> paramsMap,String sign){
		boolean result = false;
		Set<String> keySet = paramsMap.keySet();
		
		Iterator<String> iter = keySet.iterator(); 
		String key = null;
		String value = null;
		String signStr = "";
		while(iter.hasNext()){ 
			key = iter.next(); 
			value = paramsMap.get(key);
			if(!StringUtils.isEmpty(value)){
				signStr += key + "=" + value + "&";			
			}
		}
		String signResult = SHA(signStr.substring(0,signStr.length() - 1) + appsecret).toLowerCase();
		if(sign.equals(signResult)){
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 小票机打印
	 * @param devno 设备号
	 * @param content 打印内容
	 * @param appId
	 * @param appSecret
	 */
	public static boolean print(String devno,String content){
		boolean result = false;
		
//		devno = "DEV11530";//测试用
		
		devno = devno.replaceAll(",", "\",\"");//支持多打印机同时打
		
		// 去除内容中的硬换行符，否则会签名失败
//		content = content.replaceAll("\r\n", "");
		content = com.wm.util.StringUtil.replaceNTSRElement(content);
		
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"appid\":\"").append(appid).append("\",");
		sb.append("\"content\":\"").append(content).append("\",");
		sb.append("\"device_nos\":[\"").append(devno).append("\"],");
		String  nowTime = (int) (System.currentTimeMillis()/1000) + "";
		sb.append("\"timestamp\":\"").append(nowTime).append("\"");

		String sign = SHA((sb.toString() + "}")).toLowerCase();
		sign = SHA(sign + appsecret).toLowerCase();
		final String body = sb.append(",\"sign\":\"").append(sign).append("\"}").toString();
		logger.info(body);
		try{
			final URL url = new URL("http://p.kelaile.cn/api/device/print");
			
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("Content-Length", Integer.toString(body.getBytes().length));

			connection.setDoInput(true);
			connection.setDoOutput(true);

			final DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.write(body.getBytes("UTF-8"));
			output.flush();
			
			InputStream input = connection.getInputStream();
			String codestr = convertStreamToString(input);
			JSONObject obj = JSONObject.fromObject(codestr);
			int code = (Integer) obj.get("code");
			if (code != 0) {
				logger.error("print log devno:" + devno + ",code:" + code + ",msg:" + obj.get("msg").toString());
				result = false;
			}else{
				logger.info("print log devno:" + devno + ",code:" + code + ",res:" + obj.get("res").toString());
				result = true;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 远程下单打印
	 * @param devno
	 * @param content
	 * @param appId
	 * @param appSecret
	 */
	public static Boolean orderPrint(String devno,String content,String appId,String appSecret, String payId, String totalPrice){
		boolean result = false;
		
		content = com.wm.util.StringUtil.replaceNTSRElement(content);
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"appid\":\"").append(appId).append("\",");
		String  nowTime = (int) (System.currentTimeMillis()/1000) + "";
		sb.append("\"content\":\"").append(content).append("\",");
		sb.append("\"device_no\":\"").append(devno).append("\",");
		sb.append("\"out_trade_no\":\"").append(payId).append("\",");
//		sb.append("\"pay_notify_url\":\"").append(pay_notify_url_test).append("\",");
		sb.append("\"show_pay_qr\":\"").append("1").append("\",");
		sb.append("\"timestamp\":\"").append(nowTime).append("\",");
		sb.append("\"total_price\":\"").append(totalPrice).append("\"");

		String sign = SHA((sb.toString() + "}")).toLowerCase();
		sign = SHA(sign + appSecret).toLowerCase();
		final String body = sb.append(",\"sign\":\"").append(sign).append("\"}").toString();
		logger.info(body);
		try{
			final URL url = new URL("http://p.kelaile.cn/api/device/orderPrint");
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(body.getBytes().length));
			connection.setDoInput(true);
			connection.setDoOutput(true);
			final DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.write(body.getBytes("UTF-8"));
			output.flush();
			
			InputStream input = connection.getInputStream();
			String codestr = convertStreamToString(input);
			JSONObject obj = JSONObject.fromObject(codestr);
			int code = (Integer) obj.get("code");
			if (code != 0) {
				logger.error("print log devno:" + devno + ",code:" + code + ",msg:" + obj.get("msg").toString());
				result = false;
			}else{
				String res = obj.get("res").toString();
				logger.info("print log devno:" + devno + ",code:" + code + ",res:" + res);
				JSONObject resJson = JSONObject.fromObject(res);
				String tradeNo = resJson.get("trade_no").toString();
				String outTradeNo = resJson.get("out_trade_no").toString();
				logger.info("orderPrint result tradeNo: " + tradeNo + ",outTradeNo:" + outTradeNo);
				result = true;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public static String convertStreamToString(InputStream is) {   
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
	  	StringBuilder sb = new StringBuilder();   		   
	  	String line = null;   
	  	try {   
	  		while ((line = reader.readLine()) != null) {   
	  			sb.append(line + "/n");   
	  		}   
	  	} catch (IOException e) {   
	  		logger.error(e.getMessage());
	  	} finally {   
	  		try {   
	  			is.close();   
	  		} catch (IOException e) {   
	  			logger.error(e.getMessage());
	  		}   
	  	}   
	  	return sb.toString();   
	}   
	
	public static String SHA(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes("utf-8"));
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
        return "";
    }
	
	public static void main(String arg[]){
		String content;
		content = "\\t\\t测试打印\\r\\n";
		content += "名称　　　　　 单价  数量 金额\\r\\n";
		content += "--------------------------------\\r\\n";
		content += "饭　　　　　　 1.0    1   1.0\\r\\n";
		content += "炒饭　　　　　 10.0   10  10.0\\r\\n";
		content += "蛋炒饭　　　　 10.0   10  100.0\\r\\n";
		content += "鸡蛋炒饭　　　 100.0  1   100.0\\r\\n";
		content += "番茄蛋炒饭　　 1000.0 1   100.0\\r\\n";
		content += "西红柿蛋炒饭　 1000.0 1   100.0\\r\\n";
		content += "西红柿鸡蛋炒饭 100.0  10  100.0\\r\\n";
		content += "备注：加辣\\r\\n";
		content += "--------------------------------\\r\\n";
		content += "合计：xx.0元\\r\\n";
		content += "送货地点：广州市南沙区xx路xx号\\r\\n";
		content += "联系电话：13888888888888\\r\\n";
		content += "订餐时间：2014-08-08 08:08:08\\r\\n";
		content += "\\r\\n\\r\\n\\r\\n";
		System.out.println(content);
		// 注意：内容不可包含硬换行符，制表符、换行符、回车符要转义，不然会签名失败
		PrintKLLUtils.printTest(content);
	}
}
