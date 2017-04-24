package com.wm.service.org;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.org.OrgEntity;

public interface OrgServiceI extends CommonService{

	/**
	 * 递归找此orgId下的，包括此orgId的orgId的列表
	 * @param orgId
	 * @return
	 */
	public List<String> recursionQueryOrgIdList(int orgId, boolean containSelf);
	
	/**
	 * 查找所有子机构
	 * @param orgList
	 * @param rootId
	 * @return
	 */
	public List<OrgEntity> findChildOrgList(List<OrgEntity> orgList, Integer rootId);

	/**
	 * 查找该快递员的所有父级组织架构
	 * @param courierId
	 * @return
	 */
	public List<Integer> findLeaderOrgs(Integer courierId);

	/**
	 * 判断某网点，某天是否有人上班
	 * @param orgId
	 * @param date
	 * @return
	 */
	public Boolean isOrgOnDuty(Integer orgId, String date);

	/**
	 * 统计某网点，某天上班人数
	 * @param subOrgId
	 * @param date
	 * @return
	 */
	public Integer orgOnDutyPerson(Integer orgId, String date);
	
	/**
	 * 根据组织架构获取快递员：查询与指定快递员在同一级别组织架构下的快递员列表
	 * 比如：level = 5 就是获取Id为courierId的快递员所在片区的所有快递员列表
	 * @param courierId 快递员ID
	 * @param level 组织架构级别， 6 网点 5 片区 4 区 3 市 2 省 
	 * @return
	 */
	public List<Integer> getCourierIdByOrgLevel(Integer courierId, Integer level);
	
	/**
	 *根据快递员id获取快递员所属的组织架构 
	 * @param courierId 快递员id
	 * @return
	 */
	public Map<Integer, OrgEntity> getCourierBlongOrg(Integer courierId);
	
	/**根据快递员id，所属区域等级，获取快递员所属区域id 
	 * @param courierId 快递员id
	 * @param level  机构等级
	 * @return
	 */
	public Map<String, Object>  getCourierBlongAreaId(Integer courierId, Integer level);
	
	/**
	 * 根据机构等级获取机构id和机构名称
	 * @param level 机构等级
	 * @return
	 */
	public List<Map<String, Object>> getOrgIdAndName(Integer level);
	
	/**
	 * 根据快递员id获取可上班的网点经纬度
	 * @param courierId 快递员id
	 * @return
	 */
	public List<Map<String, Object>> getOrgLongitudeAndLatitude(int courierId);
	
	/**
	 * 获取快递员绑定的网点
	 * @param courierId 快递员id
	 * @return
	 */
	public List<Map<String, Object>> getOrgByCourierId(Integer courierId, int page, int rows);

	/**
	 * 根据pId获取机构id和机构名称
	 * @param pId
	 * @return
	 */
	public List<Map<String, Object>> getOrgIdAndNameByPid(Integer pId);
	
}
