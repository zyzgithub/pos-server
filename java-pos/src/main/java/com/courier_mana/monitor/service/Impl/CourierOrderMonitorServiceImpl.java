package com.courier_mana.monitor.service.Impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.CourierOrderMonitorServicI;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.wm.entity.courier.CourierLocationEntity;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;

@Service
public class CourierOrderMonitorServiceImpl extends CommonServiceImpl implements CourierOrderMonitorServicI {
	
	private static final Logger logger = LoggerFactory.getLogger(CourierOrderMonitorServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgServiceImpl;
	@Autowired
	private JpushServiceI jpushService;
	@Autowired 
	private WUserServiceI wuserService;
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	
	@Override
	public Long getCourierOrdersCount(List<Integer> courierIds,String[] states) {
		if(CollectionUtils.isEmpty(courierIds)){
			return null;
		}
		
		//查询管理员下今日所有的订单数
		StringBuilder queryOrdersSql = new StringBuilder();
		//String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if(states!= null && states.length>=1){
			StringBuffer where  = new StringBuffer();
			if(states.length>2){
                where.append("(");
				for (int i = 0; i < states.length; i++) {
					where.append(" state = "+ states[i]);
					if(i+1 != states.length){
						where.append(" or ");
					}
				}
				where.append(")");
			}else{
				where.append(" state = "+ states[0]);
			}
			queryOrdersSql.append("select count(1) from `order` where courier_id in (" +StringUtils.join(courierIds, ",") + ") And "+ where.toString() +"  And create_time >= UNIX_TIMESTAMP(CURDATE()) " );
			return this.getCountForJdbc(queryOrdersSql.toString());
		}else{
			queryOrdersSql.append("select count(1) from `order` where courier_id in (" + StringUtils.join(courierIds, ",") + ") And create_time >= UNIX_TIMESTAMP(CURDATE()) ");
			return this.getCountForJdbc(queryOrdersSql.toString());
		}
	}
	
