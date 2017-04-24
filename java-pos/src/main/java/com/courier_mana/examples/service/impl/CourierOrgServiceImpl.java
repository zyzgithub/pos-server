package com.courier_mana.examples.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.common.StringUtil;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.wm.util.IntegerUtil;


@Service
public class CourierOrgServiceImpl extends CommonServiceImpl implements CourierOrgServicI {
	private static final Logger logger = LoggerFactory.getLogger(CourierOrgServiceImpl.class);
	
	/**
	 * 查询合作商管辖的快递员
	 */
	public List<Integer> getPartnerUserId(Integer userId){
		String sql = "SELECT a.courier_id id from 0085_courier_info a INNER JOIN `user` b on a.courier_id=b.id\n" +
				   "where a.bind_user_id = "+ userId +"";
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>();
		for(Map<String, Object> map:list){
			result.add((Integer)map.get("id")) ;
		}
		return result;
	}
	
	/**
	 * 查询快递员所管辖所有的机构，如果自己就属于网点了，查询结果为空
	 */
	@Override
	public List<Map<String, Object>> getManageOrgs(Integer courierId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		StringBuilder queryOrgSql = new StringBuilder();
		queryOrgSql.append(" select *  ");
		queryOrgSql.append(" from 0085_org o");
		queryOrgSql.append("  where ");
		queryOrgSql.append("  o.p_path like ( ");
		queryOrgSql.append("    select CONCAT(o.p_path, CONCAT(o.id, '_%'))");
		queryOrgSql.append("    from 0085_org o ");
		queryOrgSql.append("	where o.id =  ");
		queryOrgSql.append("		(");
		queryOrgSql.append("		 SELECT  o.org_id");
		queryOrgSql.append(" 		 from 0085_courier_org o");
		queryOrgSql.append(" 		 where o.courier_id=?");
		queryOrgSql.append("	) and o.status = 1 ");
		queryOrgSql.append(") and o.status = 1 ");
		
		return findForJdbc(queryOrgSql.toString(), courierId);
	} 
	
	private List<Integer> getChildOrgs(String pPath){
		StringBuilder sql = new StringBuilder(" SELECT o.id FROM 0085_org o WHERE o.p_path REGEXP '");
		sql.append(pPath);
		sql.append("' ");
		return findListbySql(sql.toString());
	}
	
	@Override
	public List<Map<String, Object>> getManageOrgs2(Integer courierId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		//获取用户所在的机构
		Map<String, Object> orgMap = getParentOrg(courierId);
		if(orgMap == null){
			logger.error("无法根据courierId=" + courierId + "所属机构");
			return new ArrayList<Map<String, Object>>();
		}
		
		//根据机构路径查询其所有的下一级机构
		String parentOrgPath = orgMap.get("p_path").toString()+ orgMap.get("id") + "_%";
		String sql = "select * from 0085_org o where o.p_path like ? and o.status = 1 ";
		return findForJdbc(sql.toString(), parentOrgPath);
	} 
	
	@Override
	public List<Integer> getManageOrgIds(Integer courierId){
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		List<Integer> orgIds = new ArrayList<Integer>();
		
		//查询快递员自己所在的机构
		Map<String, Object> orgMap = getParentOrg(courierId);
		if(orgMap == null){
			logger.error("无法根据courierId=" + courierId + "所属机构");
			throw new IllegalArgumentException("权限不足");
		}else{
			//如果快递员属于网点经理，就拿自己所在的网点机构ID，否则就拿管辖的机构ID
			if("6".equals(String.valueOf(orgMap.get("level")))){
				orgIds.add(Integer.parseInt(orgMap.get("id").toString()));
			}else{
				Integer orgId = Integer.parseInt(orgMap.get("id").toString());
				orgIds.add(orgId);
				/*
				 * 查出子区域ID
				 */
				StringBuilder pPath = new StringBuilder("^");
				pPath.append(orgMap.get("p_path").toString());
				pPath.append(orgId);
				pPath.append("_");
				List<Integer> childOrgIds = this.getChildOrgs(pPath.toString());
				/*
				 * 将查询结果放到结果集
				 */
				orgIds.addAll(childOrgIds);
			}
		}
		return orgIds;
	}
	
	@Override
	public Map<String, Object> getParentOrg(Integer courierId){
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select  o.id, o.p_path, o.level ");
		sql.append(" from  0085_courier_org co, 0085_org o");
		sql.append(" where co.org_id = o.id and co.courier_id=? and o.status = 1 ");
		
		return this.findOneForJdbc(sql.toString(), courierId);
	}
	
