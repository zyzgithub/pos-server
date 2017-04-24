package com.courier_mana.monitor.service.Impl;

import java.util.ArrayList;
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

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.CourierMerchantServicI;


@Service
public class CourierMerchantServiceImpl extends CommonServiceImpl implements CourierMerchantServicI {
	private static final Logger logger = LoggerFactory.getLogger(CourierMerchantServiceImpl.class);
	@Autowired
	private CourierOrgServicI courierOrgServiceImpl;
	
	@Override
	public List<Map<String, Object>> getMerchants(Integer courierId) {
		//数据校验
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询快递员管理的机构ID列表
		List<Integer> orgIds = courierOrgServiceImpl.getManageOrgIds(courierId);
		if(CollectionUtils.isEmpty(orgIds)){
			return new ArrayList<Map<String, Object>>();
		}
		StringBuilder queryMerchantIdsSql = new StringBuilder("SELECT  DISTINCT mo.merchant_id  ");
		queryMerchantIdsSql.append(" from 0085_merchant_org mo ");
		queryMerchantIdsSql.append(" where mo.org_id in ( " + StringUtils.join(orgIds, ",") + ")");
		
		logger.debug("executeSql>>>>>>"+queryMerchantIdsSql);
		//查询机构下的商家ID列表
		List<Map<String, Object>> merchantIdsMap = findForJdbc(queryMerchantIdsSql.toString());
		if(CollectionUtils.isEmpty(merchantIdsMap)){
			return new ArrayList<Map<String,Object>>();
		}
		
		List<Integer> merchantIds = new ArrayList<Integer>();
		for(Map<String, Object> merchantIdMap:merchantIdsMap){
			merchantIds.add(Integer.parseInt(merchantIdMap.get("merchant_id").toString()));
		}
		//根据Id所有的商家店铺
		StringBuilder queryMerchantsSql = new StringBuilder();
		queryMerchantsSql.append("select * from merchant where id in (" + StringUtils.join(merchantIds, ",") + ")");
		
		logger.debug("executeSql:"+queryMerchantsSql);
		
		return this.findForJdbc(queryMerchantsSql.toString());
	}

//	public Long getMerchantCount(Integer courierId) {
//		//数据校验
//		if(courierId == null){
//			throw new IllegalArgumentException("courierId=null");
//		}
//		//查询快递员管理的机构ID列表
//		List<Integer> orgIds = courierOrgServiceImpl.getManageOrgIds(courierId);
//		if(CollectionUtils.isEmpty(orgIds)){
//			return null;
//		}
//		StringBuilder queryMerchantIdsSql = new StringBuilder("SELECT  DISTINCT mo.merchant_id  ");
//		queryMerchantIdsSql.append(" from 0085_merchant_org mo ");
//		queryMerchantIdsSql.append(" where mo.org_id in ( " + StringUtils.join(orgIds, ",") + ")");
//		
//		logger.debug("executeSql>>>>>>"+queryMerchantIdsSql);
//		//查询机构下的商家ID列表
//		List<Map<String, Object>> merchantIdsMap = findForJdbc(queryMerchantIdsSql.toString());
//		if(CollectionUtils.isEmpty(merchantIdsMap)){
//			return null;
//		}
//		
//		List<Integer> merchantIds = new ArrayList<Integer>();
//		for(Map<String, Object> merchantIdMap:merchantIdsMap){
//			merchantIds.add(Integer.parseInt(merchantIdMap.get("merchant_id").toString()));
//		}
//		//根据Id所有的商家店铺
//		StringBuilder queryMerchantsSql = new StringBuilder();
//		queryMerchantsSql.append("select count(1) from merchant where is_delete = '0' and id in (" + StringUtils.join(merchantIds, ",") + ")");
//		
//		logger.debug("executeSql:"+queryMerchantsSql);
//		return this.getCountForJdbc(queryMerchantsSql.toString());
//	}

	
	
	@Override
	public Long getMerchantCount(String orgIdsStr) {
		if(orgIdsStr == null){
			return 0L;
		}
		StringBuilder sql = new StringBuilder(" SELECT COUNT(DISTINCT mo.merchant_id) ");
		sql.append(" FROM 0085_merchant_org mo, merchant m ");
		sql.append(" WHERE m.id = mo.merchant_id AND m.is_delete = 0 AND mo.org_id in( ");
		sql.append(orgIdsStr);
		sql.append(") ");
		return this.getCountForJdbc(sql.toString());
	}

