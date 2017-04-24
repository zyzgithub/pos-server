package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.common.vo.SearchVo4UserRank;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.CourierUserService;


@Service
public class CourierUserServiceImpl extends CommonServiceImpl implements CourierUserService {
	private static final Logger logger = LoggerFactory.getLogger(CourierUserServiceImpl.class);

	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Autowired
	private CourierOrgServicI courierOrgService; 
	
	/**
	 * 获取指定用户在指定时间三天内的消费详情
	 */
	@Override
	public List<Map<String, Object>> userExpensesDetails(Integer endTime, Integer userId, boolean isAgent, Integer agentUserId) {
		logger.info("invoke method userExpensesDetails, params:{},{},{},{}", endTime, userId, isAgent, agentUserId);
		/**
		 * 检查参数
		 */
		if(isAgent && agentUserId == null){
			throw new IllegalArgumentException("合作商用户ID不能为空");
		}
		
		BigDecimal totalSpent = BigDecimal.valueOf(this.getTotalSpent3DaysAgo(userId, endTime, isAgent, agentUserId));
		List<Map<String, Object>> spentDetail = this.getSpentDetails(userId, endTime, isAgent, agentUserId);
		for(int i=spentDetail.size();i>0;i--){
			Map<String, Object> item = spentDetail.get(i-1);
			BigDecimal sum = BigDecimal.valueOf((Double)item.get("sum"));
			totalSpent = totalSpent.add(sum);
			item.put("totalSpent", totalSpent);
		}
		return spentDetail;
	}

	/**
	 * 获取划分用户类型规则
	 */
	@Override
	public List<Map<String, Object>> getUserTypeRule() {
		logger.info("invoke method getUserTypeRule, without param.");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rule.id, rule.type_name typeName, rule.type_desc typeDesc, rule.amount ");
		sql.append("FROM 0085_custtype_rule rule ");
		sql.append("WHERE invalid=0 ORDER BY amount DESC");
		return this.findForJdbc(sql.toString());
	}

	/**
	 * 获取用户总消费列表(用于统计各类型用户数量)
	 */
	@Override
	public List<Map<String, Object>> getUserTotalSpent(SearchVo vo, Integer orgId){
		logger.info("invoke method getUserTotalSpent, params:{},{},{}", vo, orgId);
		/**
		 * 根据查询时间调整查询语句
		 */
		String period = this.getPeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tus.total_money totalSpent ");
		sql.append(" FROM `order` o, 0085_merchant_org morg, tum_user_statistics tus ");
		sql.append(" WHERE o.merchant_id = morg.merchant_id AND tus.user_id = o.user_id ");
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(StringUtils.join(this.getManageL6OrgId(orgId), ","));
		sql.append(" )	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal')  ");
		sql.append(period);
		sql.append("  	AND tus.total_order > 1 ");
		sql.append(" GROUP BY o.user_id ");
		
		logger.info("SQL: " + sql.toString());
		return this.findForJdbc(sql.toString());
	}
	
