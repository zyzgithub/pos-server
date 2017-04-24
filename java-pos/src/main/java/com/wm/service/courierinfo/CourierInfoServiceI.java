package com.wm.service.courierinfo;


import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

public interface CourierInfoServiceI extends CommonService{
	
	/**
	 * 根据快递员id获取快递员类型
	 * @param userId
	 * @return
	 */
	public Integer getCourierTypeByUserId(Integer userId);
	
	/**
	 * 根据快递员id获取绑定的合作商id
	 * @param courierId
	 * @return
	 */
	public Integer getCourierBindUserId(Integer courierId);
	
	/**
	 * 根据快递员类型获取快递员id
	 * @param courierType
	 * @return
	 */
	public List<Integer> getCouriersByCourierType(Integer courierType);
}