	@Override
	public Integer getParentOrgId(Integer courierId){
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select co.org_id from 0085_courier_org co ");
		sql.append(" where co.courier_id=?");
		return this.findOneForJdbc(sql.toString(), Integer.class, courierId);
	}
	
	@Override
	public List<Map<String, Object>> getManageCouriers(Integer courierId){
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		//查询快递员管理的机构ID列表
		List<Integer> orgIds = getManageOrgIds(courierId);
		String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
		
		List<Integer> courierIds = this.getCourierIdsByOrgId(orgIdsStr); 
		
		StringBuilder queryCouriersSql = new StringBuilder();
		queryCouriersSql.append("select * from `user` where id in (" + StringUtils.join(courierIds, ",") + ")");
		
		//查询用户列表
		return this.findForJdbc(queryCouriersSql.toString());
	}
	
	/**
	 * 根据快递员ID获取合作商其管辖的快递员列表
	 * @param courierId  用户id
	 * @return
	 */
	public List<Map<String,Object>> getPartnerCouriers(Integer courierId){
		String sql = "select b.id,b.username from 0085_courier_info a INNER JOIN `user` b on a.courier_id=b.id where a.bind_user_id = "+ courierId +"";
		
		return this.findForJdbc(sql);
	}
	
	/**
	 * 根据快递员ID获取其管辖的快递员ID列表
	 * @param courierId
	 * @return
	 */
	@Override
	public List<Integer> getManageCouriersId(Integer courierId){
		List<Integer> courierIds = new ArrayList<Integer>();
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询快递员管理的机构ID列表
		List<Integer> orgIds = getManageOrgIds(courierId);
		
		String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
		
		courierIds = getCourierIdsByOrgId(orgIdsStr);
		courierIds.add(courierId);
		return courierIds;
	}
	
	/**
	 * 根据快递员ID获取其管辖的快递员ID列表  加强版
	 * @param courierId
	 * @return
	 */
	public List<Integer> getManageCouriersIdTwo(Integer courierId){
		if(!IntegerUtil.isEmpty(courierId)){
			throw new IllegalArgumentException("");
		}
		String sql = "SELECT  DISTINCT o.courier_id id   from 0085_courier_org o  where o.org_id in (\n" +
					 "	select o.id from 0085_org o where o.p_path like (select CONCAT(o.p_path,o.id,'_%') path from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id = "+ courierId +") and o.status = 1 and o.`level` !=6 ) and o.status = 1\n" +
					 "					 	UNION ALL\n" +
					 "	select o.id from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id =  "+ courierId +")  and o.status = 1 and o.`level` = 6 \n" +
					 ")";
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>();
		for(Map<String,Object> map :list){
			result.add(IntegerUtil.objToInteger(map.get("id")));
		}
		return result;
	}
	
	/**
	 * 根据机构id和用户Id查询管辖范围内的机构ID列表 	加强版
	 */
	public List<Integer> getManagerOrgIdsTwo(Integer courierId){
		String sql = "select o.id from 0085_org o where o.p_path like (select CONCAT(o.p_path,o.id,'_%') path from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id = "+ courierId +") and o.status = 1 and o.`level` !=6 ) and o.status = 1\n" +
					 "	UNION ALL\n" +
					 "	select o.id from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id = "+ courierId +") and o.status = 1 and o.`level` = 6 ";
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>(list.size());
		for(Map<String, Object> map:list){
			result.add((Integer)map.get("id"));
		}
		return result;
	}
	
	/**
	 * 根据机构id和用户Id查询管辖范围内的机构ID列表    加强版
	 * @param courierId
	 * @param orgId
	 * @return
	 */
	public List<Integer> getManageOrgIdsTwo(Integer courierId,Integer orgId){
		if(orgId == null){
			throw new IllegalArgumentException("orgId=null");
		}
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		String sql = "select o.id from 0085_org o where o.id = "+ orgId +" and o.status = 1 and o.`level` = 6\n" +
					 " UNION ALL\n" +
					 " select o.id from 0085_org o where o.p_path like (select CONCAT(o.p_path,o.id,'_%') path from 0085_org o where o.id = "+ orgId +" and o.status = 1 AND  o.`level` != 6) and o.status = 1";
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>(list.size());
		for(Map<String, Object> map:list){
			result.add(IntegerUtil.objToInteger(map.get("id")));
		}
		return result;
	}
	
