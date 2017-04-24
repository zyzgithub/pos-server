package com.wm.dao.org.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;
import org.testng.collections.Lists;

import com.wm.dao.org.OrgDao;
import com.wm.entity.org.OrgEntity;

@Repository("orgDao")
public class OrgDaoImpl extends GenericBaseCommonDao<OrgEntity, Integer> implements OrgDao{

	@Override
	public List<String> recursionQueryOrgIdList(Integer orgId, boolean containSelf) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT group_concat(id) as list  FROM 0085_org where FIND_IN_SET(pid, ? ) ");
		List<Map<String,Object>> list = this.findForJdbc(sbsql.toString(), orgId);
		List<String> result = Lists.newArrayList();
		if(containSelf){
			result.add(String.valueOf(orgId));
		}
		while(list != null && list.size() > 0){
			if(list.get(0) != null){
				Map<String, Object> map = list.get(0);
				if(map != null && map.get("list") != null){
					String obj = map.get("list").toString();
					String[] strs = StringUtils.split(obj, ",");
					if(strs != null){
						for (int i = 0; i < strs.length; i++) {
							result.add(StringUtils.trim(strs[i]));
						}
					}
					list = this.findForJdbc(sbsql.toString(), obj);
				}else{
					break;
				}
			}
		}
		return result;
	}


	//递归查询网点对应的orgId
	public List<String> recursionQueryWangDianOrgIdList(int orgId, boolean containSelf) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT group_concat(id) as list  FROM 0085_org where FIND_IN_SET(pid, ? ) ");
		List<Map<String,Object>> list = this.findForJdbc(sbsql.toString(), orgId);
		List<String> result = Lists.newArrayList();
		if(containSelf){
			result.add(String.valueOf(orgId));
		}
		while(list != null && list.size() > 0){
			if(list.get(0) != null){
				Map<String, Object> map = list.get(0);
				if(map != null && map.get("list") != null){
					String obj = map.get("list").toString();
					String[] strs = StringUtils.split(obj, ",");
					if(strs != null){
						for (int i = 0; i < strs.length; i++) {
							result.add(StringUtils.trim(strs[i]));
						}
					}
					list = this.findForJdbc(sbsql.toString(), obj);
				}else{
					break;
				}
			}
		}
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryFirstChildrenOrgs(Integer orgId, Integer courierId, Integer page, Integer rows) {
		//查用户对应的区域孩子id列表
		StringBuffer sbsql = new StringBuffer();
			sbsql.append(" SELECT ");
			sbsql.append(" 	org.org_name, ");
			sbsql.append(" 	org.area_code, ");
			sbsql.append(" 	org.`level` AS org_level, ");
			sbsql.append(" 	org.pid AS org_pid, ");
			sbsql.append(" 	org.id ");
			sbsql.append(" FROM ");
			sbsql.append(" 	0085_org as org ");
			if(orgId == null){
				sbsql.append(" 	, 0085_courier_org AS cour_org ");
			}
			sbsql.append(" WHERE 1=1 ");
			sbsql.append(" AND org.`status` = 1 ");// 表示有效
		List<Object> params = Lists.newArrayList();
		if(orgId != null){
			sbsql.append(" AND org.pid = ? ");
			params.add(orgId);
		}else{
			sbsql.append(" AND org.id = cour_org.org_id ");
			sbsql.append(" AND cour_org.courier_id = ? ");
			params.add(courierId);
		}
		sbsql.append(" ORDER BY org.`level`, org.sort  ");
		List<Map<String,Object>> list = this.findForJdbcParam(sbsql.toString(), page, rows, params.toArray());
		if(orgId == null && list != null && list.size() > 0){
			// 只返回一个最大的区域
			list = Lists.newArrayList(list.get(0));
		}
		return list;
	}
	
}
