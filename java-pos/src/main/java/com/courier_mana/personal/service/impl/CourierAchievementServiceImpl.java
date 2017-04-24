package com.courier_mana.personal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courier_mana.personal.service.CourierAchievementService;

@Service
public class CourierAchievementServiceImpl extends CommonServiceImpl implements CourierAchievementService{
	private static final Logger logger = LoggerFactory.getLogger(CourierAchievementServiceImpl.class);
	
	
	/**
	 * 获取一个节点下快递员：排名
	 */
	@Override
	public List<Map<String, Object>> queryNodeRanking(Long courierId, String nodeType) {
		
		StringBuilder sql = new StringBuilder();
		
		List<Map<String, Object>> list = this.findForJdbc(sql.toString(), null);
		
		return list;
	}
	
	/**
	 * 获取快递员信息
	 */
	@Override
	public Map<String, Object> getCourierInfo(Integer courierId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.id, u.username, u.photoUrl, p.`name` positionName, o.org_name orgName FROM `user` u ");
		sql.append("LEFT JOIN 0085_courier_position cp ON cp.courier_id=u.id ");
		sql.append("LEFT JOIN 0085_position p ON p.id=cp.position_id ");
		sql.append("LEFT JOIN 0085_courier_org co on co.courier_id=u.id ");
		sql.append("LEFT JOIN 0085_org o on o.id=co.org_id ");
		sql.append("WHERE u.user_type='courier' AND u.id=?");
		
		return this.findOneForJdbc(sql.toString(), courierId);
	}

