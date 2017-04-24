package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.AgentServiceI;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.CourierCommentService;
import com.wm.service.user.CustTypeRuleServiceI;

@Service
public class CourierCommentServiceImpl extends CommonServiceImpl implements
		CourierCommentService {

	private static Logger logger = LoggerFactory
			.getLogger(CourierCommentServiceImpl.class);

	@Autowired
	private CourierOrgServicI courierOrgServiceImpl;
	@Autowired
	private CustTypeRuleServiceI custTypeRuleServiceI;
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	@Autowired
	private AgentServiceI agentService;
	
	@Override
	public Map<String, Object> getCommentStatistic(Integer userId, SearchVo vo){
		logger.info("调用方法 getCommentStatistic( userId: {}, vo: {}), 获得大项统计信息.", userId, vo);
		
		/**
		 * 获取管辖区域ID列表
		 */
		List<Integer> orgIds = null;
		Integer targetOrg = vo.getOrgId();
		if(targetOrg != null){
			orgIds = this.courierOrgServiceImpl.getManageOrgIds(userId, targetOrg);
		}else{
			orgIds = this.courierOrgServiceImpl.getManageOrgIds(userId);
		}
		
		/**
		 * 获取商家ID列表
		 */
		List<Integer> merchantIds = getMerchantIds(orgIds);
		
		return this.getCommentStatisticMap(merchantIds, vo);
	}
	
	@Override
	public List<Map<String, Object>> getCommentList(Integer userId, SearchVo vo, Integer commentGrade, Integer page, Integer rows){
		logger.info("调用方法 getCommentList( userId: {}, vo: {}, commentGrade: {}, page: {}, rows: {}), 获得大项统计信息.", userId, vo, commentGrade, page, rows);
		/**
		 * 获取SQL时间条件字句
		 */
		String period = this.getCommentPeriod(vo);
		
		/**
		 * 获取SQL评价星级筛选条件字句
		 */
		String commentGradeStr = " HAVING grade < 5 ";
		if(commentGrade != null){
			commentGradeStr = this.getGradeFactor(commentGrade);
		}
		
		/**
		 * 获取管辖区域ID列表
		 */
		List<Integer> orgIds = null;
		Integer targetOrg = vo.getOrgId();
		if(targetOrg != null){
			orgIds = this.courierOrgServiceImpl.getManageOrgIds(userId, targetOrg);
		}else{
			orgIds = this.courierOrgServiceImpl.getManageOrgIds(userId);
		}
		
		/**
		 * 获取商家ID列表
		 */
		List<Integer> merchantIds = getMerchantIds(orgIds);
		
		return this.getCommentListData(merchantIds, period, commentGradeStr, page, rows);
	}
	
	/**(OvO)
	 * 获得大项统计信息数据
	 * @param merchantIds
	 * @param period
	 * @return
	 */
	private Map<String, Object> getCommentStatisticMap(List<Integer> merchantIds, SearchVo vo){
		/**
		 * 如果merchantIds为空, 那么下面的SQL会出问题
		 * 虽然可以通过修改SQL修复报错的问题
		 * 但是既然没有商家ID, 那么也没有必要查数据库了
		 * 直接构造一个空数据
		 */
		if(merchantIds.isEmpty()){
			Map<String, Object> emptyResult = new HashMap<String, Object>();
			emptyResult.put("1s", 0);
			emptyResult.put("2s", 0);
			emptyResult.put("3s", 0);
			emptyResult.put("4s", 0);
			emptyResult.put("todayOrderComment", 0);
			emptyResult.put("totalComment", 0);
			emptyResult.put("commentRate", 0);
			emptyResult.put("negativeCommnet", 0);
			emptyResult.put("negativeRate", 0);
			return emptyResult;
		}
		/**
		 * 获取今天订单数
		 */
		BigDecimal orderCount = this.getOrderCount(merchantIds, this.getOrderPeriod(vo));
		
		/**
		 * 获取各种数量
		 */
		Map<String, Object> result = this.getCommentCount(merchantIds, this.getCommentPeriod(vo));
		
		/**
		 * 计算差评(1~4星)数
		 */
		Integer c1 = Integer.valueOf(result.get("1s").toString());
		Integer c2 = Integer.valueOf(result.get("2s").toString());
		Integer c3 = Integer.valueOf(result.get("3s").toString());
		Integer c4 = Integer.valueOf(result.get("4s").toString());
		Integer negative = c1 + c2 + c3 + c4;
		
		/**
		 * 计算评价系数(百分数)
		 */
		BigDecimal todayOrderComment = new BigDecimal(result.get("todayOrderComment").toString());
		BigDecimal commentRate = BigDecimal.ZERO;
		if(orderCount.compareTo(BigDecimal.ZERO) != 0){
			commentRate = todayOrderComment.divide(orderCount, BigDecimal.ROUND_HALF_UP, 4).movePointRight(2);
		}
		result.put("commentRate", commentRate);
		
		/**
		 * 计算差评系数(百分数)
		 */
		BigDecimal totalComment = new BigDecimal(result.get("totalComment").toString());
		BigDecimal negativeCommnet = BigDecimal.valueOf(negative);
		BigDecimal negativeRate = BigDecimal.ZERO;
		if(totalComment.compareTo(BigDecimal.ZERO) != 0){
			negativeRate = negativeCommnet.divide(totalComment, BigDecimal.ROUND_HALF_UP, 4).movePointRight(2);
		}
		result.put("negativeCommnet", negativeCommnet);
		result.put("negativeRate", negativeRate);
		
		return result;
	}
	
	/**(OvO)
	 * 获得评价列表数据
	 * @param merchantIds
	 * @param period
	 * @param commentType
	 * @return	orderId		订单ID
	 * 			grade		评价星级
	 * 			userId		用户ID
	 * 			cTime		评价时间
	 */
	private List<Map<String, Object>> getCommentListData(List<Integer> merchantIds, String period, String commentGrade, Integer page, Integer rows){
		/**
		 * 如果merchantIds为空, 那么下面的SQL会出问题
		 * 虽然可以通过修改SQL修复报错的问题
		 * 但是既然没有商家ID, 那么也没有必要查数据库了
		 * 直接构造一个空数据
		 */
		if(merchantIds.isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		
		/**
		 * 获得要显示评论的订单ID
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ocom.order_id orderId, SUM(ocom.grade)/COUNT(1) grade, ocom.user_id userId, FROM_UNIXTIME(ocom.comment_time, '%Y-%m-%d') cTime ");
		sql.append(" FROM `order` o, 0085_order_comment ocom ");
		sql.append(" WHERE ");
		sql.append(period);
		sql.append(" 	AND o.merchant_id IN ( ");
		sql.append(StringUtils.join(merchantIds, ","));
		sql.append(" 	) ");
		sql.append(" 	AND ocom.invalid = 0 AND ocom.comment_display <> 'N' AND o.id = ocom.order_id ");
		sql.append(" GROUP BY ocom.order_id ");
		sql.append(commentGrade);
		sql.append(" ORDER BY orderId DESC ");
		List<Map<String, Object>> orderIds = this.findForJdbc(sql.toString(), page, rows);
		
		for(Map<String, Object> item: orderIds){
			Integer orderId = Integer.valueOf(item.get("orderId").toString());
			item.putAll(this.getCommentsByOrderId(orderId));
			Integer userId = Integer.valueOf(item.get("userId").toString());
			item.put("username", this.getCourierName(userId));
		}
		return orderIds;
	}
	
	/**(OvO)
	 * 根据订单ID获得该订单的所有非5星评论
	 * @param orderId	订单ID
	 * @return
	 */
	private Map<String, Object> getCommentsByOrderId(Integer orderId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ocom.comment_content text, ocom.grade, ocom.tags, ocom.comment_target_id targetId, ocom.comment_target targetType ");
		sql.append(" FROM 0085_order_comment ocom ");
		sql.append(" WHERE ocom.order_id = ? AND ocom.grade < 5 AND ocom.invalid = 0 AND ocom.comment_display <> 'N'; ");
		List<Map<String, Object>> comments = this.findForJdbc(sql.toString(), orderId);
		/**
		 * 再生产库中发现有同一张订单对商家多次评价的情况
		 * 所以用List包装各类型的评论
		 */
		List<Map<String, Object>> courierComments = null;
		List<Map<String, Object>> merchantComments = null;
		List<Map<String, Object>> flavorComments = null;
		for(Map<String, Object> item: comments){
			Integer targetType = Integer.valueOf(item.get("targetType").toString());
			if(targetType == 0){
				if(courierComments == null){
					courierComments = new ArrayList<Map<String,Object>>();
				}
				Integer courierId = Integer.valueOf(item.get("targetId").toString());
				String targetName = this.getCourierName(courierId);
				item.put("targetName", targetName);
				courierComments.add(item);
			}else if(targetType == 1){
				if(merchantComments == null){
					merchantComments = new ArrayList<Map<String,Object>>();
				}
				Integer merchantId = Integer.valueOf(item.get("targetId").toString());
				String targetName = this.getMerchantName(merchantId);
				item.put("targetName", targetName);
				merchantComments.add(item);
			}else if(targetType == 2){
				if(flavorComments == null){
					flavorComments = new ArrayList<Map<String,Object>>();
				}
				Integer merchantId = Integer.valueOf(item.get("targetId").toString());
				String targetName = this.getMerchantName(merchantId);
				item.put("targetName", targetName);
				flavorComments.add(item);
			}
		}
		Map<String, Object> commentMap = new HashMap<String, Object>();
		commentMap.put("courierComments", courierComments);
		commentMap.put("merchantComments", merchantComments);
		commentMap.put("flavorComments", flavorComments);
		return commentMap;
	}
	
	/**(OvO)
	 * 根据快递员ID获取快递员名称
	 * @param courierId	快递员ID
	 * @return
	 */
	private String getCourierName(Integer courierId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.username ");
		sql.append(" FROM `user` u ");
		sql.append(" WHERE u.id = ? ");
		Map<String, Object> result = this.findOneForJdbc(sql.toString(), courierId);
		if(result != null){
			return result.get("username").toString();
		}
		return null;
	}
	
	/**(OvO)
	 * 根据商家ID获取商家名称
	 * @param merchantId	商家ID
	 * @return
	 */
	private String getMerchantName(Integer merchantId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT m.title ");
		sql.append(" FROM merchant m ");
		sql.append(" WHERE m.id = ? ");
		Map<String, Object> result = this.findOneForJdbc(sql.toString(), merchantId);
		if(result != null){
			return result.get("title").toString();
		}
		return null;
	}
	
	/**(OvO)
	 * 获取指定商家的订单数
	 * @param merchantIds	商家ID
	 * @param period		统计时段
	 * @return
	 */
	private BigDecimal getOrderCount(List<Integer> merchantIds, String period){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(o.id), 0) orderCount ");
		sql.append(" FROM `order` o ");
		sql.append(" WHERE ");
		sql.append(period);
		sql.append("  	AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	AND o.merchant_id IN ( ");
		sql.append(StringUtils.join(merchantIds, ","));
		sql.append(" ) ");
		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString());
		String resultStr = resultMap.get("orderCount").toString();
		return new BigDecimal(resultStr);
	}
	
	/**(OvO)
	 * 获取指定时间, 指定区域下的评论数
	 * @param merchantIds	商家ID
	 * @param period		统计时段
	 * @return	totalComment		本日评价数
	 * 			todayOrderComment	本日订单评价数
	 * 			4s					4星评价
	 * 			3s					3星评价
	 * 			2s					2星评价
	 * 			1s					1星评价
	 */
	private Map<String, Object> getCommentCount(List<Integer> merchantIds, String period){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(CASE WHEN grades.create_time >= UNIX_TIMESTAMP(CURDATE()) THEN 1 ELSE NULL END), 0) todayOrderComment ");
		sql.append("  	,IFNULL(COUNT(1),0) totalComment ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN grades.grade >=4 AND grades.grade<5 THEN 1 ELSE NULL END), 0) 4s ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN grades.grade >=3 AND grades.grade<4 THEN 1 ELSE NULL END), 0) 3s ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN grades.grade >=2 AND grades.grade<3 THEN 1 ELSE NULL END), 0) 2s ");
		sql.append(" 	,IFNULL(COUNT(CASE WHEN grades.grade >=1 AND grades.grade<2 THEN 1 ELSE NULL END), 0) 1s ");
		sql.append(" FROM( ");
		sql.append(" 	SELECT SUM(ocom.grade)/COUNT(1) grade, o.create_time ");
		sql.append(" 	FROM `order` o, 0085_order_comment ocom ");
		sql.append(" WHERE ");
		sql.append(period);
		sql.append(" 		AND o.merchant_id IN ( ");
		sql.append(StringUtils.join(merchantIds, ","));
		sql.append(" 		) ");
		sql.append(" 		AND ocom.invalid = 0 AND ocom.comment_display <> 'N' AND o.id = ocom.order_id ");
		sql.append(" 	GROUP BY ocom.order_id ");
		sql.append(" )grades ");
		return this.findOneForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 根据入参构建SQL时间条件(评价表)
	 * @param vo	搜索条件VO
	 * @return	SQL的时间条件
	 */
	private String getCommentPeriod(SearchVo vo){
		String period = vo.getTimeType();
		if("week".equals(period)){
			return " ocom.comment_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(period)){
			return " ocom.comment_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}
		return " ocom.comment_time >= UNIX_TIMESTAMP(CURDATE()) ";
	}
	
	/**(OvO)
	 * 根据入参构建SQL时间条件(订单表)
	 * @param vo	搜索条件VO
	 * @return	SQL的时间条件
	 */
	private String getOrderPeriod(SearchVo vo){
		String period = vo.getTimeType();
		if("week".equals(period)){
			return " o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(period)){
			return " o.create_time >= UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}
		return " o.create_time >= UNIX_TIMESTAMP(CURDATE()) ";
	}
	
	/**(OvO)
	 * 根据入参构建SQL评价星级条件
	 * @param grade		评价星级条件(用于筛选)
	 * @return	SQL的星级筛选条件
	 */
	private String getGradeFactor(Integer grade){
		StringBuilder result = new StringBuilder();
		result.append(" HAVING grade < ");
		result.append(grade + 1);
		result.append(" AND grade >= ");
		result.append(grade);
		return result.toString();
	}
	
	/**(OvO)
	 * 根据区域ID获取商家ID
	 * @param orgIds	区域ID
	 * @return	商家ID List
	 */
	private List<Integer> getMerchantIds(List<Integer> orgIds){
		logger.debug("调用方法 getMerchantIds(orgIds: {}) 扫码首单总金额.", orgIds);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT morg.merchant_id ");
		sql.append(" FROM  0085_merchant_org morg ");
		sql.append(" WHERE morg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		List<Integer> merchantId = new ArrayList<Integer>();
		for(Map<String, Object> map: this.findForJdbc(sql.toString())){
			merchantId.add(Integer.valueOf(map.get("merchant_id").toString()));
		}
		return merchantId;
	}
	
	/***********************************************************************
	 ***********************************************************************/
	@Override
	public List<Map<String, Object>> getCommentClassify(Integer courierId) {
		List<Map<String, Object>> list = null;
		//今天时间
		int secondOfToday = (int)(new Date().getTime()/1000);
		Integer today = this.getStartTimeOfTheDayInSecond(secondOfToday);
		if (courierId == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl:参数错误！");
		} else {
			List<Integer> courierIds = null;
			/**
			 * 判断courierId是否合作商用户ID, 控制流程
			 * 获取需要查询的快递员列表
			 */
			if(this.courierMyInfoService.isAgentUser(courierId)){
				courierIds = this.agentService.getAgentCourierIds(courierId);
			}else{
				courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
			}
			
			StringBuilder query = new StringBuilder();
			query.append("  select sum((case when a.grade=1 then 1 else 0 end )) as one,sum((case when a.grade=2 then 1 else 0 end )) as two,");
			query.append("  sum((case when a.grade=3 then 1 else 0 end )) as three,");
			query.append("  sum((case when a.grade=4 then 1 else 0 end )) as four,");
			query.append("  sum((case when a.grade=5 then 1 else 0 end )) as five");
			query.append("    from 0085_order_comment a ,`order` o");
			query.append("  where a.comment_target =0 and o.courier_id !=0 and a.order_id=o.id and o.courier_id in("
					+ StringUtils.join(courierIds, ",")
					+ ") and a.comment_time> ? ");

			list = findForJdbc(query.toString(),today);
		}

		return list;
	}

	@Override
	public List<Map<String, Object>> getCommentByStar(Integer courierId,
			Integer star, Integer page, Integer rows) {
		//今天时间
		int secondOfToday = (int)(new Date().getTime()/1000);
		Integer today = this.getStartTimeOfTheDayInSecond(secondOfToday);
		
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		if (courierId == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl.getCommentByStar():参数错误！");
		} else {
			List<Integer> courierIds = null;
			/**
			 * 判断courierId是否合作商用户ID, 控制流程
			 * 获取需要查询的快递员列表
			 */
			if(this.courierMyInfoService.isAgentUser(courierId)){
				courierIds = this.agentService.getAgentCourierIds(courierId);
			}else{
				courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
			}
			
			if (courierIds == null || courierIds.isEmpty()) {
				throw new IllegalArgumentException("管辖下没有快递员");
			}
			StringBuilder query = new StringBuilder();
			query.append("  select oro.id as commentId ,oro.user_id as userId,u.username as commenterName,oro.grade ,o.pay_time as time from 0085_order_comment oro,`user` u ,`order` o ");
			query.append("  where oro.comment_target =0 and oro.order_id =o.id and oro.user_id=u.id and oro.grade= ?  and o.courier_id in("
					+ StringUtils.join(courierIds, ",")
					+ ")and oro.comment_time>?");
			query.append("  order by oro.comment_time DESC");
			query.append("  limit ?,? ");
			logger.info("query{}：", query.toString());
			list = findForJdbc(query.toString(), star, today, (page - 1) * rows, rows);
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				Integer userId = (Integer) map.get("userId");
				// 用户等级
				String custType = this.getCustType(userId);
				map.put("custType", custType);
				Long commentId = (Long) map.get("commentId");
				Map<String, Object> detail = this.getDetailComment(commentId.intValue());
				if (detail != null) {
					map.put("detail", detail);
				}

			}

		}

		return list;
	}

	@Override
	public List<Map<String, Object>> getCommentByCustType(Double min,
			Double max, Integer page, Integer rows, Integer courierId) {
		//今天时间
		int secondOfToday = (int)(new Date().getTime()/1000);
		Integer today = this.getStartTimeOfTheDayInSecond(secondOfToday);
		List<Map<String, Object>> list = null;
		logger.info(
				"invoke method getCommentByCustType, params:{},{},{},{},{}",
				min, max, page, rows, courierId);

		if (courierId == null && page == null && rows == null && min == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl.getCommentByCustType 参数错误");
		}
		List<Integer> courierIds = null;
		/**
		 * 判断courierId是否合作商用户ID, 控制流程
		 * 获取需要查询的快递员列表
		 */
		if(this.courierMyInfoService.isAgentUser(courierId)){
			courierIds = this.agentService.getAgentCourierIds(courierId);
		}else{
			courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
		}

		if (courierIds == null || courierIds.isEmpty()) {
			throw new IllegalArgumentException("管辖下没有快递员");
		}
		if (max != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("  select oro.id as commentId ,oro.user_id as userId,u.username as commenterName,oro.grade ,o.pay_time as time from 0085_order_comment oro,`user` u ,`order` o");
			sql.append("  where oro.comment_target =0 and oro.order_id =o.id and oro.user_id=u.id and u.id in (");
			sql.append("  select orr.user_id ");
			sql.append("  from `order` orr where orr.state='confirm' and orr.pay_state='pay' and orr.pay_id is not null ");
			sql.append("  group by orr.user_id");
			sql.append("  HAVING SUM(orr.origin)>= ? and SUM(orr.origin)<? )");
			sql.append("  and oro.comment_time> ?");
			sql.append("  and o.courier_id in("
					+ StringUtils.join(courierIds, ",") + ")");
			sql.append("  order by oro.comment_time DESC ");
			sql.append("  limit ?,?");
			list = this.findForJdbc(sql.toString(), min, max,today,(page - 1) * rows, rows);

		} else {
			StringBuilder sql = new StringBuilder();
			sql.append("  select oro.id as commentId ,oro.user_id as userId,u.username as commenterName,oro.grade ,o.pay_time as time from 0085_order_comment oro,`user` u ,`order` o");
			sql.append("  where oro.comment_target =0 and oro.order_id =o.id and oro.user_id=u.id and u.id in (");
			sql.append("  select orr.user_id ");
			sql.append("  from `order` orr where orr.state='confirm' and orr.pay_state='pay' and orr.pay_id is not null ");
			sql.append("  group by orr.user_id");
			sql.append("  HAVING SUM(orr.origin)>= ?  )");
			sql.append("  and oro.comment_time> ?");
			sql.append("  and o.courier_id in("
					+ StringUtils.join(courierIds, ",") + ")");
			sql.append("  order by oro.comment_time DESC ");
			sql.append("  limit ?,?");
			list = this.findForJdbc(sql.toString(), min, (page - 1) * rows,today,
					rows);
		}

		for (Map<String, Object> map : list) {
			Long commentId = (Long) map.get("commentId");
			Integer userId = (Integer) map.get("userId");
			// 用户等级
			String custType = this.getCustType(userId);
			map.put("custType", custType);
			Map<String, Object> detail = this.getDetailComment(commentId.intValue());
			if (detail != null) {
				map.put("detail", detail);
			}
		}

		return list;

	}

//	上线要解注释并放入                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	@Override
	public List<Map<String, Object>> getComment(Integer courierId,
			Integer page, Integer rows) {
		//今天时间
		int secondOfToday = (int)(new Date().getTime()/1000);
		Integer today = this.getStartTimeOfTheDayInSecond(secondOfToday);
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		logger.info("getComment:{}{}{}", courierId, page, rows);
		if (courierId == null && page == null && rows == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl.getComment 参数错误");
		} else {
			
			List<Integer> courierIds = null;
			
			boolean falg = courierMyInfoService.isAgentUser(courierId);
			
			if(falg){
				courierIds = courierOrgServiceImpl.getPartnerUserId(courierId);
			}else{
				courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
				// courierIds = courierOrgServiceImpl.getManageCouriersIdTwo(courierId);// 优化后的
			}
			
			
			
			if (courierIds == null || courierIds.isEmpty()) {
				throw new IllegalArgumentException("管辖下没有快递员");
			}else{
			StringBuffer query = new StringBuffer();
			query.append("  select oro.id as commentId ,oro.user_id as userId,u.username as commenterName,oro.grade ,o.pay_time as time from 0085_order_comment oro,`user` u ,`order` o ");
			query.append("  where oro.comment_target =0 and oro.order_id =o.id and oro.user_id=u.id   and o.courier_id in("
					+ StringUtils.join(courierIds, ",")
					+ ") and oro.comment_time> ? " );
			query.append("  order by oro.comment_time DESC");
			list = findForJdbcParam(query.toString(), page, rows,today);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					Integer userId = (Integer) map.get("userId");
					// 用户等级
					String custType = this.getCustType(userId);
					map.put("custType", custType);
					Long commentId = (Long) map.get("commentId");
					Map<String, Object> detail = this.getDetailComment(commentId.intValue());
					if (detail != null) {
						String content = detail.get("tags") +"   "+detail.get("content");
						detail.put("content",content);
						map.put("detail", detail);
					}

				}
			}
		}
		}
		return list;
	}
	
	@Override
	public Map<String, Object> getDetailComment(Integer commentId) {
		Map<String, Object> map = null;
		List<Map<String,Object>> list = null;
		logger.info("getDetailComment:{}", commentId);
		if (commentId == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl.getDetailComment 参数错误");
		} else {

			StringBuffer query = new StringBuffer();
			query.append("  select oro.user_id as userId,u.username as commenterName,oro.grade ,o.id,usu.username as courierName,u.mobile as userMobile,");
			query.append("  usu.mobile as courierMobile ,oro.comment_content as content, oro.tags ");
			query.append("  from 0085_order_comment oro, `user` u,`order` o ,`user` usu ");
			query.append("  where  oro.order_id=o.id and o.user_id = u.id and o.courier_id=usu.id and oro.id=? ");
//			query.append("  and oro.comment_time>?");
//			query.append("  order by oro.comment_time DESC");
			map = findOneForJdbc(query.toString(), commentId);
			
			if(map != null && map.size()!=0){
				list = this.getReturnVisit(((BigInteger)map.get("id")).intValue());
				if(list.size()==0){
					map.put("returnVisit", 01);
				}else if((Integer)list.get(0).get("courierId")==0){
					map.put("returnVisit", 02);
				}else if((Integer)list.get(0).get("courierId")!=0){
					map.put("returnVisit", 03);
				}
			}
			logger.info("query:{}", query.toString());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getCustTypeList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rule.id, rule.type_name typeName, rule.type_desc typeDesc, rule.amount ");
		sql.append("FROM 0085_custtype_rule rule ");
		sql.append("WHERE invalid=0");
		sql.append(" order by rule.amount desc");
		return this.findForJdbc(sql.toString());
	}

	@Override
	public List<Map<String, Object>> orderCommentSearch(List<Integer> orgIds,
			Integer page, Integer rows, SearchVo searchVo) {
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		if (CollectionUtils.isEmpty(orgIds)) {
			logger.error("订单统计参数为空：orgIds=null");
			throw new IllegalArgumentException("orgIds=null");
		}
		// 按时间类型搜索： 按天：day 按周week 按月：month 自定义：other
		String timewhere = "";
		if ("day".equals(searchVo.getTimeType())) {
			timewhere = " and  DATE(FROM_UNIXTIME(o.complete_time)) = CURDATE()";
		} else if ("week".equals(searchVo.getTimeType())) {
			timewhere = " and date(FROM_UNIXTIME(o.complete_time)) >= DATE_SUB(curdate(), INTERVAL 1 WEEK) ";
		} else if ("month".equals(searchVo.getTimeType())) {
			timewhere = " and date(FROM_UNIXTIME(o.complete_time)) >= DATE_SUB(curdate(), INTERVAL 1 MONTH) ";
		} else if ("other".equals(searchVo.getTimeType())) {
			if (searchVo.getBeginTime() != null && searchVo.getBeginTime() != 0
					&& searchVo.getEndTime() != null
					&& searchVo.getEndTime() != 0) {
				timewhere = " and date(FROM_UNIXTIME(o.complete_time)) >= date(FROM_UNIXTIME("
						+ searchVo.getBeginTime() + ")) ";
				timewhere += " and date(FROM_UNIXTIME(o.complete_time)) <= date(FROM_UNIXTIME("
						+ searchVo.getEndTime() + ")) ";
			} else if (searchVo.getBeginTime() != null
					&& searchVo.getBeginTime() != 0) {
				timewhere = " and date(FROM_UNIXTIME(o.complete_time)) >= date(FROM_UNIXTIME("
						+ searchVo.getBeginTime() + ")) ";
			} else if (searchVo.getEndTime() != null
					&& searchVo.getEndTime() != 0) {
				timewhere = " and date(FROM_UNIXTIME(o.complete_time)) <= date(FROM_UNIXTIME("
						+ searchVo.getEndTime() + ")) ";
			}
		}

		StringBuilder sql = new StringBuilder();
		sql.append(" select oro.id as commentId ,oro.user_id as userId,u.username as commenterName,oro.grade ,o.pay_time as time  ");
		sql.append(" from 0085_order_comment oro,`user` u ,`order` o,0085_courier_org cor ");

		sql.append(" where oro.comment_target =0 and cor.courier_id =o.courier_id and o.id = oro.order_id and oro.user_id=u.id ");
		sql.append(" 	and cor.org_id in (" + StringUtils.join(orgIds, ",")
				+ ")  ");
		sql.append(timewhere);
		sql.append(" order by oro.comment_time DESC  ");

		list = this.findForJdbc(sql.toString(), page, rows);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				Integer userId = (Integer) map.get("userId");
				// 用户等级
				String custType = this.getCustType(userId);
				map.put("custType", custType);
				Long commentId = (Long) map.get("commentId");
				Map<String, Object> detail = this.getDetailComment(commentId.intValue());
				if (detail != null) {
					map.put("detail", detail);
				}

			}
		}
		return list;

	}

	@Override
	public List<Integer> getOrgIds(Integer orgId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Integer> orgIds = new ArrayList<Integer>();
		logger.info("getOrgIds params:{}", orgId);
		if (orgId == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl getOrgIds :参数错误");
		}
		StringBuffer query = new StringBuffer();
		query.append("  select orgs.id from 0085_org orgs ");
		query.append("  where orgs.p_path like (");
		query.append("  select CONCAT(c.p_path,CONCAT(c.id,'_%')) from ");
		query.append("  0085_org c where c.id =?) AND orgs.status=1");
		list = findForJdbc(query.toString(), orgId);
		// 判断查询结果是否为空
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				orgIds.add((Integer) map.get("id"));
			}
		} else {
			orgIds.add(orgId);
		}
		return orgIds;
	}

	@Override
	public List<Map<String, Object>> getReturnVisit(Integer orderId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		logger.info("getOrgIds params:{}", orderId);
		if (orderId == null) {
			throw new IllegalArgumentException(
					"CourierCommentServiceImpl getReturnVisit :参数错误");
		}
		StringBuffer query = new StringBuffer();
		query.append("  select order_id as orderId ,user_id as courierId from waimai_order_follow  where order_id =? ");
		list = findForJdbc(query.toString(), orderId);
		return list;
	}

	/**
	 * 获取客户类型
	 * 
	 * @return
	 */
	public String getCustType(Integer userId) {
		String custType = "1";
		/*String sql = "select count(*) from `order` where user_id=? ";
		Long hisOrders = this
				.getCountForJdbcParam(sql, new Object[] { userId });
		if (hisOrders > 0) {
			sql = "select sum(origin) total from `order` where state='confirm' and pay_state='pay' and pay_id is not null and user_id=? group by user_id ";
			List<Map<String, Object>> list = this.findForJdbc(sql,
					new Object[] { userId });
			for (Map<String, Object> map : list) {
				Double total = Double.parseDouble(map.get("total").toString());
				List<CustTypeRuleEntity> rules = custTypeRuleServiceI
						.findHql("from CustTypeRuleEntity order by amount asc");
				for (CustTypeRuleEntity rule : rules) {
					if (rule.getAmount() >= (total*100)) {
						Integer type =Integer.parseInt(rule.getTypeName().substring(3))-1;
						return type.toString();
					}
				}
			}
		}*/
		return custType;
	}

	/**
	 * @return 获取今天0:0:0 的时间戳
	 *
	 * 
	 * @author zy
	 */
	public  Timestamp getTodayTimestamp() {
		Calendar theDayStart = Calendar.getInstance();
		theDayStart.setTimeInMillis(System.currentTimeMillis());
		theDayStart.set(Calendar.HOUR, 0);
		theDayStart.set(Calendar.MINUTE, 0);
		theDayStart.set(Calendar.SECOND, 0);
		theDayStart.set(Calendar.MILLISECOND, 0);
		return new Timestamp(theDayStart.getTime().getTime());
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

}
