package com.team.wechat.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.star.uno.RuntimeException;
import com.wm.util.BaiduAddress;
import com.wm.util.BaiduMap;

public class MapUtil {
	private static ObjectMapper om = new ObjectMapper();
	static double DEF_PI = 3.14159265359; // PI2.
	static double DEF_2PI = 6.28318530712; // 2*PI3.
	static double DEF_PI180 = 0.01745329252; // PI/180.04.
	static double DEF_R = 6370693.5; // radius of earth

	public static double GetShortDistance(String lon1, String lat1, String lon2,String lat2){
		return GetShortDistance(strToDouble(lon1),strToDouble(lat1),strToDouble(lon2),strToDouble(lat2));
	}
	public static double GetShortDistance(double lon1, double lat1, double lon2,double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	
	public static double GetLongDistance(String lon1, String lat1, String lon2,String lat2){
		return GetLongDistance(strToDouble(lon1),strToDouble(lat1),strToDouble(lon2),strToDouble(lat2));
	}
	
	public static double GetLongDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	public static double strToDouble(String v){
		if(v==null ||"".equals(v))
			return 0;
		else{
			try{
				return Double.parseDouble(v);
			}catch(Exception e){}
		}
		return 0;
	}
	
	public static BaiduMap getBaiduMap(String address) {
		try {
			String spec = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=hf7GGTXwqxndSLLYBfrKVwZh&address="+URLEncoder.encode(address, "utf-8");
			URL url = new URL(spec);
			
			BaiduMap map = om.readValue(url, BaiduMap.class);
			
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("地址解析错误");
		}
		
	}
	
	public static BaiduAddress getBaiduAddress(double lng, double lat) {
		BaiduAddress address = new BaiduAddress();
		try {
			String s = "http://api.map.baidu.com/geocoder/v2/?ak=HaNyiialPLKRigFG8aEo8tDZ&location="+lat+","+lng+"&output=json";
			URL url = new URL(s);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.connect();
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuilder sb = new StringBuilder();
			String ss = null;
			while((ss = r.readLine())!=null)
				sb.append(ss);
			r.close();
			
			JSONObject json = new JSONObject(sb.toString());
			int status = json.getInt("status");
			if(0 == status) {
				
				json = json.getJSONObject("result").getJSONObject("addressComponent");
				address.setCity(json.getString("city"));
				address.setDistrict(json.getString("district"));
				address.setStreet(json.getString("street"));
				address.setStreet_number(json.getString("street_number"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return address;
	}
}
