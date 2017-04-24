package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.ReorderStatisticService;

@Service
public class ReorderStatisticServiceImpl extends CommonServiceImpl implements ReorderStatisticService {
	private static final Logger logger = LoggerFactory.getLogger(ReorderStatisticServiceImpl.class);
	
	@Autowired
	CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> getReorderInfo(Integer userId, String period, Integer beginTime, Integer endTime, Integer page, Integer rows) {
		logger.info("调用方法 getReorderInfo(userId: {}, period: {}, beginTime: {}, endTime: {}, page: {}, rows: {}), 获取复购报表数据.", userId, period, beginTime, endTime, page, rows);
		/**
		 * 获取SQL中时间条件子句
		 */
		String periodInSQL = this.getPeriodInSQL(period, beginTime, endTime);
		/**
		 * 获取用户所管辖的区域ID
		 */
		List<Integer> orgIds = this.getManageL6OrgId(userId);
		/**
		 * 获取已排序的各网点复购率列表
		 */
		List<Map<String, Object>> orderedOrderCountList = this.getOrderedOrderCountList(userId, periodInSQL, orgIds, page, rows);
		
		int num = (page-1) * rows;		//记录序号
		
		/**
		 * 往列表中追加其他信息
		 */
		for(Map<String, Object> item: orderedOrderCountList){
			/**
			 * 追加记录序号
			 */
			item.put("rank", ++num);
			
			Integer orgId = Integer.valueOf(item.get("orgId").toString());
			
			/**
			 * 追加网店名称
			 */
			item.put("orgName", this.getOrgName(orgId));
			
			/**
			 * 追加各类用户数
			 */
			Map<String, Object> userCount = this.getUserCount(orgId, periodInSQL);
			if(userCount == null){
				logger.warn("无法根据网点ID: {},获取各种下单数的用户数量", orgId);
			}else{
				item.putAll(userCount);
			}
			
			/**
			 * 追加各类用户的复购率和总复购率
			 */
			BigDecimal orderCount = new BigDecimal(item.get("orderCount").toString());
			item.putAll(this.getRate(orderCount, userCount));
		}
		return orderedOrderCountList;
	}
	
	/**(OvO)
	 * 根据入参获取SQL中的时间条件子句(不含"AND")
	 * @param period	
	 * @return
	 */
	private String getPeriodInSQL(String period, Integer beginTime, Integer endTime){
		if("week".equals(period)){
			return " o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(period)){
			return " o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}else if("other".equals(period)){
			return " o.create_time >= " + beginTime + " AND o.create_time < " + endTime + " ";
		}
		return " o.create_time >= UNIX_TIMESTAMP(CURDATE()) ";
	}
	
	/**(OvO)
	 * 获取已排序的各网点订单数列表
	 * @param userId	当前登录用户ID
	 * @param period	统计时段
	 * @param orgIds	统计区域
	 * @param page		页码
	 * @param rows		每页显示记录数
	 * @return	orgId		网点ID
	 * 			orderCount	订单数
	 */
	private List<Map<String, Object>> getOrderedOrderCountList(Integer userId, String period, List<Integer> orgIds, Integer page, Integer rows){
		logger.debug("调用方法 getOrderedReorderRateList(userId: {}, period: {}, orgIds: {}, page: {}, rows: {}), 获取已排序的各网点复购率列表.", userId, period, orgIds, page, rows);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT morg.org_id orgId, COUNT(o.id) orderCount ");
		sql.append(" FROM `order` o, 0085_merchant_org morg ");
		sql.append(" WHERE ");
		sql.append(period);
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" 	) ");
		sql.append(" 	AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	AND morg.merchant_id = o.merchant_id ");
		sql.append(" GROUP BY morg.org_id ");
		sql.append(" ORDER BY orderCount DESC, orgId ");
		