	/**
	 * 根据机构id和用户Id查询管辖范围内的机构ID列表
	 * @author lxb
	 */
	@Override
	public List<Map<String, Object>> getDotRights(Integer courierId, Integer orgId) {
		if(orgId == null){
			throw new IllegalArgumentException("orgId=null");
		}
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询当前的机构
		Map<String, Object> orgMap = this.getManageCurrentOrgs(orgId);
		if(orgMap == null || orgMap.size() == 0 ){
			throw new IllegalArgumentException("orgId不存在");
		}
		
		//如果快递员属于网点经理，就拿自己所在的网点机构ID，否则就拿管辖的机构ID
		List<Map<String, Object>> orgList = new ArrayList<Map<String, Object>>();
		if("6".equals(String.valueOf(orgMap.get("level")))){
			orgList.add(orgMap);
		}else{
			//根据机构路径查询其所有的下一级机构
			String parentOrgPath = orgMap.get("p_path").toString()+ orgMap.get("id") + "_%";
			String orgsql = "select * from 0085_org o where o.p_path like ? and o.status = 1";
			orgList = findForJdbc(orgsql.toString(), parentOrgPath);

		}
		return orgList;
	}

    /**
     * 根据机构id和用户Id查询管辖范围内的机构ID列表
     * @author lxb
     */
    @Override
    public List<Integer> getManageOrgIds(Integer courierId, Integer orgId) {
        if(orgId == null){
            return this.getManageOrgIds(courierId);
        }
        if(courierId == null){
            throw new IllegalArgumentException("courierId=null");
        }
        //查询当前的机构
        Map<String, Object> orgMap = this.getManageCurrentOrgs(orgId);
        if(orgMap == null || orgMap.size() == 0 ){
            throw new IllegalArgumentException("orgId不存在");
        }
        
        //如果快递员属于网点经理，就拿自己所在的网点机构ID，否则就拿管辖的机构ID
        List<Integer> orgIds = new ArrayList<Integer>();
        if("6".equals(String.valueOf(orgMap.get("level")))){
            orgIds.add(Integer.parseInt(orgMap.get("id").toString()));
        }else{
			orgIds.add(orgId);
			/*
			 * 查出子区域ID
			 */
			StringBuilder pPath = new StringBuilder("^");
			pPath.append(orgMap.get("p_path").toString());
			pPath.append(orgId);
			pPath.append("_");
			List<Integer> childOrgIds = this.getChildOrgs(pPath.toString());
			/*
			 * 将查询结果放到结果集
			 */
			orgIds.addAll(childOrgIds);
        }
        return orgIds;
    }
	

	/**
	 * 根据机构id和快递员Id查询管辖范围内的快递员ID列表
	 * @author lxb
	 */
	@Override
	public List<Integer> getManageCouriersId(Integer courierId, Integer orgId) {
		List<Integer> orgIds = getManageOrgIds(courierId, orgId);
		String orgIdsStr = StringUtil.checkIdsString(StringUtils.join(orgIds, ","));
		return getCourierIdsByOrgId(orgIdsStr);
	}
	
	@Override
	public List<Integer> getCourierIdsByOrgId(String orgIdsStr){
		StringBuilder queryCourierIdsSql = new StringBuilder(" SELECT DISTINCT u.id ");
		queryCourierIdsSql.append(" FROM 0085_courier_org corg, `user` u ");
		queryCourierIdsSql.append(" WHERE corg.courier_id = u.id AND u.is_delete = 0 AND u.user_state = 1 AND corg.org_id IN (");
		queryCourierIdsSql.append(orgIdsStr);
		queryCourierIdsSql.append(") ");
		//查询机构下的快递员ID列表
		return this.findListbySql(queryCourierIdsSql.toString());
	}
	
	/**
	 * 查询当前机构
	 * @param courierId
	 * @return
	 */
	@Override
	public Map<String, Object> getManageCurrentOrgs(Integer orgId) {
		if(orgId == null){
			throw new IllegalArgumentException("orgId=null");
		}
		//根据机构路径查询其所有的下一级机构
		String sql = "select o.* from 0085_org o where o.id = ? and o.status = 1 ";
		return findOneForJdbc(sql.toString(), orgId);
	} 
	
