package com.wm.service.baidu;

import java.util.List;
import java.util.Map;

public interface BaiduMapServiceI {
	
	int DEFALUT_RADIUS = 5000; 
	/**
	 * 根据名称查询以某一经纬度（lng, lat）为半径地址列表
	 * @param name 搜索的关键字
	 * @param lng 经度
	 * @param lat 纬度
	 * @param radius 半径（单位：米）
	 * @return
	 */
	List<Map<String, Object>> searchAddressByRadis(String name,
			double lng, double lat, long radius);
	
	/**
	 * 根据经纬度（lng, lat）获取对于的地址细节
	 * @param lng
	 * @param lat
	 * @return
	 */
	Map<String, String> getAddressDetail(double lng, double lat);
	/**
	 * 获取地图两点之间的距离(单位：米)
	 * @param originLng
	 * @param originLat
	 * @param destLng
	 * @param destLat
	 * @param mode
	 * @param originRegion
	 * @param destRegion
	 * @return
	 */
	int getDistance(double originLng, double originLat, double destLng, double destLat, 
			String mode, String originRegion, String destRegion);
}