		return this.findForJdbc(sql.toString(), page, rows);
	}
	
	/**(OvO)
	 * 根据快递员ID获取管辖范围内所有level6的机构ID
	 * @param courierId	快递员ID
	 * @return	level6 的机构ID列表
	 */
	private List<Integer> getManageL6OrgId(Integer courierId){
		/**
		 * 获取快递员所绑定的机构信息
		 */
		Map<String, Object> orgInfo = this.courierOrgService.getParentOrg(courierId);
		
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
	
	/**(OvO)
	 * 根据网点ID获取网店名称
	 * @param orgId	网点ID
	 * @return	orgName
	 */
	private String getOrgName (Integer orgId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT org.org_name orgName ");
		sql.append(" FROM 0085_org org ");
		sql.append(" WHERE org.id = ? ");
		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString(), orgId);
		if(resultMap == null){
			logger.warn("无法获取网点ID为: {} 的网点名称", orgId);
			return "";
		}
		return resultMap.get("orgName").toString();
	}
	
	/**(OvO)
	 * 获取指定区域各下单数量的用户数
	 * @param orgId		区域ID
	 * @param period	统计时间
	 * @return	two		统计时间内下了1到2单的用户
	 * 			four	统计时间内下了3到4单的用户
	 * 			six		统计时间内下了5到6单的用户
	 * 			eight	统计时间内下了7到8单的用户
	 * 			others	统计时间内下了8单以上的用户
	 */
	private Map<String, Object> getUserCount(Integer orgId, String period){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(CASE WHEN counts.c <= 2 THEN 1 ELSE NULL END),0)two ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN (counts.c > 2 AND counts.c <= 4) THEN 1 ELSE NULL END),0)four ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN (counts.c > 4 AND counts.c <= 6) THEN 1 ELSE NULL END),0)six ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN (counts.c > 6 AND counts.c <= 8) THEN 1 ELSE NULL END),0)eight ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN counts.c > 8 THEN 1 ELSE NULL END),0)others ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT COUNT(o.id) c ");
		sql.append(" 	FROM 0085_merchant_org morg, `order` o , tum_user_statistics tus ");
		sql.append(" 	WHERE ");
		sql.append(period);
		sql.append(" 		AND tus.total_order > 1 ");
		sql.append(" 		AND morg.org_id = ? ");
		sql.append(" 		AND morg.merchant_id = o.merchant_id AND tus.user_id = o.user_id ");
		sql.append(" 		AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	GROUP BY o.user_id ");
		sql.append(" )counts ");
		return this.findOneForJdbc(sql.toString(), orgId);
	}
	
	/**(OvO)
	 * 根据获取的各类用户数Map算出各类用户的复购率和总复购率
	 * @param orderCount	订单数
	 * @param userCount		用户数Map
	 * @return
	 */
	private Map<String, Object> getRate(BigDecimal orderCount, Map<String, Object> userCount){
		Map<String, Object> result = new HashMap<String, Object>();
		
		/**
		 * 老用户总数, 用于计算总复购率
		 */
		BigDecimal oldUser = BigDecimal.ZERO;
		
		/**
		 * 遍历各类用户数Map
		 * 计算各类用户的复购率
		 * 以及计算老用户总数
		 */
		for(String key: userCount.keySet()){
			String valueStr = userCount.get(key).toString();
			BigDecimal i = new BigDecimal(valueStr);
			oldUser = oldUser.add(i);
			if(orderCount.compareTo(BigDecimal.ZERO) == 0){
				result.put(key + "Rate", 0);
			}else{
				result.put(key + "Rate", i.divide(orderCount, 4, BigDecimal.ROUND_HALF_UP).movePointRight(2));
			}
		}
		
		/**
		 * 计算总复购率
		 */
		if(orderCount.compareTo(BigDecimal.ZERO) == 0){
			result.put("reorderRate", 0);
		}else{
			result.put("reorderRate", oldUser.divide(orderCount, 4, BigDecimal.ROUND_HALF_UP).movePointRight(2));
		}
		
		return result;
	} 
}
