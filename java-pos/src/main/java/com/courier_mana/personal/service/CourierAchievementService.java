package com.courier_mana.personal.service;

import java.util.List;
import java.util.Map;

public interface CourierAchievementService {
	
	
	/**
	 * 单个节点 排名前3的快递员
	 * @param courierId 快递员Id
	 * @param nodeType  节点类型
	 * @return  list<Map<String,String>>
	 */
	public abstract List<Map<String,Object>> queryNodeRanking(Long courierId,String nodeType);
	
	/**
	 * 获取快递员(管理员)个人信息
	 * 如果查找不到快递员的信息方法返回null
	 * @param courierId 快递员ID
	 * @return	id			快递员ID
	 * 			username	快递员名称
	 * 			photoUrl	头像Url
	 * 			positionName职位名称		
	 * 			orgName		所在机构名称
	 */
	public abstract Map<String, Object> getCourierInfo(Integer courierId);
	
	/**
	 * 获取快递员排名
	 * @param courierId		快递员ID
	 * @param startDate		开始时间
	 * @param endDate		结束时间
	 * @param isRankByArea	按区域排名
	 * @param page			页数
	 * @param rowsPerPage	每页记录数
	 * @return	courier_id	快递员ID
	 * 			username	快递员姓名
	 * 			photoUrl	快递员头像Url
	 * 			total		订单数
	 * 			orgName		快递员所在机构
	 */
	public abstract List<Map<String, Object>> getCouriersRank(Integer courierId,
			String startDate, String endDate, Boolean isRankByArea,
			Integer page, Integer rowsPerPage);
	
	/**(OvO)
	 * 获取快递员排名(合作商)
	 * @param courierId		快递员ID
	 * @param startDate		开始时间
	 * @param endDate		结束时间
	 * @param isRankByArea	按区域排名
	 * @param page			页数
	 * @param rowsPerPage	每页记录数
	 * @return	courier_id	快递员ID
	 * 			username	快递员姓名
	 * 			photoUrl	快递员头像Url
	 * 			total		订单数
	 * 			orgName		快递员所在机构
	 */
	public abstract List<Map<String, Object>> getCouriersRank4Agent(Integer courierId,
			String startDate, String endDate,
			Integer page, Integer rowsPerPage);
	
	/**
	 * 获取我的排名
	 * @param courierId 	快递员ID
	 * @param startDate		开始时间
	 * @param endDate		结束时间
	 * @param isRankByArea	按区域排名
	 * @return	total		订单数
	 * 			rank		排名
	 */
	public abstract Map<String, Object> getMyRank(Integer courierId, String startDate, 
			String endDate, Boolean isRankByArea);
}
