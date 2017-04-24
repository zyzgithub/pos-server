package com.wm.service.impl.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.common.Constants;
import com.wm.dao.org.OrgDao;
import com.wm.entity.org.OrgEntity;
import com.wm.service.courierorg.CourierOrgServiceI;
import com.wm.service.org.OrgServiceI;

@Service("orgService")
@Transactional
public class OrgServiceImpl extends CommonServiceImpl implements OrgServiceI {
	
	private static final Logger logger = Logger.getLogger(OrgServiceImpl.class);
	
	@Autowired
	private OrgDao orgDao;
	
	@Autowired
	private CourierOrgServiceI courierOrgServiceI;

	@Override
	public List<String> recursionQueryOrgIdList(int orgId, boolean containSelf) {
		return this.orgDao.recursionQueryOrgIdList(orgId, containSelf);
	}
	
	public List<OrgEntity> findChildOrgList(List<OrgEntity> orgList, Integer rootId) {
		List<OrgEntity> orgs = new ArrayList<OrgEntity>();
		for (OrgEntity org : orgList) {
			Integer orgId = org.getId();
			Integer pid = org.getPid();
			if(rootId.equals(orgId)){
				orgs.add(org);
			} else if (rootId.equals(pid)) {
				List<OrgEntity> childOrgs = findChildOrgList(orgList, orgId);
				orgs.addAll(childOrgs);
			}
		}
		return orgs;
	}

	@Override
	public List<Integer> findLeaderOrgs(Integer courierId) {
		List<Integer> leaderOrgs = new ArrayList<Integer>();
		String sql1 = "select o.id,o.pid,o.level from 0085_org o left join 0085_courier_org co on co.org_id=o.id where co.courier_id=? and o.status=1 ";
		List<Map<String, Object>> list = this.findForJdbc(sql1, new Object[]{courierId});
		if(list != null && list.size() > 0){
			Map<String, Object> map = list.get(0);
			Integer pId = Integer.parseInt(map.get("pid").toString());
			findParentOrgs(leaderOrgs, pId);
		} else {
			logger.warn("未找到该快递员【" + courierId + "】所属组织架构");
		}
		return leaderOrgs;
	}

	/**
	 * 递归查找父节点
	 * @param leaderOrgs
	 * @param pId
	 */
	private void findParentOrgs(List<Integer> leaderOrgs, Integer pId) {
		if(pId != null && pId != 0){
			leaderOrgs.add(pId);
			OrgEntity pOrg = this.findUniqueByProperty(OrgEntity.class, "id", pId);
			if(pOrg != null){
				findParentOrgs(leaderOrgs, pOrg.getPid());
			}
		}
	}

	@Override
	public Boolean isOrgOnDuty(Integer orgId, String date) {
		String sql = "select count(ca.id) from 0085_courier_attendance ca where from_unixtime(ca.create_time,'%Y-%m-%d')=? and ca.user_id in ";
		sql += "(select co.courier_id from 0085_courier_org co where co.org_id=?)";
		Long count = this.getCountForJdbcParam(sql, new Object[]{date, orgId});
		return count > 0 ? true : false;
	}

	@Override
	public Integer orgOnDutyPerson(Integer orgId, String date) {
		String sql = "select count(distinct(ca.user_id)) from 0085_courier_attendance ca ";
		sql += " where from_unixtime(ca.create_time,'%Y-%m-%d')=? and ca.user_id in  ";
		sql += " (select co.courier_id from 0085_courier_org co where co.org_id=?)";
		Long count = this.getCountForJdbcParam(sql, new Object[]{date, orgId});
		return count.intValue();
	}

	@Override
	public List<Integer> getCourierIdByOrgLevel(Integer courierId, Integer level) {
 		Map<Integer, OrgEntity> levelMap = getCourierBlongOrg(courierId);
 		if(levelMap.get(level) != null){
 			Integer id = levelMap.get(level).getId();
 	 		if(id == null){
 	 			logger.info("该机构等级为: " + level + " 您不在该机构下");
 	 		}
 			return courierOrgServiceI.queryCouriersByParentOrgId(id);		
 		}
 		else {
			return new ArrayList<Integer>();
		}
 		
 			
	}
	