	@Override
	public List<Map<String, Object>> getUserTotalSpent4Agent(SearchVo vo, Integer userId){
		logger.info("invoke method getUserTotalSpent4Agent, params:{},{},{}", vo, userId);
		/**
		 * 根据查询时间调整查询语句
		 */
		String period = this.getPeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tus.total_money totalSpent ");
		sql.append(" FROM `order` o, 0085_merchant_info mi, tum_user_statistics tus ");
		sql.append(" WHERE mi.creator = ? AND o.merchant_id = mi.merchant_id AND tus.user_id = o.user_id AND mi.platform_type = 2 ");
		sql.append("  	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(period);
		sql.append("  	AND tus.total_order > 1 ");
		sql.append(" GROUP BY o.user_id ");
		
		logger.info("SQL: " + sql.toString());
		return this.findForJdbc(sql.toString(), userId);
	}
	
	/**
	 * 获取用户排名
	 */
	@Override
	public Map<String, Object> getUserRank(Integer userId, boolean isAgent, SearchVo4UserRank vo, int page, int rowsPerPage) {
		logger.info("invoke method getUserRank, params:{},{},{}", vo, page, rowsPerPage);
		/**
		 * 如果要获取新用户排名, 则跳到指定方法
		 */
		if(vo.getIsNewUser()){
			return this.getNewUserRank(vo, userId, isAgent, page, rowsPerPage);
		}
		
		List<Map<String, Object>> rule = this.getUserTypeRule();	//用户划分规则表
		Map<String, Object> currentRule = null;						//按用户类别排名时使用, 在对排名结果添加用户类别信息时免除多次对规则表循环
		/**
		 * 如果本次排名是按类别排名
		 * 那么就根据类别确定搜索金额范围
		 */
		if(vo.getUserType() != null){
			Integer min = 0;
			Integer max = null;
			
			for(int i=rule.size()-1; i>=0; i--){
				Map<String, Object> listItem = rule.get(i);
				if(listItem.get("typeName").equals(vo.getUserType()) && i>0){
					currentRule = listItem;							//记录当前用户类别信息
					min = (Integer)listItem.get("amount");
					max = (Integer)rule.get(i-1).get("amount");
					break;
				}
				if(listItem.get("typeName").equals(vo.getUserType()) && i==0){
					currentRule = listItem;							//记录当前用户类别信息
					min = (Integer)listItem.get("amount");
					break;
				}
			}
			
			/**
			 * min, max 是来自tum_user_statistics表的数据, 在生产库中单位为分
			 * 而查询时使用的tum_user_statistics表，单位为分, 所以此处无需对min, max 进行处理
			 */
			vo.setUserTotalSpentFrom(min);
			
			/*
			 * 注意: max 是有可以为null的
			 */
			vo.setUserTotalSpentTo(max);
		}
		
		/**
		 * 查询用户最近三天消费情况的搜索条件
		 * 按天、周、月搜索，三天消费的搜索条件为今天的最后一秒(明天的0秒)
		 */
		Integer endTimeForDetails = null;
		if("other".equals(vo.getTimeType())){
			/**
			 * 自定义搜索时间，三天消费搜索条件也要相应作变动
			 */
			endTimeForDetails = getEndTimeOfTheDayInSecond(vo.getEndTime());
		}else{
			endTimeForDetails = getEndTimeOfTheDayInSecond((int)(new Date().getTime()/1000));
		}
		
		/**
		 * 获取页面所显示的用户列表
		 * 要判断登录用户是否合作商以控制流程
		 */
		List<Map<String, Object>> result = null;
		if(isAgent){
			result = this.getOrderedOldUserList4Agent(vo, userId, page, rowsPerPage);
		}else{
			result = this.getOrderedOldUserList(vo, page, rowsPerPage);
		}
		
		int num = (page-1)*rowsPerPage;		//记录序号(rank字段)
		
		/**
		 * 向查询结果添加用户信息以及最近的消费情况
		 */
		for(Map<String, Object> m: result){
			num++;
			m.put("rank", num);
			Integer userIdInMap = Integer.valueOf(m.get("id").toString());
			
			m.put("preSpent", this.getUserPreSpent(vo, userIdInMap));
			/**
			 * details 应该不会为空
			 */
			Map<String, Object> details = this.getOldUserDetails(userIdInMap);
			
			/*
			 * 如果是按用户级别排名, 则利用currentRule将级别信息添加到排行信息中
			 */
			if(currentRule != null){
				m.put("userType", currentRule.get("typeName"));
				m.put("userTypeId", currentRule.get("id"));
				m.put("userTypeDesc", currentRule.get("typeDesc"));
			}
			else{
				for(Map<String, Object> r: rule){
					//totalSpent是来自tum_user_statistics表的数据,单位是分;amount是来自0085_custtype_rule表的数据,在生产库中单位是分
					//所以无须进行单位匹配
					if(Integer.valueOf(details.get("totalSpent").toString()) >= Integer.valueOf(r.get("amount").toString())){
						m.put("userType", r.get("typeName"));
						m.put("userTypeId", r.get("id"));
						m.put("userTypeDesc", r.get("typeDesc"));
						break;
					}
				}
			}
			Integer totalSpent = Integer.valueOf(details.get("totalSpent").toString());
			m.put("totalSpent", BigDecimal.valueOf(totalSpent).movePointLeft(2));
			m.put("userName", details.get("userName"));
			m.put("spentDetails", this.userExpensesDetails(endTimeForDetails, Integer.valueOf(m.get("id").toString()), isAgent, userId));
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rankList", result);
		return resultMap;
	}
	
	/**(OvO)
	 * 获取新用户排名
	 * @param vo			搜索条件VO
	 * @param page			页码
	 * @param rowsPerPage	每页显示记录数
	 * @return
	 */
	private Map<String, Object> getNewUserRank(SearchVo4UserRank vo, Integer userId, boolean isAgent, Integer page, Integer rowsPerPage){
		List<Map<String, Object>> rule = this.getUserTypeRule();	//用户划分规则表
		/**
		 * 获取最原始的排序数据(未分页)
		 * 要判断登录用户是否合作商以控制流程
		 */
		Map<String, Object> result = null;
		if(isAgent){
			result = this.getOrderedNewUserList4Agent(vo, userId);
		}else{
			result = this.getOrderedNewUserList(vo);
		}
		List<Map<String, Object>> rankList = (List<Map<String, Object>>)result.get("rankList");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		Integer size = rankList.size();
		/**
		 * 遍历排名List, 并向元素添加信息
		 */
		int firstIndex = (page-1) * rowsPerPage;
		int lastIndex = page * rowsPerPage;
		/**
		 * 防止数组越界
		 */
		if(lastIndex > size - 1){
			lastIndex = size;
		}
		if(firstIndex > size -1){
			firstIndex = 1;
			lastIndex = 0;
		}
		
		for(int i=firstIndex; i<lastIndex; i++){
			Map<String, Object> item = rankList.get(i);
			item.put("rank", i+1);
			Integer userIdInItem = Integer.valueOf(item.get("id").toString());
			
			Map<String, Object> details = this.getNewUserDetails(userIdInItem);
			item.put("orgName", details.get("orgName"));
			item.put("userName", details.get("userName"));
			for(Map<String, Object> r: rule){
				//totalSpent是来自tum_user_statistics表的数据,单位是分;amount是来自0085_custtype_rule表的数据,在生产库中单位是分
				//所以无须进行单位匹配
				if(Integer.valueOf(details.get("totalSpent").toString()) >= Integer.valueOf(r.get("amount").toString())){
					item.put("userType", r.get("typeName"));
					item.put("userTypeId", r.get("id"));
					item.put("userTypeDesc", r.get("typeDesc"));
					break;
				}
			}
			resultList.add(item);
		}
		result.put("rankList", resultList);
		return result;
	}

	/**(OvO)
	 * 根据机构ID获取旗下所有level6的机构ID
	 * @param orgId	快递员ID
	 * @return	level6 的机构ID列表
	 */
	private List<Integer> getManageL6OrgId(Integer orgId){
		/**
		 * 获取快递员所绑定的机构信息
		 */
		Map<String, Object> orgInfo = this.courierOrgService.getManageCurrentOrgs(orgId);
		
		if(orgInfo == null){
			throw new IllegalArgumentException("机构: " + orgId + " 非有效机构");
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
	 * 获取三天前的历史消费金额
	 * @param userId			用户ID
	 * @param endTimeInSecond	用于确定3天前的时间
	 * @param isAgent			当前登录用户是否合作商
	 * @param agentUserId		合作商用户ID
	 * @return	返回三天前的历史消费金额
	 */
	private Double getTotalSpent3DaysAgo(Integer userId, Integer endTimeInSecond, boolean isAgent, Integer agentUserId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ifnull(sum(origin),0) totalSpent");
		sql.append(" from `order` o ");
		if(isAgent){
			sql.append(" , 0085_merchant_info minfo ");
		}
		sql.append(" where o.state='confirm' and o.pay_state='pay' and (o.rstate='norefund' OR o.rstate='normal') ");
		if(isAgent){
			sql.append(" AND o.merchant_id = minfo.merchant_id AND minfo.platform_type = 2 AND minfo.creator = ");
			sql.append(agentUserId);
		}
		sql.append(" 	AND o.user_id = ");
		sql.append(userId);
		sql.append(" 	AND o.create_time < ");
		sql.append(endTimeInSecond-3*86400);
		return (Double)(findOneForJdbc(sql.toString()).get("totalSpent"));
	}
	
	private List<Map<String, Object>> getSpentDetails(Integer userId, Integer endTimeInSecond, boolean isAgent, Integer agentUserId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT o.user_id userId, DATE(FROM_UNIXTIME(o.create_time)) date, SUM(o.origin) sum ");
		sql.append(" from `order` o  ");
		if(isAgent){
			sql.append(" , 0085_merchant_info minfo ");
		}
		sql.append(" where o.user_id = ? AND o.create_time >= ? AND o.create_time < ? AND o.state='confirm' AND o.pay_state='pay' and (o.rstate='norefund' OR o.rstate='normal') ");
		if(isAgent){
			sql.append(" AND o.merchant_id = minfo.merchant_id AND minfo.platform_type = 2 AND minfo.creator = ");
			sql.append(agentUserId);
		}
		sql.append(" GROUP BY o.user_id, DATE(FROM_UNIXTIME(o.create_time)) DESC ");
		return findForJdbc(sql.toString(), userId, endTimeInSecond - 3 * 86400, endTimeInSecond);
	}
	
	/**(OvO)
	 * 获取用户在当前时段消费金额并进行排序(可按用户等级筛选)
	 * @param vo	搜索条件VO
	 * @param page	页码
	 * @param rows	每页显示记录数
	 * @return	id		用户ID
	 * 			spent	当前时段用户的消费金额
	 */
	private List<Map<String, Object>> getOrderedOldUserList(SearchVo4UserRank vo, Integer page, Integer rows){
		Integer totalSpentFrom = vo.getUserTotalSpentFrom();
		Integer totalSpentTo = vo.getUserTotalSpentTo();
		Integer orgId = vo.getOrgId();
		String period = this.getPeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tus.user_id id, SUM(o.origin) spent ");
		sql.append(" FROM `order` o, 0085_merchant_org morg, tum_user_statistics tus ");
		sql.append(" WHERE o.merchant_id = morg.merchant_id AND tus.user_id = o.user_id ");
		if(totalSpentFrom != null){
			sql.append(" AND tus.total_money >= ");
			sql.append(totalSpentFrom);
		}
		if(totalSpentTo != null){
			sql.append(" AND tus.total_money < ");
			sql.append(totalSpentTo);
		}
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(StringUtils.join(this.getManageL6OrgId(orgId), ","));
		sql.append(" )	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(period);
		sql.append(" AND tus.total_order > 1 ");
		sql.append(" GROUP BY o.user_id ");
		sql.append(" ORDER BY spent DESC, id ");
		
		return this.findForJdbc(sql.toString(), page, rows);
	}
	
	/**(OvO)
	 * 获取用户在当前时段消费金额并进行排序(可按用户等级筛选)(合作商)
	 * @param vo	搜索条件VO
	 * @param page	页码
	 * @param rows	每页显示记录数
	 * @return	id		用户ID
	 * 			spent	当前时段用户的消费金额
	 */
	private List<Map<String, Object>> getOrderedOldUserList4Agent(SearchVo4UserRank vo, Integer userId, Integer page, Integer rows){
		Integer totalSpentFrom = vo.getUserTotalSpentFrom();
		Integer totalSpentTo = vo.getUserTotalSpentTo();
		String period = this.getPeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tus.user_id id, SUM(o.origin) spent ");
		sql.append(" FROM `order` o, 0085_merchant_info minfo , tum_user_statistics tus ");
		sql.append(" WHERE minfo.creator = ? AND minfo.platform_type = 2 AND o.merchant_id = minfo.merchant_id AND tus.user_id = o.user_id ");
		if(totalSpentFrom != null){
			sql.append(" AND tus.total_money >= ");
			sql.append(totalSpentFrom);
		}
		if(totalSpentTo != null){
			sql.append(" AND tus.total_money < ");
			sql.append(totalSpentTo);
		}
		sql.append(" 	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(period);
		sql.append(" AND tus.total_order > 1 ");
		sql.append(" GROUP BY o.user_id ");
		sql.append(" ORDER BY spent DESC, id ");
		
		return this.findForJdbcParam(sql.toString(), page, rows, userId);
	}
	
	/**(OvO)
	 * 获取新用户在当前时段消费金额并进行排序(未分页)
	 * @param vo	搜索条件VO
	 * @param page	页码
	 * @param rows	每页显示记录数
	 * @return		id				用户ID
	 * 				spent			当前时段用户的消费金额
	 * 			newUserCount	新用户数
	 */
	private Map<String, Object> getOrderedNewUserList(SearchVo4UserRank vo){
		String period = this.getNewUserPeriodBySearchVo(vo);
		Integer orgId = vo.getOrgId();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT o.user_id id, SUM(o.origin) spent ");
		sql.append(" FROM `user` u, `order` o, 0085_merchant_org morg ");
		sql.append(" WHERE u.id = o.user_id AND morg.merchant_id = o.merchant_id ");
		sql.append(period);
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(StringUtils.join(this.getManageL6OrgId(orgId), ","));
		sql.append(" )	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(" GROUP BY o.user_id ");
		sql.append(" ORDER BY spent DESC, id ");
		
		List<Map<String, Object>> resultList = this.findForJdbc(sql.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rankList", resultList);
		result.put("newUserCount", resultList.size());
		return result;
	}
	
	/**(OvO)
	 * 获取新用户在当前时段消费金额并进行排序(未分页)(合作商)
	 * @param vo	搜索条件VO
	 * @param page	页码
	 * @param rows	每页显示记录数
	 * @return		id				用户ID
	 * 				spent			当前时段用户的消费金额
	 * 			newUserCount	新用户数
	 */
	private Map<String, Object> getOrderedNewUserList4Agent(SearchVo4UserRank vo, Integer userId){
		String period = this.getNewUserPeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT o.user_id id, SUM(o.origin) spent ");
		sql.append(" FROM `user` u, `order` o, 0085_merchant_info minfo ");
		sql.append(" WHERE minfo.creator = ? AND minfo.platform_type = 2 ");
		sql.append(period);
		sql.append(" 	AND u.id = o.user_id AND minfo.merchant_id = o.merchant_id ");
		sql.append(" 	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(" GROUP BY o.user_id ");
		sql.append(" ORDER BY spent DESC, id ");
		
		List<Map<String, Object>> resultList = this.findForJdbc(sql.toString(), userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rankList", resultList);
		result.put("newUserCount", resultList.size());
		return result;
	}
	
	/**(OvO)
	 * 获取老用户详细信息
	 * @param userId	用户ID
	 * @return	totalSpent	总消费金额
	 * 			userName	用户名
	 */
	private Map<String, Object> getOldUserDetails(Integer userId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tus.total_money totalSpent, u.username userName ");
		sql.append(" FROM tum_user_statistics tus, `user` u ");
		sql.append(" WHERE tus.user_id = u.id AND u.id = ? ");
		
		return this.findOneForJdbc(sql.toString(), userId);
	}
	
	/**(OvO)
	 * 获取老用户详细信息
	 * @param userId	用户ID
	 * @return	orgName	总消费金额
	 * 			userName	用户名
	 */
	private Map<String, Object> getNewUserDetails(Integer userId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.username userName, org.org_name orgName, tus.total_money totalSpent ");
		sql.append(" FROM `user` u, `order` o, 0085_merchant_org morg, 0085_org org, tum_user_statistics tus ");
		sql.append(" WHERE u.id = ? AND o.user_id = u.id AND u.id = tus.user_id AND o.pay_time > 0 ");
		sql.append(" 	AND morg.merchant_id = o.merchant_id AND org.id = morg.org_id ");
		sql.append(" ORDER BY o.pay_time LIMIT 1 ");
		
		return this.findOneForJdbc(sql.toString(), userId);
	}
	
	/**(OvO)
	 * 获取用户上周期的消费金额
	 * @param vo		搜索条件VO
	 * @param userId	用户ID	
	 * @return
	 */
	private BigDecimal getUserPreSpent(SearchVo4UserRank vo, Integer userId){
		String period = this.getPrePeriodBySearchVo(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(SUM(o.origin),0) preSpent ");
		sql.append(" FROM `order` o  ");
		sql.append(" WHERE o.user_id = ? ");
		sql.append(period);
		sql.append(" 	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		
		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString(), userId);
		return BigDecimal.valueOf(Double.parseDouble(resultMap.get("preSpent").toString()));
	}
	
	/**
	 * 整理vo中的秒数(vo所存的时间以秒为单位)
	 * 使它变成当天的最后一秒(第二天的0秒)
	 * 这个秒数SQL查询，直接使用秒数查询比较快
	 */
	private Integer getEndTimeOfTheDayInSecond(Integer endTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(endTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	
	private Integer getStartTimeOfTheDayInSecond(Integer startTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime*1000l);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	
	/**
	 * 根据查询参数获取SQL时间条件
	 * @param vo	查询条件
	 * @return	SQL时间条件子句
	 */
	private String getPeriodBySearchVo(SearchVo vo){
		String period = null;
		
		if("day".equals(vo.getTimeType()) || vo.getTimeType() == null){
			period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE()) ";
		}else if("week".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}else if("other".equals(vo.getTimeType())){
			if(vo.getBeginTime()!=null && vo.getEndTime()!=null && vo.getBeginTime() !=0 && vo.getEndTime()>=vo.getBeginTime()){
				period = " AND o.create_time >= " + this.getStartTimeOfTheDayInSecond(vo.getBeginTime()) + " AND o.create_time < " + this.getEndTimeOfTheDayInSecond(vo.getEndTime());
			}
		}
		return period;
	}
	
	/**
	 * 根据查询参数获取新用户SQL时间条件
	 * @param vo	查询条件
	 * @return	SQL时间条件子句
	 */
	private String getNewUserPeriodBySearchVo(SearchVo vo){
		String period = null;
		
		if("day".equals(vo.getTimeType()) || vo.getTimeType() == null){
			period = " AND o.create_time >= UNIX_TIMESTAMP(CURDATE()) AND u.first_order_time >= UNIX_TIMESTAMP(CURDATE())";
		}else if("week".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) AND u.first_order_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) AND u.first_order_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}else if("other".equals(vo.getTimeType())){
			if(vo.getBeginTime()!=null && vo.getEndTime()!=null && vo.getBeginTime() !=0 && vo.getEndTime()>=vo.getBeginTime()){
				period = " AND o.create_time >= " + this.getStartTimeOfTheDayInSecond(vo.getBeginTime()) + " AND o.create_time < " + this.getEndTimeOfTheDayInSecond(vo.getEndTime());
			}
		}
		return period;
	}
	
	/**(OvO)
	 * 根据查询参数获取SQL上周期的时间条件
	 * @param vo	查询条件
	 * @return	SQL时间条件子句
	 */
	private String getPrePeriodBySearchVo(SearchVo vo){
		String period = null;
		
		if("day".equals(vo.getTimeType()) || vo.getTimeType() == null){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL 1 DAY)) AND o.create_time < UNIX_TIMESTAMP(CURDATE()) ";
		}else if("week".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate())+7 DAY)) AND o.create_time < UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(vo.getTimeType())){
			period = " AND o.create_time >= UNIX_TIMESTAMP(DATE_SUB(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY),INTERVAL 1 MONTH)) AND o.create_time < UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}else if("other".equals(vo.getTimeType())){
			Integer endTime = this.getStartTimeOfTheDayInSecond(vo.getBeginTime());
			Integer startTime = endTime - this.getEndTimeOfTheDayInSecond(vo.getEndTime()) + endTime; 
			if(vo.getBeginTime()!=null && vo.getEndTime()!=null && vo.getBeginTime() !=0 && vo.getEndTime()>=vo.getBeginTime()){
				period = " AND o.create_time >= " + startTime + " AND o.create_time < " + endTime;
			}
		}
		return period;
	}
	
	/**
	 * 查询用户所属职位
	 */
	@Override
	public Map<String, Object> getUserByPosition(Long courierId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cp.position_id position FROM 0085_courier_position cp WHERE cp.courier_id = ?");
		return findOneForJdbc(sql.toString(), courierId);
	}
}
