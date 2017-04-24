package com.wm.service.impl.baidu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm.service.baidu.BaiduMapServiceI;
import com.wm.util.HttpProxy;


@Service("baiduMapService")
public class BaiduMapServiceImpl implements BaiduMapServiceI {
	
	private final static Logger logger = LoggerFactory.getLogger(BaiduMapServiceImpl.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${baidu_api_ak}")
	private String ak = "A6uq3wBrs9cuu4H5aKVe5P5b";
	
	@Value("${baidu_map_api_url}")
	private String mapApiUrl = "http://api.map.baidu.com/";
	
	@Value("${baidu_address_interface}")
	private String addressUrl;
	
	@Value("${baidu_get_address_detail}")
	private String getAddressDetailUrl;
	
	@Value("${baidu_distance_interface}")
	private String distanceUrl = "/direction/v1";
	
	@Override
	public List<Map<String, Object>> searchAddressByRadis(String name,
			double lng, double lat, long radius) {
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		
		if(StringUtils.isBlank(ak)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置baidu_api_ak");
			return results;
		}
		
		if(StringUtils.isBlank(mapApiUrl)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置mapApiUrl");
			return results;
		}
		
		if(StringUtils.isBlank(addressUrl)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置addressUrl");
			return results;
		}
		
		String url = mapApiUrl + addressUrl;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query", name);
		params.put("location", lat + "," + lng);
		params.put("radius", radius);
		params.put("output", "json");
		params.put("ak", ak);
		
		logger.info("调用百度地图获取地址url:" + mapApiUrl + addressUrl);
		logger.info("调用百度地图获取地址params:" + JSON.toJSONString(params));
		HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
		
		try {
			String responseText = httpProxy.doGet();
			
			if(responseText != null){
				
				JsonNode response = objectMapper.readTree(responseText);
				if(response.get("status").asInt() == 0){
					JsonNode resultsJsonNode = response.get("results");
					Iterator<JsonNode> iterator = resultsJsonNode.iterator();
					
					while( iterator.hasNext()) {
						JsonNode curJsonNode = iterator.next();
						String addressName = curJsonNode.get("name") == null? "":curJsonNode.get("name").asText();
						JsonNode location = curJsonNode.get("location");
						String longitude = location.get("lng") == null?"":location.get("lng").asText();
						String latitude = location.get("lat")==null?"":location.get("lat").asText();
						String address = curJsonNode.get("address") == null ?"":curJsonNode.get("address").asText();
						
						Map<String, Object> record = new HashMap<String, Object>();
						record.put("name", addressName);
						record.put("longitude", longitude);
						record.put("latitude", latitude);
						record.put("address", address);
						
						results.add(record);
					}
				}
				else{
					logger.warn("调用百度地图获取地址失败， 返回结果:" + responseText);
				}
			}
			logger.info("调用百度地图获取地址， 返回结果responseText:" + responseText);
			logger.info("调用百度地图获取地址， 返回结果results:" + results);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@Override
	public Map<String, String> getAddressDetail(double lng, double lat){
		
		if(StringUtils.isBlank(ak)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置baidu_api_ak");
			return null;
		}
		
		if(StringUtils.isBlank(mapApiUrl)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置mapApiUrl");
			return null;
		}
		
		if(StringUtils.isBlank(getAddressDetailUrl)){
			logger.warn("调用百度地图获取地址失败，错误原因:没有设置addressUrl");
			return null;
		}
		
		String url = mapApiUrl + getAddressDetailUrl;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("location", lat + "," + lng);
		params.put("output", "json");
		params.put("ak", ak);
		HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
		
		try {
			String responseText = httpProxy.doGet();
			
			if(responseText != null){
				JsonNode response = objectMapper.readTree(responseText);
				if(response.get("status").asInt() == 0){
					JsonNode result = response.get("result");
					
					String cityCode = result.get("cityCode").asText();
					String address = result.get("formatted_address").asText();
					String sematicDesc = result.get("sematic_description").asText();
					
					JsonNode addressComponent = result.get("addressComponent");
					String adcode = addressComponent.get("adcode").asText();
					String city = addressComponent.get("city").asText();
					String country = addressComponent.get("country").asText();
					String direction = addressComponent.get("direction").asText();
					String distance = addressComponent.get("distance").asText();
					String district = addressComponent.get("district").asText();
					String province = addressComponent.get("province").asText();
					String street = addressComponent.get("street").asText();
					String streetNumber = addressComponent.get("street_number").asText();
					String countryCode = addressComponent.get("country_code").asText();
					
					Map<String, String> ret = new HashMap<String, String>();
					ret.put("cityCode", cityCode);
					ret.put("address", address);
					ret.put("sematicDesc", sematicDesc);
					ret.put("adcode", adcode);
					ret.put("city", city);
					ret.put("country", country);
					ret.put("direction", direction);
					ret.put("distance", distance);
					ret.put("district", district);
					ret.put("province", province);
					ret.put("street", street);
					ret.put("streetNumber", streetNumber);
					ret.put("countryCode", countryCode);
					
					return ret;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getDistance(double originLng, double originLat, double destLng, double destLat, 
			String mode, String originRegion, String destRegion){
		
		if(StringUtils.isBlank(ak)){
			logger.error("调用百度地图获取两点间距离失败，错误原因:没有设置baidu_api_ak");
			return Integer.MAX_VALUE;
		}
		
		if(StringUtils.isBlank(mapApiUrl)){
			logger.error("调用百度地图获取两点间距离失败，错误原因:没有设置mapApiUrl");
			return Integer.MAX_VALUE;
		}
		
		if(StringUtils.isBlank(distanceUrl)){
			logger.error("调用百度地图获取两点间距离失败，错误原因:没有设置addressUrl");
			return Integer.MAX_VALUE;
		}
		
		String url = mapApiUrl + distanceUrl;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mode", mode);
		params.put("origin", originLat+","+originLng);
		params.put("destination", destLat+","+destLng);
		params.put("origin_region", originRegion);
		params.put("destination_region", destRegion);
		params.put("output", "json");
		params.put("ak", ak);
		
		HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
		try {
			String responseText = httpProxy.doGet();
			
			if(responseText != null){
				JsonNode response = objectMapper.readTree(responseText);
				if(response.get("status").asInt() == 0){
					if(response.get("result") != null){
//						return response.get("result").get("routes").get(0).get("distance").asInt();
						JsonNode routes = response.get("result").get("routes");
						if(routes != null){
							JsonNode routes0 = routes.get(0);
							if(routes0 != null){
								JsonNode distance = routes0.get("distance");
								if(distance != null){
									return distance.asInt();
								} else {
									logger.warn("调用百度地图获取两点间距离失败, distance is null");
								}
							} else {
								logger.warn("调用百度地图获取两点间距离失败, routes0 is null");
							}
						} else {
							logger.warn("调用百度地图获取两点间距离失败, routes is null");
						}
					}
				}
				else{
					logger.warn("调用百度地图获取两点间距离失败, 返回结果:" + responseText);
				}
			}
			else{
				logger.warn("调用百度地图获取两点间距离失败, 没有结果返回...");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.MAX_VALUE;
	}
	
	public static void main(String[] args){
		BaiduMapServiceI baiduMapService = new BaiduMapServiceImpl();
		double distance = baiduMapService.getDistance(113.340789, 23.125757, 113.362887,23.123144, "walking", "广州", "广州");
		System.out.println(distance);
	}
	
}
