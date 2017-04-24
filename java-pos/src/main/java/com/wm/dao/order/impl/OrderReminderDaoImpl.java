package com.wm.dao.order.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Repository;

import com.base.enums.UserTypeEnum;
import com.wm.dao.order.OrderReminderDao;
import com.wm.entity.order.OrderReminderEntity;

@Repository("orderReminderDao")
@SuppressWarnings("unchecked")
public class OrderReminderDaoImpl extends
		GenericBaseCommonDao<OrderReminderEntity, Integer> implements
		OrderReminderDao {

	@Override
	public List<Map<String, Object>> queryByOrderIdAndMinute(Integer orderId,
			Integer minuteBefore, int page, int rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT id, order_id, from_unixtime(create_time,  '%Y-%m-%d %h:%i:%s') as create_time, remind_desc, resolver, from_unixtime(resolver_time,  '%Y-%m-%d %h:%i:%s') as resolver_time, resolver_desc ");
		sbsql.append("FROM 0085_order_reminder ");
		if (minuteBefore == null || minuteBefore == 0) {
			minuteBefore = 30;// 默认30分钟
		}
		sbsql.append("WHERE 1 = 1 AND ").append("  create_time BETWEEN ? AND UNIX_TIMESTAMP() ");
		if (orderId == null || orderId == 0) {
			// 不添加到where中
		} else {
			sbsql.append(" AND order_id = ? ");
		}
		sbsql.append(" order by create_time DESC ");
		int matches = StringUtils.countMatches(sbsql, "?");
		//计算minuteBefore的秒数
		Date date = org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(), -minuteBefore);
		int seconds = DateUtils.getSeconds(date);
		if (matches == 1) {
			return this.findForJdbcParam(sbsql.toString(), page, rows, seconds);
		}
		if (matches == 2) {
			return this.findForJdbcParam(sbsql.toString(), page, rows, seconds, orderId);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> queryByOrderIdAndDateTime(Integer orderId,
			String dtStart, String dtEnd, int page, int rows) {
		if (StringUtils.isBlank(dtStart) || StringUtils.isBlank(dtEnd)) {
			return null;
		}
		Date dateStart = null, dateEnd = null;
		try {
			dateStart = DateUtils._4y_m_d_h_m_s.parse(dtStart);
			dateEnd = DateUtils._4y_m_d_h_m_s.parse(dtEnd);
		} catch (ParseException e) {
		}
		if(dateStart == null || dateEnd == null){
			return null;
		}
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT id, order_id, from_unixtime(create_time,  '%Y-%m-%d %h:%i:%s') as create_time, remind_desc, resolver, from_unixtime(resolver_time,  '%Y-%m-%d %h:%i:%s') as resolver_time, resolver_desc ");
		sbsql.append("FROM 0085_order_reminder ");
		
		sbsql.append("WHERE 1 = 1 AND ").append("  create_time BETWEEN ? AND ? ");
		if (orderId == null || orderId == 0) {
			// 不添加到where中
		} else {
			sbsql.append(" AND order_id = ? ");
		}
		sbsql.append(" order by create_time DESC ");
		int matches = StringUtils.countMatches(sbsql, "?");
		//计算minuteBefore的秒数
		int secondsDateStart = DateUtils.getSeconds(dateStart);
		int secondsDateEnd = DateUtils.getSeconds(dateEnd);
		if (matches == 2) {
			return this.findForJdbcParam(sbsql.toString(), page, rows, secondsDateStart, secondsDateEnd);
		}
		if (matches == 3) {
			return this.findForJdbcParam(sbsql.toString(), page, rows, secondsDateStart, secondsDateEnd, orderId);
		}
		return null;
	}

	@Override
	public List<OrderReminderEntity> queryListByOrderId(int orderId, int first,
			int rows) {
		Class<OrderReminderEntity> clazz = OrderReminderEntity.class;
		String sql = " select * from 0085_order_reminder where order_id = ? order by create_time DESC";
		SQLQuery sqlQuery = this.createSqlQuery(sql, orderId).addEntity(clazz);
		sqlQuery.setFirstResult(first).setFetchSize(rows);
		return sqlQuery.list();
	}

	@Override
	public List<Map<String, Object>> queryByUserIdUserTypeAndMinute(
			Integer userId, Integer userType, Integer minuteBefore, int page,
			int rows) {
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("SELECT orderRem.id, orderRem.order_id, from_unixtime(orderRem.create_time,  '%Y-%m-%d %h:%i:%s') as create_time, ");
		sbsql.append(" orderRem.remind_desc, orderRem.resolver, from_unixtime(orderRem.resolver_time) as resolver_time, orderRem.resolver_desc ");
		sbsql.append("FROM 0085_order_reminder as orderRem , `order` as o ");
		
		if (minuteBefore == null || minuteBefore == 0) {
			minuteBefore = 30;// 默认30分钟
		}
		sbsql.append("WHERE 1 = 1 AND ").append("  orderRem.create_time BETWEEN ? AND UNIX_TIMESTAMP() AND o.id = orderRem.order_id ");
		if (userId == null || userId == 0 || userType == null ) {
			return null;
		} else if(userType == UserTypeEnum.MERCHANT.getTypeNum()){
			sbsql.append(" AND o.merchant_id = ? ");
		} else{
			sbsql.append(" AND o.courier_id = ? ");
		}
		sbsql.append(" order by orderRem.create_time DESC ");
		//计算minuteBefore的秒数
		Date date = org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(), -minuteBefore);
		int seconds = DateUtils.getSeconds(date);
		return this.findForJdbcParam(sbsql.toString(), page, rows, seconds, userId);
	}

	@Override
	public List<Map<String, Object>> queryByUserIdUserTypeAndDateTime(
			Integer userId, Integer userType, String dtStart, String dtEnd,
			int page, int rows) {

		if (StringUtils.isBlank(dtStart) || StringUtils.isBlank(dtEnd)) {
			return null;
		}
		Date dateStart = null, dateEnd = null;
		try {
			dateStart = DateUtils._4y_m_d_h_m_s.parse(dtStart);
			dateEnd = DateUtils._4y_m_d_h_m_s.parse(dtEnd);
		} catch (ParseException e) {
		}
		if(dateStart == null || dateEnd == null){
			return null;
		}
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT orderRem.id, orderRem.order_id, from_unixtime(orderRem.create_time,  '%Y-%m-%d %h:%i:%s') as create_time, ");
		sbsql.append(" orderRem.remind_desc, orderRem.resolver, from_unixtime(orderRem.resolver_time) as resolver_time, orderRem.resolver_desc ");
		sbsql.append("FROM 0085_order_reminder as orderRem , `order` as o ");
		sbsql.append("WHERE 1 = 1 AND ").append("  orderRem.create_time BETWEEN ? AND ? AND o.id = orderRem.order_id ");
		if (userId == null || userId == 0 || userType == null ) {
			return null;
		} else if(userType == UserTypeEnum.MERCHANT.getTypeNum()){
			sbsql.append(" AND o.merchant_id = ? ");
		} else{
			sbsql.append(" AND o.courier_id = ? ");
		}
		sbsql.append(" order by orderRem.create_time DESC ");

		//计算minuteBefore的秒数
		int secondsDateStart = DateUtils.getSeconds(dateStart);
		int secondsDateEnd = DateUtils.getSeconds(dateEnd);
		
		return this.findForJdbcParam(sbsql.toString(), page, rows, secondsDateStart, secondsDateEnd, userId);
	}
	

}
