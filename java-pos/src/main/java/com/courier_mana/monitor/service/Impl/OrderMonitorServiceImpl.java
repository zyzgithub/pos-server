package com.courier_mana.monitor.service.Impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.OrderMonitorService;
import com.courier_mana.personal.service.CourierMyInfoService;

@Service
public class OrderMonitorServiceImpl extends CommonServiceImpl implements
		OrderMonitorService {

	@Autowired
	private CourierOrgServicI courierOrgServic;
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	@Override
	public List<Map<String, Object>> getNosendOrder(Integer page, Integer rows,
			List<Integer> merchantIds) {
		StringBuilder query = new StringBuilder();
		query.append("  SELECT o.realname,o.time_remark as sendTime,o.order_num as orderNum,o.mobile,o.id as orderId,o.pay_id as payId,o.remark,m.title as merchantTitle,m.id as merchantId,o.address,o.order_type as orderType,o.user_id as userId,o.from_type as orderFrom,c.org_name orgName ");
		query.append("  from `order` o left JOIN  merchant m on o.merchant_id=m.id LEFT JOIN 0085_merchant_org b on b.merchant_id=m.id left join  0085_org c on c.id=b.org_id ");
		query.append("  where  o.state in ('pay','accept') and o.courier_id=0 and o.sale_type = 1 and o.order_type = 'normal' and m.id in("
				+ StringUtils.join(merchantIds, ",") + ")  ");
		 query.append("  and DATE(FROM_UNIXTIME(o.pay_time))= curdate() ");
		query.append("  order by o.pay_time asc");

		return findForJdbc(query.toString(), page, rows);
	}
	
	// 催单
	@Override
	public List<Map<String, Object>> getUrgeOrder(Integer page, Integer rows,
			List<Integer> merchantIds) {

		List<Map<String, Object>> list = null;
		Map<String, Object> couriers = null;
		StringBuilder query = new StringBuilder();

		query.append("  select o.realname,o.time_remark as sendTime,o.order_num as orderNum,o.mobile,o.id as orderId,o.pay_id as payId,o.remark,m.title as merchantTitle ,m.id as merchantId,o.address,o.order_type as orderType,o.courier_id as courierId,o.from_type as orderFrom,o.user_id as userId");
		query.append("  from  `order`  o ,merchant m , ");
		query.append("  (select ormi.order_id   ");
		query.append("  from 0085_order_reminder ormi where  ormi.order_id not in  ");
		query.append("  (select ormc.order_id from 0085_order_reminder ormc where ormc.state='resolve') ");
		query.append("  group by ormi.order_id");
		query.append("   )b");
		query.append("  where   o.merchant_id=m.id  AND b.order_id = o.id ");
		query.append("  and m.id in("
				+ StringUtils.join(merchantIds, ",") + ")");
		
		query.append("  and o.pay_time >= UNIX_TIMESTAMP(CURDATE()) ");
		query.append("  order by o.pay_time desc ");

		list = findForJdbc(query.toString(), page, rows);

		if (list != null) {
			for (Map<String, Object> map : list) {
				Integer courierI = ((Long) map.get("courierId")).intValue();
				if (courierI != 0) {
					couriers = this.getCourier(courierI);
					// 快递员的名字
					String courierName = (String) couriers.get("username");
					String mobile = (String) couriers.get("mobile");
					map.put("courierName", courierName);
					map.put("courierMobile", mobile);
				}
				String orderType = (String)map.get("orderType");
				//保证orderType 只返回4中状态
				if(!"mobile".equals(orderType)){
					map.put("orderType", "normal");
				}
				// 判断是否 为私厨，众包，电话订单 在 orderType 只允许一种存在，优先级为 私厨>众包>电话订单
				String crowdSourse = (String) map.get("orderFrom");
				Integer merchantId = ((BigInteger) map.get("merchantId")).intValue();
				Map<String, Object> privateOrder = this
						.getPrivateOrder(merchantId);
				Integer type = null;
				if (privateOrder != null) {
					type = (Integer) map.get("private");
				}
				if ("crowdsourcing".equals(crowdSourse)) {
					Map<String, Object> merchantDetail = this
							.getMerchantDetail(merchantId);
					map.put("orderType", "crowd");
					map.put("merchantUsername",
							(String) merchantDetail.get("merchantUsername"));
					map.put("merchantMobile",
							(String) merchantDetail.get("merchantMobile"));
					map.put("merchantAddress",
							(String) merchantDetail.get("merchantAddress"));
				}
				if (type != null && type == 2) {
					map.put("orderType", "private");
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getUrgeCount(Integer courierId) {

		Map<String, Object> map = null;
		StringBuilder query = new StringBuilder();
		List<Integer> merchantIds = this.getMerchantIds(courierId);

		query.append("  select count(o.id) as urgeCount");
		query.append("  from  `order`  o ,merchant m , ");
		query.append("  (select ormi.order_id  ");
		query.append("  from 0085_order_reminder ormi where  ormi.order_id not in  ");
		query.append("  (select ormc.order_id from 0085_order_reminder ormc where ormc.state='resolve') ");
		query.append("  group by ormi.order_id");
		query.append("  )b");
		query.append("  where m.id in("+ StringUtils.join(merchantIds, ",") + ")");
		query.append("  and  o.merchant_id=m.id  AND b.order_id = o.id ");
		query.append("  and DATE(FROM_UNIXTIME(o.pay_time)) = CURDATE()");
		map = findOneForJdbc(query.toString());
		return map;

	}

	@Override
	public List<Integer> getAddressId(Integer courierId) {
		// 获得管理员管辖下的所有架构id
		List<Integer> addressIds = new ArrayList<Integer>();
		List<Integer> orgIds = courierOrgServic.getManageOrgIds(courierId);
		if (orgIds.size() == 0) {
			throw new IllegalArgumentException("没有管辖区域");
		} else {
			StringBuilder query = new StringBuilder();
			query.append("  select addrs.id as addressId,org.id as orgId from 0085_org org ,0085_building building ,address addrs");
			query.append("  where addrs.building_id=building.id and building.org_id=org.id and  org.id in("
					+ StringUtils.join(orgIds, ",") + ") ");

			List<Map<String, Object>> addressIdss = findForJdbc(query
					.toString());
			if (addressIdss.size() != 0) {

				for (Map<String, Object> map : addressIdss) {
					addressIds.add(((Long) map.get("addressId")).intValue());
				}
			}
		}
		return addressIds;
	}
	
	@Override
	public List<Integer> getMerchantIdsTwo(Integer courierId){
		String sql = "select merchant_id id from 0085_merchant_org  where org_id in (\n" +
					 "		select o.id from 0085_org o where o.p_path like (select CONCAT(o.p_path,o.id,'_%') path from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id="+ courierId +") and o.status = 1 and o.`level` !=6 ) and o.status = 1\n" +
					 "		UNION ALL\n" +
					 "		select o.id from 0085_org o where o.id = (select co.org_id from 0085_courier_org co where co.courier_id="+ courierId +") and o.status = 1 and o.`level` = 6	\n" +
					 " 	)";
		List<Map<String, Object>> list = findForJdbc(sql);
		List<Integer> result = new ArrayList<Integer>(list.size());
		
		for(Map<String, Object> map :list){
			result.add((Integer)map.get("id"));
		}
		return result;
	}
	
	@Override
	public List<Integer> getMerchantIds(Integer courierId) {
		// 获得管理员管辖下的所有架构id
		List<Integer> merchantIds = new ArrayList<Integer>();
		List<Integer> orgIds = courierOrgServic.getManageOrgIds(courierId);
		if (orgIds.size() == 0){
			throw new IllegalArgumentException("没有管辖区域");
		} else {
			StringBuilder query = new StringBuilder();
			query.append("  select merchant_id from 0085_merchant_org  where org_id in ("+StringUtils.join(orgIds, ",")+")");
			List<Map<String, Object>> merchantIdss = findForJdbc(query
					.toString());
			if (merchantIdss.size() != 0) {

				for (Map<String, Object> map : merchantIdss){
					/**(OvO)
					 * 如果商家ID是2664(会员充值), 则跳过
					 */
					if(((Integer) map.get("merchant_id")).equals(2664)){
						continue;
					}
					merchantIds.add(((Integer) map.get("merchant_id")).intValue());
				}
			}
		}
		return merchantIds;
	}

	@Override
	public List<Map<String, Object>> getCastFifteen(Integer courierId,
			Integer page, Integer rows) {
		
		Boolean falg = this.courierMyInfoService.isAgentUser(courierId);
		List<Integer> courierIds = null;
		if(falg){
			courierIds = courierOrgServic.getPartnerUserId(courierId);
		}else{
			courierIds = courierOrgServic.getManageCouriersId(courierId);
		}
		List<Map<String, Object>> list = null;
		Map<String, Object> couriers = null;
		if (courierIds.size() == 0) {
			throw new IllegalArgumentException("没有管辖地址");
		} else {
			StringBuilder query = new StringBuilder();
			query.append(" SELECT o.realname,o.time_remark as sendTime,o.order_num as orderNum,o.mobile,o.id as orderId,o.pay_id as payId,o.remark,m.title as merchantTitle ,m.id as merchantId,o.address,o.order_type as orderType,o.courier_id as courierId,o.from_type as orderFrom,o.user_id as userId, o.time_remark ");
			query.append(" FROM `order` o, merchant m ");
			query.append(" WHERE o.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
			query.append(" 	AND o.order_type = 'normal' AND sale_type = 1 AND pay_state = 'pay' AND o.state <> 'confirm' ");
			query.append(" 	AND o.courier_id IN ( ");
			query.append(StringUtils.join(courierIds, ","));
			query.append(" 	) ");
			query.append(" 	AND m.id = o.merchant_id ");
			query.append(" HAVING UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(CONCAT(CURDATE(),' ' , RIGHT(o.time_remark, 5))) > -900 ");
			query.append(" 	AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(CONCAT(CURDATE(),' ' , RIGHT(o.time_remark, 5))) <=0 ");
			query.append(" ORDER BY o.pay_time ");
			// 获取当前时间
//			Long time = System.currentTimeMillis() / 1000;
			list = findForJdbcParam(query.toString(), page, rows);
			// 快递员信息查询和上部分分离
			if (list != null) {
				for (Map<String, Object> map : list) {
					Integer courierI = ((Long) map.get("courierId")).intValue();
					if (courierI != 0) {
						couriers = this.getCourier(courierI);
						// 快递员的名字
						String courierName = (String) couriers.get("username");
						String mobile = (String) couriers.get("mobile");
						map.put("courierName", courierName);
						map.put("courierMobile", mobile);
					}
					String orderType = (String)map.get("orderType");
					//保证orderType 只返回4中状态
					if(!"mobile".equals(orderType)){
						map.put("orderType", "normal");
					}
					// 判断是否 为私厨，众包，电话订单 在 orderType 只允许一种存在，优先级为 私厨>众包>电话订单
					String crowdSourse = (String) map.get("orderFrom");
					Integer merchantId = ((BigInteger) map.get("merchantId"))
							.intValue();
					Map<String, Object> privateOrder = this
							.getPrivateOrder(merchantId);
					Integer type = null;
					if (privateOrder != null) {
						type = (Integer) map.get("private");
					}
					if ("crowdsourcing".equals(crowdSourse)) {
						Map<String, Object> merchantDetail = this
								.getMerchantDetail(merchantId);
						map.put("orderType", "crowd");
						map.put("merchantUsername",
								(String) merchantDetail.get("merchantUsername"));
						map.put("merchantMobile",
								(String) merchantDetail.get("merchantMobile"));
						map.put("merchantAddress",
								(String) merchantDetail.get("merchantAddress"));
					}
					if (type != null && type == 2) {
						map.put("orderType", "private");
					}
				}
			}

		}
		return list;
	}

	public Map<String, Object> getCourier(Integer courierId) {
		Map<String, Object> map = null;
		if (courierId == null) {
			throw new IllegalArgumentException("getCourier() :参数错误");
		} else {
			StringBuilder query = new StringBuilder();
			query.append("select  u.mobile,u.username from `user` u where u.id =? ");
			map = findOneForJdbc(query.toString(), courierId);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getCastThirdty(Integer courierId,
			Integer page, Integer rows) {
		
		Boolean falg = this.courierMyInfoService.isAgentUser(courierId);
		List<Integer> courierIds = null;
		if(falg){
			courierIds = courierOrgServic.getPartnerUserId(courierId);
		}else{
			courierIds = courierOrgServic.getManageCouriersId(courierId);
		}
		
		
		
		
		
		List<Map<String, Object>> list = null;
		Map<String, Object> couriers = null;
		if (courierIds.size() == 0) {
			throw new IllegalArgumentException("没有管辖地址");
		} else {
//			Long time = System.currentTimeMillis()/1000;
			StringBuilder query = new StringBuilder();
			query.append(" SELECT o.realname,o.time_remark as sendTime,o.order_num as orderNum,o.mobile,o.id as orderId,o.pay_id as payId,o.remark,m.title as merchantTitle ,m.id as merchantId,o.address,o.order_type as orderType,o.courier_id as courierId,o.from_type as orderFrom,o.user_id as userId, o.time_remark ");
			query.append(" FROM `order` o, merchant m ");
			query.append(" WHERE o.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
			query.append(" 	AND o.order_type = 'normal' AND sale_type = 1 AND pay_state = 'pay' AND o.state <> 'confirm' ");
			query.append(" 	AND o.courier_id IN ( ");
			query.append(StringUtils.join(courierIds, ","));
			query.append(" 	) ");
			query.append(" 	AND m.id = o.merchant_id ");
			query.append(" HAVING UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(CONCAT(CURDATE(),' ' , RIGHT(o.time_remark, 5))) > 0 ");
			query.append(" ORDER BY o.pay_time ");
			
			list = findForJdbcParam(query.toString(), page, rows);
			// 快递员信息查询和上部分分离
			if (list != null) {
				for (Map<String, Object> map : list) {
					Integer courierI = ((Long) map.get("courierId")).intValue();
					if (courierI != 0) {
						couriers = this.getCourier(courierI);
						// 快递员的名字
						String courierName = (String) couriers.get("username");
						String mobile = (String) couriers.get("mobile");
						map.put("courierName", courierName);
						map.put("courierMobile", mobile);
					}
					String orderType = (String)map.get("orderType");
					//保证orderType 只返回4中状态
					if(!"mobile".equals(orderType)){
						map.put("orderType", "normal");
					}
					// 判断是否 为私厨，众包，电话订单 在 orderType 只允许一种存在，优先级为 私厨>众包>电话订单
					String crowdSourse = (String) map.get("orderFrom");
					Integer merchantId = ((BigInteger) map.get("merchantId"))
							.intValue();
					Map<String, Object> privateOrder = this
							.getPrivateOrder(merchantId);
					Integer type = null;
					if (privateOrder != null) {
						type = (Integer) map.get("private");
					}
					if ("crowdsourcing".equals(crowdSourse)) {
						Map<String, Object> merchantDetail = this
								.getMerchantDetail(merchantId);
						map.put("orderType", "crowd");
						map.put("merchantUsername",
								(String) merchantDetail.get("merchantUsername"));
						map.put("merchantMobile",
								(String) merchantDetail.get("merchantMobile"));
						map.put("merchantAddress",
								(String) merchantDetail.get("merchantAddress"));
					}
					if (type != null && type == 2) {
						map.put("orderType", "private");
					}
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getPrivateOrder(Integer merchantId) {

		StringBuilder query = new StringBuilder();
		query.append("select  info.merchant_source as private from   0085_merchant_info info where info.merchant_id =? ");
		Map<String, Object> privateOrder = findOneForJdbc(query.toString(),
				merchantId);
		return privateOrder;

	}

	@Override
	public Map<String, Object> getMerchantDetail(Integer merchantId) {

		StringBuilder query = new StringBuilder();
		query.append("  select m.address as merchantAddress ,u.username as merchantUsername ,u.mobile as merchantMobile from merchant m ,`user` u where m.user_id = u.id and m.id =?");
		Map<String, Object> merchantDetail = findOneForJdbc(query.toString(),
				merchantId);
		return merchantDetail;
	}

	@Override
	public Integer assigCourier(Integer courierId, Integer orderId) {

		StringBuilder query = new StringBuilder();
		query.append(" update `order` set courier_id = ? where id = ?");
		return executeSql(query.toString(), courierId, orderId);
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
	
	/**
	 * 通过快递员Id查询其所管辖下的所有合作商家
	 */
	@Override
	public List<Integer> getPartnerMerchantIds(Integer courierId) {
		String sql = "select a.id from 0085_merchant_info a where a.platform_type = 2 and a.creator = "+ courierId +"";
		
		List<Integer> result = new ArrayList<Integer>();
		List<Map<String, Object>> list = this.findForJdbc(sql);
		for(Map<String,Object> map :list){
			result.add((Integer)map.get("id"));
		}
		return result;
	}

}
