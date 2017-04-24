package com.courier_mana.examples.service;

import java.util.List;
import java.util.Map;


/**
 * 
 * 快递员组织架构服务接口
 *
 */
public interface CourierOrgServicI {
	
	/**
	 * 根据机构id和用户Id查询管辖范围内的机构ID列表    加强版
	 * @param courierId  用户Id
	 * @param orgId      机构Id
	 * @return
	 */
	public List<Integer> getManageOrgIdsTwo(Integer courierId,Integer orgId);
	
	/**
	 * 根据快递员ID获取其管辖的快递员ID列表  加强版
	 * @param courierId 用户id
	 * @return
	 */
	public List<Integer> getManageCouriersIdTwo(Integer courierId);
	
	/**
	 * 根据机构ID快递员ID获取其管辖的快递员Id列表
	 * @param courierId 用户Id
	 * @param orgId     机构Id
	 * @return
	 */
	public List<Integer> getManageCouriersIdTwo(Integer courierId, Integer orgId);
	
	/**
	 * 根据快递员ID获取合作商其管辖的快递员列表
	 * @param courierId 用户id
	 * @return
	 */
	public List<Map<String,Object>> getPartnerCouriers(Integer courierId);
	
	/**
	 * 查询合作商管辖的快递员
	 * @param userId  用户ID
	 * @return
	 */
	public List<Integer> getPartnerUserId(Integer userId);
	
	/**
	 * 根据快递员ID获取其管辖的机构列表
	 * @param courierId
	 * @return
	 */
	List<Map<String, Object>> getManageOrgs(Integer courierId);
	
	/**
	 * 根据快递员ID获取其管辖的机构列表
	 * @param courierId
	 * @return
	 */
	List<Map<String, Object>> getManageOrgs2(Integer courierId);
	
	/**
	 * 根据快递员ID获取其管辖的机构列表
	 * @param courierId
	 * @return
	 */
	List<Integer> getManageOrgIds(Integer courierId);
	
	/**
	 * 获取用户所在的机构
	 * @param courierId 快递员ID
	 * @return
	 */
	Map<String, Object> getParentOrg(Integer courierId);
	
	/**
	 * 获取用户所在的机构Id
	 * @param courierId
	 * @return
	 */
	Integer getParentOrgId(Integer courierId);
	
	/**
	 * 根据快递员ID获取其管辖的快递员列表
	 * @param courierId
	 * @return
	 */
	List<Map<String, Object>> getManageCouriers(Integer courierId);
	
	/**
	 * 根据快递员ID获取其管辖的快递员ID列表
	 * @param courierId
	 * @return
	 */
	public List<Integer> getManageCouriersId(Integer courierId);
	
	/**
	 * 根据机构ID快递员ID获取其管辖的机构Id列表
	 * @param courierId
	 * @param orgId
	 * @return
	 */
	List<Integer> getManageOrgIds(Integer courierId,Integer orgId);
	/**
	 * 根据机构ID快递员ID获取其管辖的快递员Id列表
	 * @param courierId
	 * @param orgId
	 * @return
	 */
	List<Integer> getManageCouriersId(Integer courierId,Integer orgId);
	
	/**
	 * 返回快递员管辖的下一级所有机构
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getManageNextOrgs(Integer orgId);

	/**
	 * 根据机构ID快递员ID获取其管辖下的网点列表
	 * @param courierId
	 * @param orgId
	 * @return
	 */
    public List<Map<String, Object>> getDotRights(Integer courierId, Integer orgId);
	
	/**
	 * 查询当前机构
	 * @param courierId
	 * @return
	 */
	public Map<String, Object> getManageCurrentOrgs(Integer orgId);
	
	/**(OvO)
	 * 查询各机构(只显示网点level = 6)的信息
	 * @return	返回各网点的基本信息, 包括:
	 * 		id				机构ID
	 * 		orgName			机构名称
	 * 		latitude		网点纬度
	 * 		longitude		网点经度
	 * 		merchantCount	网点下商铺数量
	 * 		courierCount	网点下快递员数量
	 */
	public List<Map<String, Object>> getOrgsInfo();
	
	/**
	 * 根据机构id和用户Id查询管辖范围内的机构ID列表 	加强版
	 * @param courierId  用户ID
	 * @return
	 */
	public List<Integer> getManagerOrgIdsTwo(Integer courierId);
	
	/**
	 * 根据机构ID获取快递员ID
	 * @param orgIds	机构ID List
	 * @return
	 */
	public abstract List<Integer> getCourierIdsByOrgId(String orgIdsStr);
	
	/**
	 * 根据快递员ID获取管辖范围内所有level6的机构ID
	 * @author hyj
	 * @param courierId	快递员ID
	 * @return	level6 的机构ID列表
	 */
	public abstract List<Integer> getManageL6OrgId(Integer courierId);
}