	@Override
	public Map<Integer, OrgEntity>  getCourierBlongOrg(Integer courierId){
		Map<Integer, OrgEntity> result = new HashMap<Integer, OrgEntity>();		
		StringBuilder query = new StringBuilder();
		query.append("select id, p_path, level ");
		query.append(" from 0085_courier_org co");
		query.append(" left join 0085_org o on co.org_id = o.id ");
		query.append(" where courier_id = ? ");
		Map<String, Object> pathLevelMap = findOneForJdbc(query.toString(), courierId);
//		if(!pathLevelMap.get("level").equals(Constants.BRANCH_LEVEL)){
//			logger.info("id为 ：" + courierId + " 的快递员没有绑定网点");			
//		}
		if(pathLevelMap != null){
			List<Map<String, Object>> list = null;
			String path = pathLevelMap.get("p_path") == null ?"": pathLevelMap.get("p_path").toString();
			Integer id = Integer.valueOf(pathLevelMap.get("id").toString());
			if(StringUtils.isBlank(path)){
				list = findForJdbc("select id, level, org_name, pid, p_path, status, area_code from 0085_org where id = ?", id);
			}
			else {
				String [] pathArr = path.split("_");
				String sql = "select id, level, org_name, pid, p_path, status, area_code from 0085_org where id  in (" + StringUtils.join(pathArr, ",") + "," + id +" )";
				list = this.findForJdbc(sql);
			}
			
			if(CollectionUtils.isNotEmpty(list)){
				for (Map<String, Object> map: list) {
					int level = Integer.parseInt(map.get("level").toString());
					int orgId = Integer.parseInt(map.get("id").toString());
					String orgName = map.get("org_name").toString();
					int pid = Integer.parseInt(map.get("pid").toString());
					String p_path = map.get("p_path").toString();
					int status = Integer.parseInt(map.get("status").toString());
					String area_code = map.get("area_code").toString();
					OrgEntity orgEntity = new OrgEntity();
					orgEntity.setAreaCode(area_code);
					orgEntity.setId(orgId);
					orgEntity.setLevel(level);
					orgEntity.setOrgName(orgName);
					orgEntity.setPid(pid);
					orgEntity.setPPath(p_path);
					orgEntity.setStatus(status);
					result.put(level, orgEntity);
				}
			}		
		}
		return result;
		
	}

	/**根据快递员id，所属区域等级，获取快递员所属区域id
	 * @param courierId
	 * @param level
	 * @return
	 */
	@Override
	public Map<String, Object> getCourierBlongAreaId(Integer courierId, Integer level) {
		StringBuilder query = new StringBuilder();
		query.append("select id, CASE when p_path = '' THEN NULL ELSE p_path END p_path, level ");
		query.append(" from 0085_courier_org co");
		query.append(" left join 0085_org o on co.org_id = o.id ");
		query.append(" where courier_id = ? ");
		Map<String, Object> pathLevelMap = findOneForJdbc(query.toString(), courierId);
		if(pathLevelMap == null){
			return null;
		}
		
		if(pathLevelMap.get("level").equals(level)){
			Map<String, Object> cityIdMap = new HashMap<String, Object>();
			cityIdMap.put("id", pathLevelMap.get("id"));
			return cityIdMap;
		}
		if(pathLevelMap.get("p_path") != null){
			String [] pathArr = pathLevelMap.get("p_path").toString().split("_");
			query = new StringBuilder("select id from 0085_org where id  in (" + StringUtils.join(pathArr, ",") + " ) and level = ?");
			return findOneForJdbc(query.toString(), level);
		}
		else{
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getOrgIdAndName(Integer level) {
		StringBuilder query = new StringBuilder();
		query.append(" select id cityId, org_name orgName from 0085_org");
		query.append(" where level = ?");
		return findForJdbc(query.toString(), level);
	}
	
	@Override
	public List<Map<String, Object>> getOrgIdAndNameByPid(Integer pId) {
		StringBuilder query = new StringBuilder();
		query.append(" select id orgId, org_name orgName from 0085_org");
		query.append(" where pid = ?");
		return findForJdbc(query.toString(), pId);
	}

	@Override
	public List<Map<String, Object>> getOrgLongitudeAndLatitude(int courierId) {
		List<Map<String, Object>> longitudeAndLatitudeList =new ArrayList<Map<String,Object>>();
		StringBuilder query = new StringBuilder();
		query.append(" select o.id , o.p_path, o.level from 0085_courier_org co");
		query.append(" left join 0085_org o on o.id = co.org_id");
		query.append(" where co.courier_id = ?");
		List<Map<String, Object>> orgList = findForJdbc(query.toString(), courierId);
		if(CollectionUtils.isEmpty(orgList)){
			logger.warn("快递员" + courierId + "没有绑定机构");
		}
		List<Integer> orgIds = new ArrayList<Integer>();
		if(CollectionUtils.isNotEmpty(orgList)){
			for(Map<String, Object> orgMap : orgList){
				//如果已经是网点
				if(orgMap.get("level").equals(Constants.BRANCH_LEVEL)){
					orgIds.add(Integer.valueOf(orgMap.get("id").toString()));
				}
				else {
					//不是网点，找到该机构下的网点id
					List<Map<String, Object>> branchOrgIds =findForJdbc( "select id from 0085_org where p_path like ? and level = 6", 
							orgMap.get("p_path").toString() + orgMap.get("id").toString() + "_" + "%");
					for(Map<String, Object> map : branchOrgIds){
						orgIds.add(Integer.valueOf(map.get("id").toString()));
					}
				}
			}
			//找到所有可以打卡的位置
			for(Integer integer : orgIds){
				List<Map<String, Object>> list = findForJdbc("select longitude, latitude from 0085_org where id = ?", integer );
				longitudeAndLatitudeList.addAll(list);
			}
		}
		//公司总部经纬度
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("longitude", 113.362536);
		map.put("latitude", 23.123214);
		longitudeAndLatitudeList.add(map);
		return longitudeAndLatitudeList;
	}

	@Override
	public List<Map<String, Object>> getOrgByCourierId(Integer courierId, int page, int rows) {
		StringBuilder query = new StringBuilder();
		query.append(" select org_name orgName, ifnull(address, '') address");
		query.append(" from 0085_org o");
		query.append( " left join 0085_courier_org co on o.id = co.org_id");
		query.append(" where co.courier_id = ?");
		return findForJdbcParam(query.toString(), page, rows, courierId);
	}
	
}