	/**
	 * 返回快递员管辖的下一级所有机构
	 * @param courierId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getManageNextOrgs(Integer orgId) {
		if(orgId == null){
			throw new IllegalArgumentException("orgId=null");
		}
		//根据机构路径查询其所有的下一级机构
		String sql = "select o.* from 0085_org o where o.pid = ? and o.status = 1 ";
		return findForJdbc(sql.toString(), orgId);
	}

	@Override
	public List<Map<String, Object>> getOrgsInfo() {
		logger.info("Invoke method: getOrgsInfo, without param.");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT org.id, org.org_name orgName, org.latitude, org.longitude ");
		sql.append(" 	,(SELECT o.org_name FROM 0085_org o WHERE org.pid = o.id)pName ");
		sql.append(" 	,(SELECT IFNULL(COUNT(morg.merchant_id),0) FROM 0085_merchant_org morg WHERE morg.org_id = org.id)merchantCount ");
		sql.append("  	,(SELECT IFNULL(COUNT(corg.courier_id),0) FROM 0085_courier_org corg WHERE corg.org_id = org.id)courierCount ");
		sql.append(" 	,( ");
		sql.append(" 			SELECT IFNULL(COUNT(1),0) ac ");
		sql.append(" 			FROM( ");
		sql.append(" 				SELECT c.userId, c.type ");
		sql.append(" 				FROM( ");
		sql.append(" 					SELECT ca.user_id userId, ca.type, ca.create_time ");
		sql.append(" 					FROM 0085_courier_attendance ca ");
		sql.append(" 					WHERE ca.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" 					ORDER BY ca.create_time DESC ");
		sql.append(" 				)c  ");
		sql.append(" 				GROUP BY c.userId ");
		sql.append(" 				HAVING c.type = 0 ");
		sql.append(" 			)odc, 0085_courier_org co ");
		sql.append(" 			WHERE co.courier_id = odc.userId ");
		sql.append(" 				AND co.org_id = org.id ");
		sql.append(" 	)onDutyCourier ");
		sql.append(" FROM 0085_org org ");
		sql.append(" WHERE org.level = 6 AND org.`status` = 1 ");
		
		logger.debug("Inside method: getOrgsInfo, SQL: {}", sql.toString());
		
		return findForJdbc(sql.toString());
	}

	@Override
	public List<Integer> getManageCouriersIdTwo(Integer courierId, Integer orgId) {
		
		if(orgId == null){
			throw new IllegalArgumentException("orgId=null");
		}
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		String sql = "SELECT  DISTINCT o.courier_id id   from 0085_courier_org o  where o.org_id in (\n" +
					"		select o.id from 0085_org o where o.id = "+ orgId +" and o.status = 1 AND o.`level` = 6\n" +
					"		UNION ALL\n" +
					"		select o.id from 0085_org o where o.p_path like (select CONCAT(a.p_path,a.id,'_%') path from 0085_org a where a.id = "+ orgId +" and a.status = 1 AND a.`level` != 6) and o.status = 1\n" +
					")";
		
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>();
		for(Map<String, Object> map:list){
			result.add(IntegerUtil.objToInteger(map.get("id")));
		}
		return result;
	} 
	
	@Override
	public List<Integer> getManageL6OrgId(Integer courierId){
		/**
		 * 获取快递员所绑定的机构信息
		 */
		Map<String, Object> orgInfo = this.getParentOrg(courierId);
		
		if(orgInfo == null){
			throw new IllegalArgumentException("权限不足: 无法根据快递员ID获取机构信息");
		}
		
		/**
		 * level 字段在数据库中非空可以放心使用
		 */
		Integer level = Integer.valueOf(orgInfo.get("level").toString());
		/**
		 * 如果获取的机构本身就是level6的直接返回此机构ID
		 */
		if(level.equals(6)){
			List<Integer> orgIds = new ArrayList<Integer>();
			orgIds.add(Integer.valueOf(orgInfo.get("id").toString()));
			return orgIds;
		}
		
		/**
		 * 如果机构不是level6，就查询下属的level6机构ID
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT org.id ");
		sql.append(" FROM 0085_org org ");
		sql.append(" WHERE org.`status` = 1 AND org.`level` = 6 AND org.p_path REGEXP '^");
		sql.append(orgInfo.get("p_path"));
		sql.append(orgInfo.get("id"));
		sql.append("_' ");
		return this.findListbySql(sql.toString());
	}
	
}