	@Override
	public Long getBindMerchants(String orgIdsStr) {
		StringBuilder sql = new StringBuilder(" SELECT COUNT(DISTINCT mo.merchant_id) ");
		sql.append(" FROM 0085_merchant_org mo, 0085_courier_merchant cm ");
		sql.append(" WHERE mo.merchant_id = cm.merchant_id AND mo.org_id in(");
		sql.append(orgIdsStr);
		sql.append(") ");
		return this.getCountForJdbc(sql.toString());
	}

//	public List<Map<String, Object>> getBindMerchants(Integer courierId) {
//		//数据校验
//		if(courierId == null){
//			throw new IllegalArgumentException("courierId=null");
//		}
//		//查询快递员管理的机构ID列表（没有绑定快递员的）
//		List<Integer> orgIds = courierOrgServiceImpl.getManageOrgIds(courierId);
//		if(CollectionUtils.isEmpty(orgIds)){
//			return new ArrayList<Map<String, Object>>();
//		}
//		StringBuilder queryMerchantIdsSql = new StringBuilder("SELECT  DISTINCT mo.merchant_id  ");
//		queryMerchantIdsSql.append(" from 0085_merchant_org mo ");
//		queryMerchantIdsSql.append(" where mo.org_id in ( " + StringUtils.join(orgIds, ",") + ")");
//		
//		logger.debug("executeSql>>>>>>"+queryMerchantIdsSql);
//		//查询机构下的商家ID列表
//		List<Map<String, Object>> merchantIdsMap = findForJdbc(queryMerchantIdsSql.toString());
//		if(CollectionUtils.isEmpty(merchantIdsMap)){
//			return new ArrayList<Map<String,Object>>();
//		}
//		List<Integer> merchantIds = new ArrayList<Integer>();
//		for(Map<String, Object> merchantIdMap:merchantIdsMap){
//			merchantIds.add(Integer.parseInt(merchantIdMap.get("merchant_id").toString()));
//		}
//		
//		//查询绑定快递员的商家ID
//		StringBuilder queryBindMerchantIdsSql = new StringBuilder("SELECT  DISTINCT cm.merchant_id  ");
//		queryBindMerchantIdsSql.append(" from 0085_courier_merchant cm ");
//		queryBindMerchantIdsSql.append(" where cm.merchant_id in ( " + StringUtils.join(merchantIds, ",") + ")");
//		
//		logger.debug("executeSql>>>>>>"+queryBindMerchantIdsSql);
//		
//		List<Map<String, Object>> bindMerchantIdsMap = findForJdbc(queryBindMerchantIdsSql.toString());
//		if(CollectionUtils.isEmpty(bindMerchantIdsMap)){
//			return new ArrayList<Map<String,Object>>();
//		}
//		List<Integer> bindMerchantIds = new ArrayList<Integer>();
//		for(Map<String, Object> bindMerchantIdMap:bindMerchantIdsMap){
//			bindMerchantIds.add(Integer.parseInt(bindMerchantIdMap.get("merchant_id").toString()));
//		}
//		//根据Id所有的商家店铺
//		StringBuilder queryMerchantsSql = new StringBuilder();
//		queryMerchantsSql.append("select m.id,m.user_id,m.title,m.group_id,m.city_id,m.address,m.mobile,m.longitude,m.latitude,m.display,m.notice,m.start_time,m.end_time,m.delivery_time,m.logo_url,m.type,m.delivery_begin,m.deduction,m.income_date,m.dine_order_print from merchant where id in (" + StringUtils.join(bindMerchantIds, ",") + ")");
//		logger.debug("executeSql:"+queryMerchantsSql);
//		
//		return this.findForJdbc(queryMerchantsSql.toString());
//	}

	@Override
	public Long getUnBindMerchantCount(Integer courierId) {
		//数据校验
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询快递员管理的机构ID列表（没有绑定快递员的）
		List<Integer> orgIds = courierOrgServiceImpl.getManageOrgIds(courierId);
		if(CollectionUtils.isEmpty(orgIds)){
			return null;
		}
		//		select DISTINCT mo.merchant_id from 0085_merchant_org mo LEFT JOIN 0085_courier_merchant cm on mo.merchant_id = cm.merchant_id where mo.org_id in(10, 26, 34, 35, 102, 103, 159) and cm.courier_id is NULL;
		StringBuilder queryMerchantIdsSql = new StringBuilder("SELECT count(1) ");
		queryMerchantIdsSql.append(" from 0085_merchant_org mo LEFT JOIN 0085_courier_merchant cm on mo.merchant_id = cm.merchant_id");
		queryMerchantIdsSql.append(" where mo.org_id in ( " + StringUtils.join(orgIds, ",") + ") and cm.courier_id is NULL");
		
		logger.debug("executeSql>>>>>>"+queryMerchantIdsSql);
		//查询机构下的商家ID列表
		return this.getCountForJdbc(queryMerchantIdsSql.toString());
		
	}

	@Override
	public Map<String,Long> getAllAndUnBindMerchantCount(Integer courierId) {
		List<Integer> orgIds = this.courierOrgServiceImpl.getManageOrgIds(courierId);
		String orgIdsStr = StringUtils.join(orgIds, ",");
		
		Map<String,Long> retMap = new HashMap<String, Long>();
		long totalMerchantCounts = this.getMerchantCount(orgIdsStr);// 总商家数
		long bindMerchantCounts = this.getBindMerchants(orgIdsStr);// 已绑定商家数
		retMap.put("totalMerchantCounts", totalMerchantCounts);
		retMap.put("totalUnBindMerchantCounts", totalMerchantCounts - bindMerchantCounts);// 两个数量相减得出未绑定商家
		return retMap;
	}
	
}
