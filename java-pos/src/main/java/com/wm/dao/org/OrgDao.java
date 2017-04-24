package com.wm.dao.org;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface OrgDao extends IGenericBaseCommonDao{


	/**
	 * 递归找此orgId下的，包括此orgId的子节点列表
	 * @param orgId 区域id
	 * @param containSelf 是否包含传入的orgId到返回列表中
	 * @return 返回orgid列表
	 */
	public List<String> recursionQueryOrgIdList(Integer orgId, boolean containSelf);

	/**
	 * 查询First children区域列表
	 * @param orgId 区域id
	 * @param courierId 快递员id
	 * @return 返回orgid列表
	 */
	public List<Map<String, Object>> queryFirstChildrenOrgs(Integer orgId, Integer courierId, Integer page, Integer rows );

}
