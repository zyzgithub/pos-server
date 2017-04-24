package com.wm.service.couriertrainresource;

import java.util.List;
import java.util.Map;

public interface CourierTrainResourceServiceI {
	
	/**
	 * 分页获取培训资料类型
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getCourierTrainType(int page, int rows);
	
	/**
	 * 根据快递员类型和培训资料的类型， 分页获取培训资料名称
	 * @param courierType
	 * @param trainType
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getCourierTrainInfo(Integer courierType, Integer trainType, int page, int rows);
	
	/**
	 * 根据id获取培训资料url
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCourierTrainUrl(Integer id);

}