	@Override
	public Long getCourierOrdersById(Integer courierId,String[] states) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询快递员下今日所有的订单数
		StringBuilder queryOrdersSql = new StringBuilder();
		if(states != null && states.length>=1){
			queryOrdersSql.append("select count(1) from `order` where courier_id = ? And state in(" + StringUtils.join(states, ",") + ") And pay_time >= UNIX_TIMESTAMP(CURDATE())");
			return this.getCountForJdbcParam(queryOrdersSql.toString(),new Object[]{courierId});
		}else{
			queryOrdersSql.append("select count(1) from `order` where courier_id = ? And pay_time >= UNIX_TIMESTAMP(CURDATE())");
			return this.getCountForJdbcParam(queryOrdersSql.toString(),new Object[]{courierId});
		}
	}

	@Override
	public Map<String, Object> getCourierOrdersCounts(Integer courierId) {
        List<Integer> courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
		Map<String, Object> map = new HashMap<String, Object>();
//		//返回总订单数
//		Long totalCount  = this.getCourierOrdersCount(courierIds, null);

//		//返回未配送订单数
//		Long acceptCount  = this.getCourierOrdersCount(courierIds, new String[]{"'accept'"});
		
		//返回催单订单数
		Long reminderCount  = this.getCourierOrdersReminderCount(courierId);
		map.put("reminderCount", reminderCount);
		
//		//返回配送中订单数
//		Long deliveryCount  = this.getCourierOrdersCount(courierIds, new String[]{"'delivery'"});
		
		//返回超时订单数
		Long outTimeCount  = this.getCourierOrdersOutTimeCount(courierIds);
		map.put("outTimeCount", outTimeCount);
		
//		//返回已完成订单数
//		Long succCount = this.getCourierOrdersCount(courierIds, new String[]{"'delivery_done'","'done'","'confirm'","'evaluated'"});
		
		/*
		 * 统计未配送、配送中、已完成订单数
		 */
		String courierIdsStr = StringUtils.join(courierIds, ",");
		List<String> orderStates = this.getCourierOrderState(courierIdsStr);
		int acceptCount = 0;
		int deliveryCount = 0;
		int succCount = 0;
		int totalCount = orderStates.size();
		
		for(String state:orderStates){
			if("accept".equals(state)){
				acceptCount++;
			} else if ("delivery".equals(state)){
				deliveryCount++;
			} else if ("delivery_done".equals(state) || "done".equals(state) || "confirm".equals(state) || "evaluated".equals(state)){
				succCount++;
			}
		}
		
		map.put("totalCount", totalCount);
		map.put("deliveryCount", deliveryCount);
		map.put("acceptCount", acceptCount);
		map.put("succCount", succCount);
		return map;
	}
	
	/**
	 * 获取指定快递员订单的状态(state字段)
	 * @author hyj
	 * @param courierIdsStr	若干个快递员ID(用","隔开中间不能有空格)
	 * @return
	 */
	private List<String> getCourierOrderState(String courierIdsStr){
		StringBuilder sql = new StringBuilder(" select `state` from `order` where courier_id in ( ");
		sql.append(courierIdsStr);
		sql.append(") And create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		return this.findListbySql(sql.toString());
	}

	@Override
	public Long getCourierOrdersReminderCount(Integer courierId) {
		List<Integer> courierIds = courierOrgServiceImpl.getManageOrgIds(courierId);
		//查询管理员下所有的催单的订单数
		StringBuilder queryOrdersSql = new StringBuilder();
//		queryOrdersSql.append("select count(1) from `order` left join (SELECT DISTINCT r.order_id FROM 0085_order_reminder r WHERE r.create_time >= UNIX_TIMESTAMP(CURDATE())) orem on id = orem.order_id where courier_id in (" + StringUtils.join(courierIds, ",") + ") And pay_time >= UNIX_TIMESTAMP(CURDATE())");
		queryOrdersSql.append("select count(0) from `order` o where pay_time >= unix_timestamp(curdate()) and id in (")
		.append("select distinct r.order_id from 0085_order_reminder r where r.create_time >= unix_timestamp(curdate())")
		.append(") and courier_id in (").append(StringUtils.join(courierIds, ",")).append(")");
		return this.findOneForJdbc(queryOrdersSql.toString(), Long.class);
	}
	
	public Long getCourierOrdersReminderCountById(Integer courierId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		//查询快递员下今日所有的催单订单数
		StringBuilder queryOrdersSql = new StringBuilder();
		queryOrdersSql.append("select IFNULL(COUNT(1),0) from `order` , (SELECT DISTINCT r.order_id FROM 0085_order_reminder r WHERE r.create_time >= UNIX_TIMESTAMP(CURDATE())) orem where id = orem.order_id AND courier_id = ? And pay_time >= UNIX_TIMESTAMP(CURDATE())");
		return this.getCountForJdbcParam(queryOrdersSql.toString(),new Object[]{courierId});
		
	}

	@Override
	public Long getCourierOrdersOutTimeCount(List<Integer> courierIds) {
		StringBuilder queryOrdersSql = new StringBuilder();
		queryOrdersSql.append(" select count(1) ");
		queryOrdersSql.append(" from `order` o  ");
		queryOrdersSql.append(" where pay_time >= UNIX_TIMESTAMP(CURDATE()) ");
		queryOrdersSql.append(" 	AND o.order_type = 'normal' AND sale_type = 1 AND pay_state = 'pay' AND o.state <> 'confirm' ");
		queryOrdersSql.append(" 	AND o.courier_id IN ( ");
		queryOrdersSql.append(StringUtils.join(courierIds, ","));
		queryOrdersSql.append(" 	) ");
		queryOrdersSql.append(" 	AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(CONCAT(CURDATE(),' ' , RIGHT(o.time_remark, 5))) > 0 ");
		return this.getCountForJdbc(queryOrdersSql.toString());
	}

	@Override
	public Map<String, Object> getCourierOrdersCountById(Integer courierId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//返回总订单数
		Long totalCount = this.getCourierOrdersById(courierId, null);
		map.put("totalCount", totalCount);
		//返回未配送订单数
		Long acceptCount = this.getCourierOrdersById(courierId, new String[]{"'accept'"});
		map.put("acceptCount", acceptCount);
		//返回催单订单数
		Long reminderCount = this.getCourierOrdersReminderCountById(courierId);
		map.put("reminderCount", reminderCount);
		//返回配送中订单数
		Long deliveryCount = this.getCourierOrdersById(courierId, new String[]{"'delivery'"});
		map.put("deliveryCount", deliveryCount);
		//返回已完成订单数
		Long succCount = this.getCourierOrdersById(courierId, new String[]{"'delivery_done','done','confirm','evaluated'"});
		map.put("succCount", succCount);
		return map;
	}

	@Override
	public List<Map<String, Object>> getOrderDetailByOrderId(Integer orderId) {
		if(orderId == null){
			throw new IllegalArgumentException("orderId=null");
		}
		StringBuilder queryOrderDetailSql = new StringBuilder();
		queryOrderDetailSql.append("select m.id,m.name,m.unit,om.quantity,m.price,om.total_price,o.delivery_fee as addup,o.cost_lunch_box as box from order_menu om,menu m,`order` o where om.order_id = m.id and o.id = om.order_id  and om.order_id = ?");
		return this.findForJdbc(queryOrderDetailSql.toString(),orderId);
	}
	
	/**
	 * 返回合作商 相关信息
	 * @param userId 用户id
	 * @return
	 */
	private List<Map<String,Object>> getCouriersAndOrdersByPartner(Integer userId){
		
		List<Integer> courierIds = courierOrgServiceImpl.getPartnerUserId(userId);
		if(courierIds==null||courierIds.size()==0)return new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> attendanceList=new ArrayList<Map<String,Object>>();
		StringBuilder queryOrgSql = new StringBuilder();
		queryOrgSql.append(" select ca.user_id,  ca.type ");
		queryOrgSql.append(" from 0085_courier_attendance ca ");
		queryOrgSql.append("  where ");
		queryOrgSql.append(" create_time = UNIX_TIMESTAMP(CURDATE()) ");
		queryOrgSql.append(" and ca.user_id in ( " + StringUtils.join(courierIds, ",") + ")");
		queryOrgSql.append(" ORDER BY user_id, create_time desc ");
		attendanceList = findForJdbc(queryOrgSql.toString());
		
		List<Integer> attendCourierIds = new ArrayList<Integer>();
		
		//构建快递员->是否打卡的映射
				Map<Integer, Boolean> courierIdAttaStatMap = new HashMap<Integer, Boolean>();
				//用户保存上一次迭代在快递员ID
				Integer preCoureierId = null;
				for(Map<String, Object> attCourier : attendanceList){
					Integer cid = Integer.parseInt(attCourier.get("user_id").toString());
					Integer type = Integer.parseInt(attCourier.get("type").toString());
					//判断这条打卡记录是不是当前迭代在快递员最近在打卡记录
					if(preCoureierId == null || !cid.equals(preCoureierId)){
						boolean isOnduty = (type == 0);
						courierIdAttaStatMap.put(cid, isOnduty);
					}
					
					preCoureierId = cid;
				}
				
				//统计所有快递员的打卡记录
				for(Integer id: courierIds){
					//判断courier是否打卡
					if(courierIdAttaStatMap.get(id) != null && courierIdAttaStatMap.get(id).equals(Boolean.TRUE)){
						attendCourierIds.add(id);
					}
				}
				
				if(CollectionUtils.isEmpty(attendCourierIds)){
					return new ArrayList<Map<String,Object>>();
				}
				
				StringBuilder sql = new StringBuilder();
				sql.append(" select u.id as id, u.username as nickname, u.photoUrl, o.id orderId ,o.state ");
				sql.append(" 	,(SELECT org.org_name FROM 0085_org org, 0085_courier_org corg WHERE org.id = corg.org_id AND corg.courier_id = u.id ) orgName ");
				sql.append(" 	,(SELECT cp.position_id FROM 0085_courier_position cp WHERE cp.courier_id = u.id) positionId ");
				sql.append(" from  `user` u LEFT JOIN `order` o on u.id = o.courier_id and o.create_time = UNIX_TIMESTAMP(CURDATE()) ");
				sql.append(" where u.id=? ");
				List<Map<String, Object>> dbUserList = new ArrayList<Map<String, Object>>();
				for(Integer attId : attendCourierIds){
					Map<String, Object> dbUserMap = new HashMap<String, Object>();
					dbUserMap.put("id", attId);
					List<Map<String, Object>> list = this.findForJdbc(sql.toString(), attId);
					logger.info("快递员{}今日订单状态：{}", attId, list);
					int acceptCount = 0;
					for(Map<String, Object> map : list){
						Object state = map.get("state");
						if(state != null && "accept".equals(state.toString())){
							acceptCount++;
						}
						dbUserMap.put("nickname", map.get("nickname"));
						dbUserMap.put("photoUrl", map.get("photoUrl"));
						dbUserMap.put("orgName", map.get("orgName"));
						dbUserMap.put("positionId", map.get("positionId"));
						logger.info("Inside method: getCouriersAndOrdersCount, courierData: nickname - {}, photoUrl - {}, orgName - {}, positionId - {}", map.get("nickname"), map.get("photoUrl"), map.get("orgName"), map.get("positionId"));
					}
					dbUserMap.put("quantity", list.size());//今日订单数
					dbUserMap.put("acceptQuantity", acceptCount);//配送中订单数
					
					//根据快递员查询位置 
					CourierLocationEntity entity = getUserLocation(attId.toString());
					if (entity != null) {
						dbUserMap.put("lat", entity.getLatitude());
						dbUserMap.put("lon", entity.getLongitude());
						dbUserMap.put("addTime", DateUtils.formatTime(entity.getAddtime()*1000l));
					}
					dbUserList.add(dbUserMap);
				}		
		return dbUserList;
	}
	
	@Override
	public List<Map<String, Object>> getCouriersAndOrdersCount(Integer courierId) {
		//获取管理员下所有快递员的id列表
		
		Boolean flag = courierMyInfoService.isAgentUser(courierId);
		
		// 是否是合作商
		if(flag){
			return getCouriersAndOrdersByPartner(courierId);
		}
		
		List<Integer> courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
		
		//  courierOrgServiceImpl.getManageCouriersIdTwo(courierId); // 优化后的 
		
		
		//获取上班的快递员列表
		List<Integer> attendCourierIds = this.getAttendCourierList(courierIds);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select u.id as id, u.username as nickname, u.photoUrl, o.id orderId ,o.state ");
		sql.append(" 	,(SELECT org.org_name FROM 0085_org org, 0085_courier_org corg WHERE org.id = corg.org_id AND corg.courier_id = u.id ) orgName ");
		sql.append(" 	,(SELECT cp.position_id FROM 0085_courier_position cp WHERE cp.courier_id = u.id) positionId ");
		sql.append(" from  `user` u LEFT JOIN `order` o on u.id = o.courier_id and o.create_time = UNIX_TIMESTAMP(CURDATE())");
		sql.append(" where u.id=? ");
		List<Map<String, Object>> dbUserList = new ArrayList<Map<String, Object>>();
		for(Integer attId : attendCourierIds){
			Map<String, Object> dbUserMap = new HashMap<String, Object>();
			dbUserMap.put("id", attId);
			List<Map<String, Object>> list = this.findForJdbc(sql.toString(), attId);
			logger.info("快递员{}今日订单状态：{}", attId, list);
			int acceptCount = 0;
			for(Map<String, Object> map : list){
				Object state = map.get("state");
				if(state != null && "accept".equals(state.toString())){
					acceptCount++;
				}
				dbUserMap.put("nickname", map.get("nickname"));
				dbUserMap.put("photoUrl", map.get("photoUrl"));
				dbUserMap.put("orgName", map.get("orgName"));
				dbUserMap.put("positionId", map.get("positionId"));
				logger.info("Inside method: getCouriersAndOrdersCount, courierData: nickname - {}, photoUrl - {}, orgName - {}, positionId - {}", map.get("nickname"), map.get("photoUrl"), map.get("orgName"), map.get("positionId"));
			}
			dbUserMap.put("quantity", list.size());//今日订单数
			dbUserMap.put("acceptQuantity", acceptCount);//配送中订单数
			
			//根据快递员查询位置 
			CourierLocationEntity entity = getUserLocation(attId.toString());
			if (entity != null) {
				dbUserMap.put("lat", entity.getLatitude());
				dbUserMap.put("lon", entity.getLongitude());
				dbUserMap.put("addTime", DateUtils.formatTime(entity.getAddtime()*1000l));
			}
			dbUserList.add(dbUserMap);
		}
		return dbUserList;
	}

	@Override
	public List<Map<String, Object>> getCouriersAndOrdersCountById(
		Integer courierId, Integer orgId) {
		
		Boolean flag = courierMyInfoService.isAgentUser(courierId);
		
		// 是否是合作商
		if(flag){
			return getCouriersAndOrdersByPartner(courierId);
		}
		
		
		//获取管理员下所有快递员的id列表
		List<Integer> courierIds = courierOrgServiceImpl.getManageCouriersId(courierId, orgId);
		//获取打卡的快递员列表
		List<Map<String, Object>> attendanceList=new ArrayList<Map<String,Object>>();
		List<Integer> attendCourierIds = new ArrayList<Integer>();
		StringBuilder queryOrgSql = new StringBuilder();
		queryOrgSql.append(" select ca.user_id,  ca.type ");
		queryOrgSql.append(" from 0085_courier_attendance ca ");
		queryOrgSql.append("  where ");
		queryOrgSql.append("  create_time = UNIX_TIMESTAMP(CURDATE()) ");
		queryOrgSql.append(" and ca.user_id in ( " + StringUtils.join(courierIds, ",") + ")");
		queryOrgSql.append(" ORDER BY user_id, create_time desc ");
		attendanceList = findForJdbc(queryOrgSql.toString());
	
		//构建快递员->是否打卡的映射
		Map<Integer, Boolean> courierIdAttaStatMap = new HashMap<Integer, Boolean>();
		//用户保存上一次迭代在快递员ID
		Integer preCoureierId = null;
		for(Map<String, Object> attCourier : attendanceList){
			Integer cid = Integer.parseInt(attCourier.get("user_id").toString());
			Integer type = Integer.parseInt(attCourier.get("type").toString());
			//判断这条打卡记录是不是当前迭代在快递员最近在打卡记录
			if(preCoureierId == null || !cid.equals(preCoureierId)){
				boolean isOnduty = (type == 0);
				courierIdAttaStatMap.put(cid, isOnduty);
			}
			
			preCoureierId = cid;
		}
		
		//统计所有快递员的打卡记录
		for(Integer id: courierIds){
			//判断courier是否打卡
			if(courierIdAttaStatMap.get(id) != null && courierIdAttaStatMap.get(id).equals(Boolean.TRUE)){
				attendCourierIds.add(id);
			}
		}
		if(CollectionUtils.isEmpty(attendCourierIds)){
			return new ArrayList<Map<String,Object>>();
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select u.id as id, u.username as nickname, u.photoUrl, COUNT(o.id) as quantity, ");
		sql.append(" (select count(1) from `order` c1 where c1.courier_id = u.id and c1.state = 'accept'and c1.create_time = UNIX_TIMESTAMP(CURDATE())) as acceptQuantity ");
		sql.append(" 	,(SELECT org.org_name FROM 0085_org org, 0085_courier_org corg WHERE org.id = corg.org_id AND corg.courier_id = u.id ) orgName ");
		sql.append(" 	,(SELECT cp.position_id FROM 0085_courier_position cp WHERE cp.courier_id = u.id)positionId ");
		sql.append(" from `order` o RIGHT JOIN `user` u on o.courier_id = u.id And o.create_time = UNIX_TIMESTAMP(CURDATE()) where u.id in (" + StringUtils.join(attendCourierIds, ",") + ")  GROUP BY u.id");
		
		//查询用户列表
		List<Map<String, Object>> dbUserList = this.findForJdbc(sql.toString());
		//根据快递员查询位置
		for(Map<String, Object> dbUser : dbUserList){
			logger.info("Inside method: getCouriersAndOrdersCountById, courierData: nickname - {}, photoUrl - {}, orgName - {}, positionId - {}", dbUser.get("nickname"), dbUser.get("photoUrl"), dbUser.get("orgName"), dbUser.get("positionId"));
			CourierLocationEntity entity = getUserLocation(dbUser.get("id").toString());
			if (entity != null) {
				dbUser.put("lat", entity.getLatitude());
				dbUser.put("lon", entity.getLongitude());
				dbUser.put("addTime", DateUtils.formatTime(entity.getAddtime()*1000l));
			}
		}
		return dbUserList;
	}

	@Override
	public List<Map<String, Object>> getOrdersByKeywords(Integer page, Integer rows,Integer courierId, String keyword) {
		if(page==null){
			page=1;
			rows=10;
		}
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		/*String where  = " select  o.id  from  0085_courier_org co, 0085_org o where co.org_id = o.id and co.courier_id="+ courierId +" and o.status = 1 AND o.`level` = 6\n" +
						" UNION ALL\n" +
						" select o.id from 0085_org o  where   \n" +
						"		o.p_path like (select CONCAT(o.p_path, CONCAT(o.id, '_%'))\n" +
						"											from 0085_org o 	where o.id = (SELECT  o.org_id from 0085_courier_org o  where o.courier_id="+ courierId +") and o.status = 1 and o.`level` not in(6)) and o.status = 1";
		StringBuilder sql = new StringBuilder();	
		sql.append(" select o.user_id as userId,o.id as orderId,(select nickname  from `user`  where id = o.courier_id) as courierName, " );
		sql.append(" (select mobile  from `user`  where id = o.courier_id) as courierPhone ,o.state as state,o.realname as cifName, " );
		sql.append(" o.mobile as cifPhone,o.address as address,o.delivery_time as sendTime,o.order_num as orderNum, " );
		sql.append(" (select m.title from merchant m where m.id = o.merchant_id) as merchantName,o.remark as remark from `order` o  " );
		sql.append(" LEFT JOIN `user` u on o.user_id = u.id where o.courier_id in (" + where + ")  " );
		sql.append(" And CONCAT(IFNULL(u.nickname,''),'|',IFNULL(u.mobile,''),'|',IFNULL(o.order_num,''),'|',IFNULL(o.id,'')) like '%"+ keyword +"%' And date(from_unixtime(o.create_time)) = "+ currentDate +"" );
		优化之后*/
		List<Integer> courierIds = courierOrgServiceImpl.getManageCouriersId(courierId);
		//查询管理员下今日所有的订单数
		StringBuilder queryOrdersSql = new StringBuilder();
		queryOrdersSql.append(" select o.user_id as userId,o.id as orderId,(select nickname  from `user`  where id = o.courier_id) as courierName, " );
		queryOrdersSql.append(" (select mobile  from `user`  where id = o.courier_id) as courierPhone ,o.state as state,o.realname as cifName, " );
		queryOrdersSql.append(" o.mobile as cifPhone,o.address as address,o.delivery_time as sendTime,o.order_num as orderNum, " );
		queryOrdersSql.append(" (select m.title from merchant m where m.id = o.merchant_id) as merchantName,o.remark as remark from `order` o  " );
		queryOrdersSql.append(" LEFT JOIN `user` u on o.user_id = u.id where o.courier_id in (" + StringUtils.join(courierIds, ",") + ")  " );
		queryOrdersSql.append(" And CONCAT(IFNULL(u.nickname,''),'|',IFNULL(u.mobile,''),'|',IFNULL(o.order_num,''),'|',IFNULL(o.id,'')) like ? And o.create_time = UNIX_TIMESTAMP(CURDATE())" );
		
		List<Map<String, Object>> orderList = this.findForJdbcParam(queryOrdersSql.toString(), page, rows, new Object[]{"%"+keyword+"%",currentDate});
		if(CollectionUtils.isNotEmpty(orderList)){
			for(int i=0;i<orderList.size();i++){
				Map<String, Object> map = orderList.get(i);
				Integer userId=((BigInteger)map.get("userId")).intValue();
				//用户等级
				String custType = wuserService.getCustType(userId);
				map.put("custType",custType);
			}
			return orderList;
		}
		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public List<Map<String, Object>> getOrdersByOrgId(Integer page, Integer rows,Integer courierId,
			Integer orgId) {
		if(page==null){
			page=1;
			rows=10;
		}
		
		boolean falg = this.courierMyInfoService.isAgentUser(courierId);
		List<Integer> courierIds = null;
		if(falg){
			courierIds = this.courierOrgServiceImpl.getPartnerUserId(courierId);
		}else{
			courierIds = courierOrgServiceImpl.getManageCouriersId(courierId, orgId);
		}
		
		//查询管理员下今日所有的订单数
		StringBuilder queryOrdersSql = new StringBuilder();
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		queryOrdersSql.append("select o.user_id as userId,o.id as orderId,(select nickname  from `user`  where id = o.courier_id) as courierName,(select mobile  from `user`  where id = o.courier_id) as courierPhone ,o.state as state,o.realname as cifName,o.mobile as cifPhone,o.address as address,o.delivery_time as sendTime,o.order_num as orderNum,(select m.title from merchant m where m.id = o.merchant_id) as merchantName,o.remark as remark from `order` o LEFT JOIN `user` u on o.user_id = u.id where o.courier_id in (" + StringUtils.join(courierIds, ",") + ") And o.pay_time = UNIX_TIMESTAMP(CURDATE()) and o.state !='unpay'" );
		List<Map<String, Object>> userList =  this.findForJdbcParam(queryOrdersSql.toString(),page,rows,currentDate);
		if(CollectionUtils.isNotEmpty(userList)){
			for(int i=0;i<userList.size();i++){
				Map<String, Object> map = userList.get(i);
				Integer userId=((BigInteger)map.get("userId")).intValue();
				//用户等级
				String custType = wuserService.getCustType(userId);
				map.put("custType",custType);
			}
			return userList;
		}
		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public List<Map<String, Object>> getCouriersByOrderId(Integer orderId) {
		//根据订单查询订单机构所在网点的快递员Id列表
		StringBuilder queryOtherCourierIdSql = new StringBuilder();
		queryOtherCourierIdSql.append("select oc.courier_id from `order` o ,0085_merchant_org om,0085_courier_org oc where o.id = ? and oc.courier_id!=0 and o.merchant_id = om.merchant_id and oc.org_id = om.org_id  ");
		List<Map<String, Object>>  courierIdMaps = findForJdbc(queryOtherCourierIdSql.toString(),orderId);
		if(CollectionUtils.isEmpty(courierIdMaps)){
			return new ArrayList<Map<String,Object>>();
		}
		List<Integer> courierIds = new ArrayList<Integer>();
		for(Map<String, Object> courierIdMap:courierIdMaps){
			courierIds.add(Integer.parseInt(courierIdMap.get("courier_id").toString()));
		}
		//根据Id查询所有的快递员信息和订单统计 
		//未完成订单数+用户名
		StringBuilder queryCouriersAndOrdersCountSql = new StringBuilder();
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//当前日期
		queryCouriersAndOrdersCountSql.append("select o.courier_id, u.username  as nickname,SUM(IF(o.state  in('accept','done','confirm','delivery','delivery_done') And date(from_unixtime(o.create_time)) =?,1,0)) as quantity from `order` o,`user` u where  u.id = o.courier_id and courier_id in (" + StringUtils.join(courierIds, ",") + ")   GROUP BY o.courier_id");
		//查询用户列表
		return this.findForJdbc(queryCouriersAndOrdersCountSql.toString(),currentDate);
	}

	@Transactional
	public Integer updateOrderCourierById(Integer courierId, Integer orderId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		if(orderId == null){
			throw new IllegalArgumentException("orderId=null");
		}
		Map<String, Object> retMap = this.findOneForJdbc("select o.courier_id as oldCourierId from `order` o where o.id=?", orderId);
		Integer row = this.executeSql("update `order` o set o.courier_id =? where o.id = ?", courierId,orderId);
		
		Long oldCourierId = (Long) retMap.get("oldCourierId");
		//发送语音消息给两位快递员
		
		if (oldCourierId != null && oldCourierId != 0) {
			Map<String, String> pushMap = new HashMap<String, String>();
			pushMap.put("orderId", oldCourierId.toString());
			String title = "您的一条订单被管理员指派给其他快递员";
			pushMap.put("title", title);
			pushMap.put("content", title);
			pushMap.put("voiceFile", "");
			jpushService.push(oldCourierId.intValue(), pushMap);
			logger.info("向快递员" + oldCourierId + "推送已抢单消息成功");
		}
		Map<String, String> pushMap = new HashMap<String, String>();
		pushMap.put("orderId", courierId.toString());
		String title = "管理员给您指派一条新的订单";
		pushMap.put("title", title);
		pushMap.put("content", title);
		pushMap.put("voiceFile", "");
		jpushService.push(courierId, pushMap);
		
		logger.info("向快递员" + courierId + "推送指派订单消息成功");
		return row;
	}
	
	/**
	 * 获取快递员坐标
	 * 
	 * @param userId
	 * @return
	 */
	public CourierLocationEntity getUserLocation(String userId) {
		String locJson = AliOcs.get("CourierLocation_" + userId);
		if (!StringUtils.isEmpty(locJson)) {
			return JSONObject.parseObject(locJson, CourierLocationEntity.class);
		}
		return null;
	}

	/**(OvO)
	 * 获取管辖范围内上班快递员ID
	 * @param courierIds	管辖范围内的快递员ID
	 * @return	上班的快递员ID List
	 */
	private List<Integer> getAttendCourierList(List<Integer> courierIds){
		/**
		 * 先获取所有上班的快递员列表
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ( ");
		sql.append(" 	SELECT ca.user_id userId, ca.type ");
		sql.append(" 	FROM 0085_courier_attendance ca  ");
		sql.append(" 	WHERE create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" 		AND ca.user_id IN ( ");
		sql.append(StringUtils.join(courierIds, ","));
		sql.append(" 	) ");
		sql.append(" 	ORDER BY create_time DESC  ");
		sql.append(" ) ca GROUP BY ca.userId  HAVING type = 0 ");
		List<Map<String, Object>> resultList = this.findForJdbc(sql.toString());
		
		/**
		 * 在列表中提取快递员ID
		 */
		List<Integer> result = new ArrayList<Integer>();
		for(Map<String, Object> item: resultList){
			Integer courierId = Integer.valueOf(item.get("userId").toString());
			result.add(courierId);
		}
		return result;
	}
}