	/**
	 * 快递员成绩排名（按时间、区域、接单数排名）
	 */
	public List<Map<String, Object>> getCouriersRank(Integer courierId,
			String startDate, String endDate, Boolean isRankByArea,
			Integer page, Integer rowsPerPage) {
		if(courierId == null){
			courierId = 0;
		}
		Integer currentRank = 0;
		String start = null;
		String num = null;
		if(page != null && rowsPerPage != null){			
			currentRank = (page-1)*rowsPerPage;
			start = currentRank.toString();
			num = rowsPerPage.toString();
		}
		String sql = "";
		sql += "SELECT d.*,e.org_name orgName from (";
		if(!isRankByArea){
			sql += "select * from (";
		}
		sql += "select b.courier_id,u.username,u.photoUrl,b.total,";
		sql += "case when @prevrank = b.total then @currank when @prevrank := b.total then @currank := @currank + 1 end as rank from (";
		sql += "select o.courier_id,count(0) as total from `order` o ";
		if(isRankByArea){
			sql += " left join 0085_courier_org co on o.courier_id = co.courier_id ";
		}
		sql += " where o.courier_id<>0 and o.state='confirm' ";
		if (StringUtils.isNotEmpty(startDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
			sql += " and o.complete_time >= UNIX_TIMESTAMP('" + startDate + "')";
		}
		if (StringUtils.isNotEmpty(endDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
			sql += " and o.complete_time < UNIX_TIMESTAMP('" + endDate + "')";
		}
		if (isRankByArea) {
			Map<String, Object> orgId = this.findOneForJdbc("SELECT org_id orgId FROM 0085_courier_org WHERE courier_id=?", courierId);
			//防止快递员ID没有对应orgID而引发一系列错误，在此先进行判断
			if(orgId == null || orgId.isEmpty()){
				sql += " and co.org_id in (0) ";
			}
			else{
				logger.info(orgId.get("orgId").toString());
				String orgIds = this.getChildOrgId((Integer)orgId.get("orgId"));
//				sql += " and co.org_id in (select org_id from 0085_courier_org where courier_id = " + courierId + ")";
				sql += " and co.org_id in (" + orgIds + ") ";
			}
		}
		sql += " group by o.courier_id order by total desc) b left join user u force index(i_u_id_un_ut) on u.id=b.courier_id, ";
		sql += " (select @currank :=0, @prevrank := null) r  ";
		if(!isRankByArea){
			sql += ") c ";
		}
		sql += ")d, 0085_org e, 0085_courier_org f WHERE d.courier_id = f.courier_id and e.id = f.org_id order by total desc, courier_id";
		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(num)) {
			sql += " limit " + start + " , " + num;
		}
		return findForJdbc(sql);
	}
	
	@Override
	public List<Map<String, Object>> getCouriersRank4Agent(Integer courierId,
			String startDate, String endDate,
			Integer page, Integer rowsPerPage){
		logger.info("调用方法: getCouriersRank4Agent 获得合作商(" + courierId + ")下所有快递员的排行");
		/**
		 * 由于在Controller中已经检查过入参, 此处不再检查
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT source.* ");
		sql.append(" 	,case when @prevrank = source.total then @currank when @prevrank := source.total then @currank := @currank + 1 when not @prevrank := source.total then @currank := @currank + 1 end as rank ");
		sql.append(" 	,'' orgName ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT couriers.*, IFNULL(COUNT(CASE WHEN o.id IS NOT NULL THEN 1 END),0) total ");
		sql.append(" 	FROM ( ");
		sql.append(" 		SELECT ci.courier_id, u.username, u.photoUrl ");
		sql.append(" 		FROM 0085_courier_info ci, `user` u ");
		sql.append(" 		WHERE ci.courier_id = u.id AND ci.bind_user_id = ? ");
		sql.append(" 	)couriers LEFT JOIN `order` o ON couriers.courier_id = o.courier_id AND o.state = 'confirm' AND o.create_time >= UNIX_TIMESTAMP(?) AND o.create_time < UNIX_TIMESTAMP(?)");
		sql.append(" 	GROUP BY couriers.courier_id ");
		sql.append(" 	ORDER BY total DESC, couriers.courier_id ");
		sql.append(" )source,(select @currank :=0, @prevrank := null) r ");
		
		logger.debug("SQL: "+sql.toString());
		return this.findForJdbcParam(sql.toString(), page, rowsPerPage, courierId, startDate, endDate);
	}

	@Override
	public Map<String, Object> getMyRank(Integer courierId, String startDate,
			String endDate, Boolean isRankByArea) {
		if(courierId == null){
			courierId = 0;
		}

		String sql = "";
		sql += "select c.total, c.rank, c.username, c.photoUrl  from (";
		sql += "select b.courier_id,u.username,b.total,u.photoUrl,";
		sql += "case when @prevrank = b.total then @currank when @prevrank := b.total then @currank := @currank + 1 end as rank from (";
		sql += "select o.courier_id,count(0) as total from `order` o ";
		if(isRankByArea){
			sql += " left join 0085_courier_org co on o.courier_id = co.courier_id ";
		}
		sql += " where o.courier_id<>0 and o.state='confirm' ";
		if (StringUtils.isNotEmpty(startDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
			sql += " and o.complete_time >= UNIX_TIMESTAMP('" + startDate + "')";
		}
		if (StringUtils.isNotEmpty(endDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
			sql += " and o.complete_time <= UNIX_TIMESTAMP('" + endDate + "')";
		}
		if (isRankByArea) {
			Map<String, Object> orgId = this.findOneForJdbc("SELECT org_id orgId FROM 0085_courier_org WHERE courier_id=?", courierId);
			//防止快递员ID没有对应orgID而引发一系列错误，在此先进行判断
			if(orgId == null || orgId.isEmpty()){
				sql += " and co.org_id in (0) ";
			}
			else{
				logger.info(orgId.get("orgId").toString());
				String orgIds = this.getChildOrgId((Integer)orgId.get("orgId"));
//				sql += " and co.org_id in (select org_id from 0085_courier_org where courier_id = " + courierId + ")";
				sql += " and co.org_id in (" + orgIds + ") ";
			}
		}
		sql += " group by o.courier_id order by total desc) b left join user u force index(i_u_id_un_ut) on u.id=b.courier_id, ";
		sql += "(select @currank :=0, @prevrank := null) r ";
		sql += ") c where c.courier_id=" + courierId;

		return findOneForJdbc(sql);
	}
	
	public String getChildOrgId(Integer orgId) {
		logger.info("invoke method getChildOrgId, params:{}", orgId);
		StringBuilder sql = new StringBuilder();
		sql.append("select id from 0085_org o ");
		sql.append("where o.p_path like ( ");
		sql.append("select CONCAT(o.p_path, CONCAT(o.id, '_%')) ");
		sql.append("from 0085_org o ");
		sql.append("where o.id = ? )");
		List<Map<String, Object>> list = this.findForJdbc(sql.toString(), orgId);
		
		StringBuilder result = new StringBuilder();
		result.append(orgId);
		result.append(",");
		for(Map<String, Object> m:list){
			result.append(m.get("id"));
			result.append(",");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
}
