package com.wm.service.building;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface BuildingServiceI extends CommonService{

	Map<String, Object> getCourierServArea(String courierId);

	
	/**
	 * 查询快递员对应网点的大厦
	 * 只能使用在物流组长或最小单位为网点的时候
	 * @param courierId
	 * @param page 从1开始
	 * @param rows
	 * @return
	 */
	List<Map<String, Object>> queryBuildingsByCourierId(Integer courierId,
			Integer page,
			Integer rows);
	
}
