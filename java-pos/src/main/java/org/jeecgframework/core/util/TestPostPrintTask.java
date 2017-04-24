package org.jeecgframework.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestPostPrintTask {

	public static String post(String url, String charset, Map params) throws IOException {
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
		try {
			conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			out = new OutputStreamWriter(conn.getOutputStream(), charset);
			out.write(buildQuery(params, charset));
			out.flush();
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
            	result.append(tempLine);
            }
            
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
		}
		return result.toString();
	}
	
	/**
	 * 将map转换为请求字符串
	 * <p>data=xxx&msg_type=xxx</p>
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String buildQuery(Map<String, Object> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuffer data = new StringBuffer();
		boolean flag = false;

		for (Entry<String, Object> entry : params.entrySet()) {
			if (flag) {
				data.append("&");
			} else {
				flag = true;
			}
			data.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset));
		}
		
		return data.toString();
	
	}
	
	private static String shopId = "2018";
	public static String createSign(String printId, String printContentList){
		StringBuilder sb = new StringBuilder();
		sb.append("printId="+printId);
		sb.append("&");
		sb.append("printContentList="+printContentList);
		sb.append("&");
		sb.append("shopId="+shopId);
		
		System.out.println(sb.toString());
		
		try{
			byte[] bs = encryptMD5(sb.toString().getBytes("utf-8"));
			sb = new StringBuilder();
			for(byte b : bs){
				String t = Integer.toHexString(b & 0xff);
				if(t.length() == 1){
					sb.append("0"+t);
				}else{
					sb.append(t);
				}
			}
			System.out.println(sb.toString());
			
		return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }
	
	public static void main(String[] args) throws IOException {

		String url = "http://www.jljcn.com:8180/edingapi/print";
//		String url = "http://localhost:8080/edingapi/print";
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("printId", "GZ3001_1");
		
		JSONArray contentArray = new JSONArray();
		JSONObject line1 = new JSONObject();
		line1.put("content", "第一行 测试ff");
		contentArray.add(line1);
		
		JSONObject line2 = new JSONObject();
		line2.put("content", "第二行 测试 dd");
		contentArray.add(line2);
		
		JSONObject line3 = new JSONObject();
		line3.put("content", "第三行 测试");
		contentArray.add(line3);
		
		JSONObject line4 = new JSONObject();
		line4.put("content", "第四行 测试");
		contentArray.add(line4);
		
		params.put("printContentList", contentArray.toString());
		
		String sign = TestPostPrintTask.createSign(params.get("printId"), params.get("printContentList"));
		
		params.put("sign", sign);
		
		System.out.println(contentArray.toString());
		TestPostPrintTask printTask = new TestPostPrintTask();
		String result = printTask.post(url, "utf-8", params);
		System.out.println(result);
		
	}

}
