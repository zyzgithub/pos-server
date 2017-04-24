package com.courier_mana.personal.service;

import java.util.Map;

public interface CourierRegionService {
	
	/**
	 * 获取当前用户所在机构
	 * @param courierId 快递员ID
	 * @return	id:		机构ID
	 * 			orgName:所在机构名称
	 * 			pid:	上级机构ID
	 * 			level:	机构级别
	 * 			orgPath:机构层级关系
	 */
	public abstract Map<String, Object> getCourierOrgInfo(Integer courierId);
	
	/**
	 * 获取指定机构的详情
	 * @param orgId 机构ID
	 * @return	orgName:所在机构名称
	 * 			pid:	上级机构ID
	 * 			level:	机构级别
	 * 			orgPath:机构层级关系
	 */
	public abstract Map<String, Object> getOrgInfo(Integer orgId);
	
	/**
	 * 获取快递员(管理员)管辖范围
	 * @param courierId
	 * @return	city:		城市
	 * 			district:	区
	 * 			area:		片区
	 * 			network:	网点
	 */
	public abstract Map<String, Object> getAdminRegion(Integer courierId);
}
