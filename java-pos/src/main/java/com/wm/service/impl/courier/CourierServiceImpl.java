package com.wm.service.impl.courier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.courier_mana.common.Constants;
import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.controller.takeout.vo.CourierPositionVo;
import com.wm.dao.courier.CourierDao;
import com.wm.dao.org.OrgDao;
import com.wm.entity.courier.CourierLocationEntity;
import com.wm.entity.courier.ScrambleLogEntity;
import com.wm.entity.courierinfo.CourierInfoEntity;
import com.wm.entity.couriermerchant.CourierMerchantEntity;
import com.wm.entity.courierposition.CourierPositionEntity;
import com.wm.entity.courierpositionlog.CourierPositionLogEntity;
import com.wm.entity.couriersalarylog.CourierSalaryLogEntity;
import com.wm.entity.order.OrderDesignateLogEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.baidu.BaiduMapServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.courier.ScrambleLogServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.courierposition.CourierPositionServiceI;
import com.wm.service.courierpositionlog.CourierPositionLogServiceI;
import com.wm.service.couriersalarylog.CourierSalaryLogServiceI;
import com.wm.service.jpush.JPushLogServiceI;
import com.wm.service.order.OrderDesignateLogServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.IDistributedLock;
import com.wm.util.MyJedisPool;
import com.wm.util.RedisDistributedLock;
import com.wm.util.RedisKeyUtil;
import com.wm.util.StringUtil;

@Service
@Transactional
@Configuration
public class CourierServiceImpl extends CommonServiceImpl implements CourierServiceI{
	
	private static final Logger logger = LoggerFactory.getLogger(CourierServiceImpl.class);

	@Autowired
	private CourierDao courierDao;

	@Autowired
	private OrgDao orgDao;
	
	@Autowired
	private OrgServiceI orgService;

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private WUserServiceI userService;
	
	@Autowired
	private OrderDesignateLogServiceI designateLogService;
	
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	@Autowired
	private CourierPositionServiceI courierPositionService;
	
	@Autowired
	private CourierSalaryLogServiceI CourierSalaryLogService;
	
	@Autowired
	private CourierPositionLogServiceI courierPositionLogService;
	
	@Autowired 
	private JPushLogServiceI jPushLogService;
	
	@Autowired 
	private ScrambleLogServiceI scrambleLogService;
	
	@Autowired
	private BaiduMapServiceI baiduMapService;
	
	@Value("${redis_server}")
	private String redisHost;
	
	@Value("${redis_password}")
	private String password;
	
	@Override
	public List<Map<String, Object>> queryDeliveryScopeList(int courierId, int page, int rows){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT  cb.building_id, ")
				.append(" cb.courier_id, ")
				.append(" b.name, ")
				.append(" b.first_floor, ")
				.append(" b.last_floor, ")
				.append(" b.address ")
			.append(" FROM ")
				.append(" 0085_building AS b, ")
				.append(" 0085_courier_building AS cb ")
			.append(" WHERE b.id = cb.building_id ")
				.append(" AND cb.courier_id = ? ");
		return this.findForJdbcParam(sbsql.toString() , page, rows, courierId);
	}

	@Override
	public List<Map<String, Object>> queryBindMerchantList(int courierId, int page, int rows){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT  mer.title, ")
				.append(" mer.address, ")
				.append(" mer.mobile, ")
				.append(" cm.merchant_id, ")
				.append(" cm.courier_id ")
			.append(" FROM ")
				.append(" 0085_courier_merchant AS cm, ")
				.append(" merchant AS mer ")
			.append(" WHERE mer.id = cm.merchant_id ")
				.append(" AND cm.courier_id = ? ");
		return this.findForJdbcParam(sbsql.toString() , page, rows, courierId);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void saveUserLocation(Integer userId, Double longitude, Double latitude) {
		CourierLocationEntity loc = new CourierLocationEntity(userId, longitude, latitude);
		this.save(loc);
		String locJson = JSONObject.toJSONString(loc);
		logger.debug("saveUserLocation:{}", locJson);
		Jedis jedis = null;
		IDistributedLock lock = null;
		String uuid = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			Integer userType = courierInfoService.getCourierTypeByUserId(userId);
			//众包快递员才上传地理位置
			if(userType != null && userType == Constants.CROWDSOURCING_COURIER){
				
				if(jedis == null){
					logger.error("无法连接redis服务器");
					return;
				}
				
				Map<String, String> addressDetail = baiduMapService.getAddressDetail(longitude, latitude);
				//保存快递员ID到ZSet
				String cityCode = addressDetail.get("cityCode");
				//根据经纬度无法确定其所在的城市
				if(!StringUtils.equals(cityCode, "0")){
					//获取锁
					lock = new RedisDistributedLock(jedis);
					uuid = lock.tryAcquireLock(Constants.LOCK_COURIER_POS, 10, 20);
					
					Map<String, String> position = new HashMap<String, String>();
					position.put("lng", longitude.toString());
					position.put("lat", latitude.toString());
					position.put("cityCode", cityCode);
					
					Pipeline pipeline = jedis.pipelined();
					String courierIdKey = RedisKeyUtil.courierIdKeyByCity(cityCode);
					pipeline.zadd(courierIdKey, System.currentTimeMillis()*-1, userId.toString());
					//保存快递位置到Map
					String poisitionKey = RedisKeyUtil.courierPositionKey(userId);
					pipeline.hmset(poisitionKey, position);
					pipeline.expire(poisitionKey, 24*60*60);
					pipeline.sync();
				}
				else{
					logger.info("userId:{}无法根据上传的经纬度:{},{}确定所在城市", new Object[]{userId, longitude, latitude});
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(lock != null && uuid != null){
					lock.releaseLock(Constants.LOCK_COURIER_POS, uuid);
				}
				JedisPool pool = MyJedisPool.getInstance().getPool();
				if(pool != null){
					pool.returnResource(jedis);
				}
			} 
			catch (Exception e) {
			}
		}
		AliOcs.set("CourierLocation_"+ userId, locJson, 24*60*60);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void cleanUserLocation(Integer userId) {

		logger.info("cleanUserLocation:" +userId );
		Jedis jedis = null;
		IDistributedLock lock = null;
		String uuid = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return;
			}
			String poisitionKey = RedisKeyUtil.courierPositionKey(userId);
			Map<String, String> postion = jedis.hgetAll(poisitionKey);
			if(postion != null){
				String cityCode = postion.get("cityCode");
				//获取锁
				lock = new RedisDistributedLock(jedis);
				uuid = lock.tryAcquireLock(Constants.LOCK_COURIER_POS, 10, 20);
				Pipeline pipeline = jedis.pipelined();
				if (pipeline.del(poisitionKey).get()==1) {
					logger.info("用户位置清除成功！");
				}else {
					logger.info("找不到用户位置！");
				} 
				String courierIdKey = RedisKeyUtil.courierIdKeyByCity(cityCode);
				pipeline.zrem(courierIdKey, userId.toString());
				pipeline.sync();
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(lock != null && uuid != null){
					lock.releaseLock(Constants.LOCK_COURIER_POS, uuid);
				}
				JedisPool pool = MyJedisPool.getInstance().getPool();
				if(pool != null){
					pool.returnResource(jedis);
				}
			} 
			catch (Exception e) {
			}
		}
	}
	
	@Override
	public CourierLocationEntity getUserLocation(Integer userId) {
		String locJson = AliOcs.get("CourierLocation_"+ userId);
		logger.info("getUserLocation:" + locJson);
		if(StringUtils.isNotEmpty(locJson)){
			return JSONObject.parseObject(locJson, CourierLocationEntity.class);
		}
		return null;
	}
	
	@Override
	public List<CourierOrgVo> queryOrgByUserId(Integer courierId){
		StringBuffer sbsql = queryOrgByUserIdSqlStatement();
		List<CourierOrgVo> orvList = this.findObjForJdbc(sbsql.toString(), CourierOrgVo.class, courierId);
		return orvList;
	}

	private StringBuffer queryOrgByUserIdSqlStatement() {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select o.id as orgId,o.org_name as orgName, ")
				.append(" o.pid as orgPid,o.status as orgStatus,o.area_code as orgAreaCode,o.level as orgLevel, ")
                .append(" p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder,cg.id as groupId, ")
				.append(" cg.group_name as groupName, cg.group_desc as groupDesc, ")
				.append(" u.id as courierId, u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM ")
				.append(" 0085_org as o, ")
				.append(" 0085_courier_org as co, ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_courier_group_member as cgm, ")
				.append(" user as u ")
				.append(" WHERE ")
				.append(" co.courier_id = ?")
				.append(" and co.org_id = o.id ")
				.append(" and cp.courier_id = co.courier_id ")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = co.courier_id ")
				.append(" and cgm.group_id = cg.id ")
				.append(" and cgm.user_id = u.id");
		return sbsql;
	}
	

	private StringBuffer querySubCourierSqlStatement() {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select u.id, u.username ")
				.append(" FROM ")
				.append(" 0085_org as o, ")
				.append(" 0085_courier_org as co, ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_courier_group_member as cgm, ")
				.append(" user as u ")
				.append(" WHERE ")
				.append(" co.courier_id = ?")
				.append(" and co.org_id = o.id ")
				.append(" and cp.courier_id = co.courier_id ")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = co.courier_id ")
				.append(" and cgm.group_id = cg.id ")
				.append(" and cgm.user_id = u.id");
		return sbsql;
	}
	
	public List<Map<String, Object>> getCourierOrg(Integer courierId){
		String sql = "select o.id,o.org_name as orgName,o.pid,o.status,o.area_code as areaCode,o.level,o.sort from 0085_org o ";
		sql += " left join 0085_courier_org co on co.org_id=o.id ";
		sql += " where co.courier_id=? ";
		return this.findForJdbc(sql, new Object[]{courierId});
	}
	
	@Override
	public CourierOrgVo getOrgByUserId(int userId){
		StringBuffer sbsql = new StringBuffer();
		CourierOrgVo courierOrgVo = null;
		sbsql.append("select o.id as orgId,o.org_name as orgName, ")
			.append(" o.pid as orgPid,o.status as orgStatus,o.area_code as orgAreaCode,o.level as orgLevel, ")
            .append(" p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder,cg.id as groupId, ")
			.append(" cg.group_name as groupName, cg.group_desc as groupDesc, ")
			.append(" u.id as courierId, u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
			.append(" FROM ")
			.append(" 0085_org as o, ")
			.append(" 0085_courier_org as co, ")
			.append(" 0085_position as p, ")
			.append(" 0085_courier_position as cp, ")
			.append(" 0085_courier_group as cg, ")
			.append(" 0085_courier_group_member as cgm, ")
			.append(" user as u ")
			.append(" WHERE ")
			.append(" co.courier_id = ?")
			.append(" and co.org_id = o.id ")
			.append(" and cp.courier_id = co.courier_id ")
			.append(" and cp.position_id = p.id ")
			.append(" and cgm.user_id = co.courier_id ")
			.append(" and cgm.group_id = cg.id ")
			.append(" and cgm.user_id = u.id");
		List<CourierOrgVo> orvList = this.findObjForJdbc(sbsql.toString(), CourierOrgVo.class, userId);
		if(orvList != null && orvList.size() > 0){
			 courierOrgVo = orvList.get(0);
		}
		return courierOrgVo;
	}
	
	
	@Override
	public List<CourierOrgVo> queryOrgByOrgName(String orgName,int page,int rows){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select o.id as orgId,o.org_name as orgName, ")
				.append(" o.pid as orgPid,o.status as orgStatus,o.area_code as orgAreaCode,o.level as orgLevel, ")
                .append(" p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder,cg.id as groupId, ")
				.append(" cg.group_name as groupName, cg.group_desc as groupDesc, ")
				.append(" u.id as courierId, u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM ")
				.append(" 0085_org as o, ")
				.append(" 0085_courier_org as co, ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_courier_group_member as cgm, ")
				.append(" user as u ")
				.append(" WHERE ")
				.append(" o.org_name = ? ")
				.append(" and co.org_id = o.id ")
				.append(" and cp.courier_id = co.courier_id ")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = co.courier_id ")
				.append(" and cgm.group_id = cg.id ")
				.append(" and cgm.user_id = u.id");
		List<CourierOrgVo> orvList = this.findObjForJdbc(sbsql.toString(),page,rows, CourierOrgVo.class, orgName);
		return orvList;
	}
	
	@Override
	public List<CourierOrgVo> queryOrgByOrgId(int orgId){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select o.id as orgId,o.org_name as orgName, ")
				.append(" o.pid as orgPid,o.status as orgStatus,o.area_code as orgAreaCode,o.level as orgLevel, ")
                .append(" p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder,cg.id as groupId, ")
				.append(" cg.group_name as groupName, cg.group_desc as groupDesc, ")
				.append(" u.id as courierId, u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM ")
				.append(" 0085_org as o, ")
				.append(" 0085_courier_org as co, ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_courier_group_member as cgm, ")
				.append(" user as u ")
				.append(" WHERE ")
				.append(" o.id = ? ")
				.append(" and co.org_id = o.id ")
				.append(" and cp.courier_id = co.courier_id ")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = co.courier_id ")
				.append(" and cgm.group_id = cg.id ")
				.append(" and cgm.user_id = u.id");
		List<CourierOrgVo> orvList = this.findObjForJdbc(sbsql.toString(), CourierOrgVo.class, orgId);
		return orvList;
	}
	
	
	@Override
	public List<CourierPositionVo> queryPositionByUserId(int userId,int page,int rows){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select cp.courier_id as courierId,p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder,cg.id as groupId, ")
				.append(" cg.group_name as groupName, cg.group_desc as groupDesc ")
				.append(" FROM ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_courier_group_member as cgm ")
				.append(" WHERE ")
				.append(" cp.courier_id = ?")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = cp.courier_id ")
				.append(" and cgm.group_id = cg.id ");
		List<CourierPositionVo> orvList = this.findObjForJdbc(sbsql.toString(),page,rows,CourierPositionVo.class, userId);
		return orvList;
	}
	
	@Override
	public List<CourierOrgVo> queryCouriersByGroupId(int groupId,int page,int rows){
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT ")
				.append(" o.id as orgId,o.org_name as orgName, ")
				.append(" o.pid as orgPid,o.status as orgStatus,o.area_code as orgAreaCode,o.level as orgLevel, ")
                .append(" p.id as positionId,p.name as positionName, p.sort_order as positionSortOrder, ")
				.append(" cg.id as groupId, cg.group_name as groupName, cg.group_desc as groupDesc, ")
				.append(" u.id as courierId, u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM ")
				.append(" 0085_courier_group_member as cgm, ")
				.append(" 0085_courier_group as cg, ")
				.append(" 0085_org as o, ")
				.append(" 0085_courier_org as co, ")
				.append(" 0085_position as p, ")
				.append(" 0085_courier_position as cp, ")
				.append(" user as u ")
				.append(" WHERE ")
				.append(" cgm.group_id = ? ")
				.append(" and cgm.group_id = cg.id ")
				.append(" and cgm.user_id = cp.courier_id ")
				.append(" and cp.position_id = p.id ")
				.append(" and cgm.user_id = co.courier_id ")
				.append(" and co.org_id = o.id ")
				.append(" and cgm.user_id = u.id ");
		List<CourierOrgVo> orvList = this.findObjForJdbc(sbsql.toString(),page,rows,CourierOrgVo.class, groupId);
		return orvList;
	}

	@Override
	public Integer getCourierLevelBySalaryPosition(Integer salary, Integer positionId) {
		String sql = "select salary from 0085_position_grade_rule where position_id=? order by salary ";
		List<Map<String, Object>> list = this.findForJdbc(sql, positionId);
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				if(salary.equals(list.get(i).get("salary"))){
					return i+1;
				}
			}
		}
		return 1;
	}

	@Override
	public List<Map<String, Object>> queryCourierSubCourierByCourierId(
			Integer courierId, Integer orderId, Integer page, Integer rows) {
		//先查询快递员所在的片区
		List<Map<String, Object>> courierOrderList = new ArrayList<Map<String, Object>>();

		List<CourierOrgVo> queryOrgByUserIdList = this.queryOrgByUserId(courierId);
		// 是否是见习员工，业务员，物流组长
		boolean isDownWuliuzuzhang = false;
		Integer orgId = null;
		if (queryOrgByUserIdList != null && queryOrgByUserIdList.size() > 0) {
			CourierOrgVo cov = queryOrgByUserIdList.get(0);
			if (cov.getPositionName().equalsIgnoreCase("物流组长")
					|| cov.getPositionName().equalsIgnoreCase("业务员")
					|| cov.getPositionName().equalsIgnoreCase("见习员工")) {
				isDownWuliuzuzhang = true;
				if(isDownWuliuzuzhang){
					return this.findForJdbcParam(querySubCourierSqlStatement().toString(), page, rows, courierId);
				}
			}else{
				// 片区经理以上
				orgId = cov.getOrgId();
			}
		}
		if(orgId == null){
			//查询快递员对应的orgId
			Map<String, Object> objCo = this.findOneForJdbc("select org_id from 0085_courier_org where courier_id = ? " , courierId);
			if(objCo != null && objCo.size() > 0){
				Object orgid = objCo.get("org_id");
				orgId = Integer.parseInt(String.valueOf(orgid));
			}
		}
		if(orgId != null){
			// 片区经理以上
			StringBuffer sbsql = new StringBuffer();
			//查此地区以下的，所以不包含自己的orgid
			List<String> recursionQueryOrgIdList = orgDao
					.recursionQueryOrgIdList(orgId, false);
			String replaceMe = "__20150903__";
			String list2SqlInList = StringUtil.list2SqlIn(recursionQueryOrgIdList);
			sbsql.append(" SELECT co.courier_id, u.username FROM ")
				.append(" 0085_courier_org as co, `user` as u ")
				.append(" WHERE  co.courier_id = u.id and (co.org_id in ")
				.append(replaceMe)
				.append(" or u.id = ? ) ");
			String stringSql = sbsql.toString();
			stringSql = stringSql.replaceAll(replaceMe, list2SqlInList);
			if (page == null || rows == null || page == 0 || rows == 0) {
				courierOrderList = this.findForJdbc(stringSql, courierId);
			} else {
				courierOrderList = this.findForJdbcParam(stringSql, page, rows, courierId);
			}
		}
		return courierOrderList;
	}

	@Override
	public Map<String, Object> designateOrder(Integer designateId, Integer designeeId,
			Integer orderId) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("success", false);
		OrderEntity order = this.get(OrderEntity.class, orderId);
		if(order == null){
			result.put("msg", "无订单:" + orderId);
			return result;
		}
		
		//添加验证
		//1，是否指派者和订单中的店铺有关系
		String sql1 = "select m.courier_id from 0085_courier_merchant m ";
		sql1 += " left join `order` o on o.merchant_id = m.merchant_id ";
		sql1 += " where o.id = ? AND m.courier_id = ? ";
		List<Map<String, Object>> listDesignate = this.findForJdbc(sql1, new Object[]{orderId, designateId});
		if(listDesignate == null || listDesignate.isEmpty()){
				result.put("msg", "快递管理员:" + getCourierUserName(designateId) + "没有指派此商铺：" + order.getMerchant().getTitle() + "的权力 =");
			return result;
		}
		//验证1完成
		
		//2，是否快递员和订单中的店铺有关系
		List<Map<String, Object>> courierList = orderService.getRelaCourier(orderId);
		if(courierList == null || courierList.isEmpty()){
			result.put("msg", "快递员:" + getCourierUserName(designeeId) + "没有绑定商铺：" + order.getMerchant().getTitle());
			return result;
		}
		boolean existCourier = false;
		for (Map<String, Object> cmap : courierList) {
			Object cid = cmap.get("courier_id");
			if(cid.equals(designeeId)){
				existCourier = true;
				break;
			}
		}
		if(!existCourier){
			result.put("msg", "快递员" + designeeId + "没有绑定商铺：" + order.getMerchant().getTitle());
			return result;
		}
		//验证完毕2
		orderService.scrambleOrder(designeeId, orderId);
		result.put("msg", "保存指派订单成功");
		
		//保存指派数据到指派日志表
		OrderDesignateLogEntity odle = new OrderDesignateLogEntity();
		odle.setCreateTime(DateUtils.getSeconds());
		odle.setDesignateId(designateId);
		odle.setDesigneeId(designeeId);
		odle.setOrderId(orderId);
		designateLogService.save(odle);

		result.put("msg", "保存指派订单和指派日志成功");
		result.put("success", true);
		return result;
	}

	private String getCourierUserName(Integer designateId) {
		Map<String, Object> usernameMap = this.findOneForJdbc("select username from `user` where id = ? ", designateId);
		String un = designateId+"";
		if(usernameMap != null && usernameMap.size() > 0){
			un = String.valueOf(usernameMap.get("username"));
		}
		return un;
	}
	
	public void saveAdjustPosition(int courierId ,int oldSalary,int newSalary, CourierInfoEntity courierInfo ,int oldPositionId, int newPositionId, CourierPositionEntity courierPosition){
		if(oldSalary!=newSalary){
			courierInfo.setSalary(newSalary);
			courierInfoService.save(courierInfo);
			CourierSalaryLogEntity courierSalaryLog = new CourierSalaryLogEntity();
			courierSalaryLog.setCourierId(courierId);
			courierSalaryLog.setOldSalary(oldSalary);
			courierSalaryLog.setNewSalary(newSalary);
			courierSalaryLog.setChangeTime(DateUtils.getSeconds());
			courierSalaryLog.setChangeOperator(0);
			CourierSalaryLogService.save(courierSalaryLog);	
		}
		if(oldPositionId!=newPositionId){
			courierPosition.setPositionId(newPositionId);
			courierPositionService.save(courierPosition);
			CourierPositionLogEntity courierPositionLog = new CourierPositionLogEntity();
			courierPositionLog.setCourierId(courierId);
			courierPositionLog.setOldPositionId(oldPositionId);
			courierPositionLog.setNewPositionId(newPositionId);
			courierPositionLog.setChangeTime(DateUtils.getSeconds());
			courierPositionLog.setChangeOperator(0);
			courierPositionLogService.save(courierPositionLog);
		}
	}

	public List<Map<String, Object>> queryNotScrambleOrderByOrgId(
			Integer orgId, Integer courierId, Integer page, Integer rows) {

		List<Map<String, Object>> orderListResult = Lists.newArrayList();
		
		List<String> recursionQueryOrgIdList = orgDao.recursionQueryOrgIdList(orgId, false);
		
		if(recursionQueryOrgIdList != null && recursionQueryOrgIdList.size() > 0){
			String list2SqlIn = StringUtil.listQuota2SqlIn(recursionQueryOrgIdList);

			StringBuffer sbsql = new StringBuffer();
				sbsql.append(" SELECT o.courier_id , CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num, ") ;
				sbsql.append(" o.time_remark as timeRemark, o.pay_id, ");
				sbsql.append(" o.id,o.state,o.pay_state,o.order_type,o.mobile,o.realname,o.address,merchant.logo_url,o.user_id, ");
				sbsql.append(" o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time, ");
				sbsql.append(" merchant.title, o.remark, ");
				sbsql.append(" case when o.origin<500 then 1 else 2 end is_priority ");
				sbsql.append(" FROM  `order` AS o , ");
				sbsql.append(" 0085_merchant_org as mer_org, merchant as merchant ");
				sbsql.append(" WHERE ( o.order_type = 'normal' OR o.order_type = 'mobile' ) ");
				sbsql.append(" AND o.courier_id = 0	AND o.state = 'accept'	AND DATE(FROM_UNIXTIME(o.create_time)) = curdate()");
				sbsql.append(" AND o.merchant_id IN ( SELECT merchant_id	FROM 0085_courier_merchant	WHERE courier_id = ? ) ");
				sbsql.append(" AND o.merchant_id = mer_org.merchant_id AND merchant.id = o.merchant_id  ");
				sbsql.append(" AND mer_org.org_id in (select id from 0085_org where FIND_IN_SET(pid	, ").append(list2SqlIn).append(" ) AND `LEVEL` = 6 ) ");
				sbsql.append(" ORDER BY o.urgent_time DESC, o.create_time ASC ");
				

				List<Map<String, Object>> orderList = this.findForJdbcParam(sbsql.toString(), page, rows, courierId);
				String sql = "SELECT m.name,o_m.price,o_m.quantity,o.time_remark as timeRemark,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num FROM "
						+ " `order` o LEFT JOIN "
						+ " merchant merchant ON merchant.id=o.merchant_id "
						+ " RIGHT JOIN order_menu o_m ON o_m.order_id=o.id LEFT JOIN menu m "
						+ " ON m.id=o_m.menu_id WHERE o.id=?";

				for (Map<String, Object> m : orderList) {
					String custType = "新"; // 用户类型
					Integer userIdTmp = Integer.parseInt(m.get("user_id").toString());

					custType = userService.getCustType(userIdTmp);
					m.put("custType", custType);

					List<Map<String, Object>> courierOrderDetail = this.findForJdbc(
							sql, new Object[] { m.get("id") });
					m.put("courierOrderDetail", courierOrderDetail);
					orderListResult.add(m);
				}

			return orderListResult;
		}
		return null;
	}
	

	@Override
	public List<Map<String, Object>> queryCourierReminderByOrgId(
			Integer orgId, Integer userId, Integer page, Integer rows) {
		return courierDao.queryCourierReminderListByOrgId(orgId, userId, page, rows);
	}
	

	/**
	 * 
	 * @param orgId
	 * @param courierId
	 * @param keyword 关键字查询，可以搜索的字段为：排号， 电话，订单号，姓名
	 * @param state 状态为空或空串或search，则查所有，否则填 scramble 抢单 , reminder 催单, 
	 * 			30minute 距离送餐时间还有30分钟的订单, 20minute 距离送餐时间还有20分钟的订单, 10minute 距离送餐时间还有10分钟的订单
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> searchCourierOrder(
			Integer orgId, Integer courierId, 
			String keyword, String state,
			Integer page, Integer rows) {

		//催单
		if(StringUtils.equals(state, "reminder")){
			return this.queryCourierReminderByOrgId(orgId, courierId, page, rows);
		}
		
		List<Map<String, Object>> orderListResult = Lists.newArrayList();
		
		List<String> recursionQueryOrgIdList = orgDao.recursionQueryOrgIdList(orgId, false);
		
		if(recursionQueryOrgIdList != null && recursionQueryOrgIdList.size() > 0){
			String list2SqlIn = StringUtil.listQuota2SqlIn(recursionQueryOrgIdList);

			StringBuffer sbsql = searchOrderMonitorSql(keyword, state, list2SqlIn, false);
				
			List<Map<String, Object>> orderList = this.findForJdbcParam(sbsql.toString(), page, rows, courierId);
			String sql = "SELECT m.name,o_m.price,o_m.quantity,o.time_remark as timeRemark,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num FROM "
					+ " `order` o LEFT JOIN "
					+ " merchant merchant ON merchant.id=o.merchant_id "
					+ " RIGHT JOIN order_menu o_m ON o_m.order_id=o.id LEFT JOIN menu m "
					+ " ON m.id=o_m.menu_id WHERE o.id=?";

			for (Map<String, Object> m : orderList) {
				String custType = "新"; // 用户类型
				Integer userIdTmp = Integer.parseInt(m.get("user_id").toString());

				custType = userService.getCustType(userIdTmp);
				m.put("custType", custType);

				List<Map<String, Object>> courierOrderDetail = this.findForJdbc(
						sql, new Object[] { m.get("id") });
				m.put("courierOrderDetail", courierOrderDetail);
				int total_num = 0 ;
				double total_price = 0.0;
				if(courierOrderDetail != null && courierOrderDetail.size() > 0){
					for (Map<String, Object> mapItem : courierOrderDetail) {
						total_num += Integer.parseInt(mapItem.get("quantity").toString());
						total_price += Double.parseDouble(mapItem.get("price").toString());
					}
				}
				m.put("total_num", total_num);
				m.put("total_price", total_price);
				
				orderListResult.add(m);
			}

			return orderListResult;
		}
		return null;
	}

	//查询订单监控的sql
	private StringBuffer searchOrderMonitorSql(String keyword, String state, String list2SqlIn, boolean countFlag) {
		StringBuffer sbsql = new StringBuffer();
		if(countFlag){
			sbsql.append(" SELECT count(distinct o.id) as count ");
		}else{
			//查询出所需要的字段
			sbsql.append(" SELECT case when o.courier_id > 0 then 1 else 0 end as scramble, ");//是否可抢单
			sbsql.append(" case when o.courier_id > 0 then (select username from `user` where id = o.courier_id) else '' end as courier_name, ");//如果已分了快递员，则返回快递员名字
			sbsql.append(" o.courier_id , CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num, ") ;
			sbsql.append(" o.time_remark as timeRemark, o.pay_id, ");
			sbsql.append(" o.id,o.state,o.pay_state,o.order_type,o.mobile,o.realname,o.address,merchant.logo_url,o.user_id, ");
			sbsql.append(" o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time, ");
			sbsql.append(" merchant.title, o.remark, ");
			sbsql.append(" case when o.origin<500 then 1 else 2 end is_priority ");
		}
		//从订单和用户相关的表查
		sbsql.append(" FROM  `order` AS o , ");
		sbsql.append(" 0085_merchant_org as mer_org, merchant as merchant ");
		sbsql.append(" WHERE 1=1 AND ( o.order_type = 'normal' OR o.order_type = 'mobile' ) ");
		sbsql.append(" AND DATE(FROM_UNIXTIME(o.create_time)) = curdate()");
		sbsql.append(" AND o.merchant_id IN ( SELECT merchant_id FROM 0085_courier_merchant	WHERE courier_id = ? ) ");
		sbsql.append(" AND o.merchant_id = mer_org.merchant_id AND merchant.id = o.merchant_id  ");
		sbsql.append(" AND mer_org.org_id in (select id from 0085_org where FIND_IN_SET(pid	, ").append(list2SqlIn).append(" ) AND `LEVEL` = 6 ) ");
		if(StringUtils.isBlank(state)){
			//添加搜索的字段为：排号， 电话，订单号，姓名
			if(StringUtils.isNotEmpty(keyword)){
				sbsql.append( " AND (o.realname like '%" + keyword + "%' " );
				sbsql.append( " or o.pay_id like '%" + keyword + "%' ");
				sbsql.append( " or o.mobile like '%" + keyword + "%' ");
				sbsql.append( " or o.order_num like '%" + keyword + "%' ) ");
			}
		}else{
			//添加状态， scramble 抢单 , reminder 催单, XXminute
			if(StringUtils.equals(state, "scramble")){
				//可抢单的为：快递员id为0，并且状态为商家已经接单
				sbsql.append(" AND o.courier_id = 0	AND o.state = 'accept'	" );
			} else if(StringUtils.equals(state, "30minute")){
				//30分钟,获取当前时间的20到30分钟内
				Date date = new Date();
				Date dateBegin = org.apache.commons.lang3.time.DateUtils.addMinutes(date, 20);
				Date dateEnd = org.apache.commons.lang3.time.DateUtils.addMinutes(date, 30);
				String strShortHourStart = DateUtils.formatShortTime(dateBegin);
				String strShortHourEnd = DateUtils.formatShortTime(dateEnd);
				//取送餐时间的前面的时间做预警，如11:30-12:00，则取11:30
				sbsql.append(" AND SUBSTRING(o.time_remark, 1, 5) BETWEEN '")
					.append(strShortHourStart).append("' AND '").append( strShortHourEnd ).append("' ");
			} else if(StringUtils.equals(state, "20minute")){
				//20分钟,获取当前时间的10到20分钟内
				Date date = new Date();
				Date dateBegin = org.apache.commons.lang3.time.DateUtils.addMinutes(date, 10);
				Date dateEnd = org.apache.commons.lang3.time.DateUtils.addMinutes(date, 20);
				String strShortHourStart = DateUtils.formatShortTime(dateBegin);
				String strShortHourEnd = DateUtils.formatShortTime(dateEnd);
				//取送餐时间的前面的时间做预警，如11:30-12:00，则取11:30
				sbsql.append(" AND SUBSTRING(o.time_remark, 1, 5) BETWEEN '")
				.append(strShortHourStart).append("' AND '").append( strShortHourEnd ).append("' ");
			} else if(StringUtils.equals(state, "10minute")){
				//10分钟,获取当前时间的0到10分钟内
				Date date = new Date();
				Date dateBegin = date;
				Date dateEnd = org.apache.commons.lang3.time.DateUtils.addMinutes(date, 10);
				String strShortHourStart = DateUtils.formatShortTime(dateBegin);
				String strShortHourEnd = DateUtils.formatShortTime(dateEnd);
				//取送餐时间的前面的时间做预警，如11:30-12:00，则取11:30
				sbsql.append(" AND SUBSTRING(o.time_remark, 1, 5) BETWEEN '")
				.append(strShortHourStart).append("' AND '").append( strShortHourEnd ).append("' ");
			}
		}
		//添加排序，按加急排序, 配送时间，然后按创建时间
		sbsql.append(" ORDER BY o.urgent_time DESC, o.time_remark DESC, o.create_time ASC ");
		return sbsql;
	}

	@Override
	public List<Map<String, Object>> queryFirstChildrenOrgs(Integer orgId,Integer courierId, Integer page,
			Integer rows) {
		return orgDao.queryFirstChildrenOrgs(orgId, courierId, page, rows);
	}


	public List<Map<String, Object>> queryOrgByCourierIdLevel(Integer courierId, Integer level) {
		//查用户对应的区域孩子id列表
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT ");
		sbsql.append(" 	org.org_name, ");
		sbsql.append(" 	org.area_code, ");
		sbsql.append(" 	org.`level` AS org_level, ");
		sbsql.append(" 	org.pid AS org_pid, ");
		sbsql.append(" 	org.id ");
		sbsql.append(" FROM ");
		sbsql.append(" 	0085_org as org, 0085_courier_org as cour_org ");
		sbsql.append(" WHERE 1=1 ");
		sbsql.append(" AND org.`status` = 1 ");// 表示有效
		sbsql.append(" AND org.id = cour_org.org_id ");
		sbsql.append(" AND org.`level` = ? ");// 等级，默认为0=未知，1=国家，2=省，3=市，4=区，5=片区
		sbsql.append(" AND cour_org.courier_id = ? ");
		List<Object> params = Lists.newArrayList();
		params.add(level);
		params.add(courierId);
		sbsql.append(" ORDER BY org.`level`, org.sort  ");
		List<Map<String,Object>> list = this.findForJdbc(sbsql.toString(), params.toArray());
		return list;
	}

	
	
	@Override
	public List<Map<String, Object>> queryCourierManagerFunction(
			Integer courierId) {
		String sql = "select r.id as role_id, f.id as func_id, u.id as user_id, r.rolename "
				+ " , f.function_name from `user` u , role r , role_user ru , role_function rf"
				+ " , `function` f where u.id = ? and u.id = ru.user_id and ru.role_id = rf.role_id "
				+ " and rf.function_id = f.id and r.rolename = '快递员管理员' and f.function_level = 2 ";
		return this.findForJdbc(sql, courierId);
	}

	@Override
	public Map<String, Object> countOrderMonitor(Integer orgId, Integer courierId) {
		// scramble 抢单 , reminder 催单, XXminute
		Map<String, Object> countMap = Maps.newHashMap();
		countMap.put("countScramble", 0);
		countMap.put("count30minute", 0);
		countMap.put("count20minute", 0);
		countMap.put("count10minute", 0);
		countMap.put("countReminder", 0);
		
		List<String> recursionQueryOrgIdList = orgDao.recursionQueryOrgIdList(orgId, false);
		if(recursionQueryOrgIdList != null && recursionQueryOrgIdList.size() > 0){
			
			String list2SqlIn = StringUtil.listQuota2SqlIn(recursionQueryOrgIdList);
			boolean countFlag = true;
			
			//	查未抢单的数
			StringBuffer sbsql = searchOrderMonitorSql(null, "scramble", list2SqlIn, countFlag);
			Map<String, Object> map = this.findOneForJdbc(sbsql.toString(), courierId);
			countMap.put("countScramble", map.get("count"));
			
			//	查XX分钟数
			sbsql = searchOrderMonitorSql(null, "30minute", list2SqlIn, countFlag);
			map = this.findOneForJdbc(sbsql.toString(), courierId);
			countMap.put("count30minute", map.get("count"));

			sbsql = searchOrderMonitorSql(null, "20minute", list2SqlIn, countFlag);
			map = this.findOneForJdbc(sbsql.toString(), courierId);
			countMap.put("count20minute", map.get("count"));

			sbsql = searchOrderMonitorSql(null, "10minute", list2SqlIn, countFlag);
			map = this.findOneForJdbc(sbsql.toString(), courierId);
			countMap.put("count10minute", map.get("count"));

			//	查催单数
			Long countReminder = courierDao.countCourierReminder(orgId, courierId);
			countMap.put("countReminder", countReminder);
		}
		
		return countMap;
	}

	@Override
	public Map<String, Object> registerState(String mobile, String userType) {

		StringBuffer sbsql = new StringBuffer();
		sbsql.append("");
		sbsql.append(" SELECT   ");
		sbsql.append(" FROM_UNIXTIME(ca.create_time),  ");
		sbsql.append(" ca.mobile,     ");
		sbsql.append(" cour_al.audit_result   ");
		sbsql.append(" FROM    ");
		sbsql.append(" 	0085_courier_apply AS ca, ");
		sbsql.append(" 	0085_courier_audit_log AS cour_al ");
		sbsql.append(" WHERE  ");
		sbsql.append(" 	ca.mobile = ? AND ca.user_type = ? ");
		sbsql.append(" AND ca.id = cour_al.appy_id  ");
		
		return this.findOneForJdbc(sbsql.toString(), mobile, userType);
	}

	@Override
	public List<Map<String, Object>> queryOrgMerchantList(int courierId,
			int page, int rows) {
		StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT  mer.id, ")
			.append(" mer.address, ")
			.append(" mer.title, ")
			.append(" mer.mobile ")
		.append(" FROM ")
			.append(" 0085_merchant_org as mer_org, ")
			.append(" merchant AS mer, ")
			.append(" 0085_courier_org AS cour_org ")
		.append(" WHERE 1 = 1 AND mer.id = mer_org.merchant_id ")
			.append(" AND mer_org.org_id = cour_org.org_id AND cour_org.courier_id = ? ");
			
		return this.findForJdbcParam(sbsql.toString(), page, rows, courierId);
	}

	@Override
	public List<Map<String, Object>> queryLogisticsLeaderMemberList(
			int courierId, int page, int rows) {

		int orgId = 0;
		List<CourierOrgVo> clist = this.queryOrgByUserId(courierId);
		if(clist != null && clist.size() > 0){
			CourierOrgVo vo = clist.get(0);
			orgId = vo.getOrgId();
		} else {
			return null;
		}
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT  u.id, u.username ")
				.append(" FROM 0085_org AS o, ")
				.append(" 0085_courier_org AS co, ")
				.append(" `user` AS u ")
				.append(" WHERE 1 = 1 AND co.courier_id = u.id ")
				.append(" AND co.org_id = o.id AND co.org_id = ? ")
		//-- 级别为6表示网点
				.append(" AND o.`level` = 6 ");
		
		return this.findForJdbcParam(sbsql.toString(), page, rows, orgId);
	}

	@Override
	public void batchSaveCourierMerchant(Integer courierId, String merchantIds) {

		List<CourierMerchantEntity> entitys = Lists.newArrayList();
		String[] merIds = StringUtils.split(merchantIds, ",");
		if(merIds != null && merIds.length > 0){
			for (String merId : merIds) {
				if(StringUtils.isBlank(merId)){
					continue;
				}
				CourierMerchantEntity en = new CourierMerchantEntity();
				en.setMerchantId(Integer.parseInt(merId));
				en.setCourierId(courierId);
				entitys.add(en);
			}
		}
		this.batchSave(entitys );
	}
	
	/**
	 * 查找x分钟仍没接单的订单数
	 * @param courierId
	 * @param orgId
	 * @param minus 范围0~59
	 * @return
	 */
	public Integer getUnscrabledOrderNumber(Integer courierId, Integer orgId, Integer minus){
		List<OrgEntity> courierOrgs = this.findCourierOrgs(courierId, orgId);
		if(courierOrgs != null && courierOrgs.size() > 0){
			String timediff = "";
			if(minus != null){
				if(minus < 10){
					timediff += "00:0" + minus + ":00";
				} else {
					if(minus > 60){
						return 0;
					} else {
						timediff += "00:" + minus + ":00";
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("('");
			for (int i = 0; i < courierOrgs.size(); i++) {
				OrgEntity org = courierOrgs.get(i);
				if (i == courierOrgs.size() - 1) {
					sb.append(org.getId());
				} else {
					sb.append(org.getId()).append(",");
				}
			}
			sb.append("')");
			String sql = "select count(distinct po.order_id) from 0085_pushed_order po";
			sql += " left join 0085_courier_org co on co.courier_id=po.pushed_courier ";
			sql += " where FIND_IN_SET(co.org_id,?) ";
			if(StringUtils.isEmpty(timediff)){
				return this.getCountForJdbcParam(sql, new Object[]{sb.toString()}).intValue();
			} else {
				sql += " and timediff(CURTIME(),FROM_UNIXTIME(po.create_time,'%H:%i:%s'))>=? ";
				return this.getCountForJdbcParam(sql, new Object[]{sb.toString(), timediff}).intValue();
			}
		}
		return 0;
	}
	
	public List<OrgEntity> findCourierOrgs(Integer courierId, Integer orgId) {
		List<OrgEntity> childOrgs = new ArrayList<OrgEntity>();
		List<OrgEntity> orgList = orgService.loadAll(OrgEntity.class);
		if(orgId == null){
			// 默认为当前快递员所管辖的所有组织架构节点
			List<Map<String, Object>> courierOrg = this.getCourierOrg(courierId);
			if(courierOrg != null && courierOrg.size() > 0){
				Integer courierOrgId = Integer.parseInt(courierOrg.get(0).get("id").toString());
				childOrgs = orgService.findChildOrgList(orgList, courierOrgId);
			}
		} else {
			// 只查询orgId下面的所有组织架构节点
			childOrgs = orgService.findChildOrgList(orgList, orgId);
		}
		return childOrgs;
	}

	@Override
	public Long countCourierReminder(Integer orgId, Integer userId) {
		return courierDao.countCourierReminder(orgId, userId);
	}

	@Override
	public List<Integer> findCourierByPosition(Integer positionId) {
		List<Integer> leaders= new ArrayList<Integer>();
		String sql = "select cp.courier_id from 0085_courier_position cp "
				+ " left join user u on u.id=cp.courier_id where cp.position_id=? and u.user_type='courier' and is_delete=0 ";
		List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{positionId});
		if(list != null && list.size() > 0){
			for(Map<String, Object> map : list){
				leaders.add(Integer.parseInt(map.get("courier_id").toString()));
			}
		}
		return leaders;
	}
	
	public Integer getCourierLeader(Integer courierId, Integer positionId) {
		String sqlCount = "select count(*) from 0085_courier_position cp where courier_id=? and cp.position_id<?";
		List<Integer> leaderOrgs = orgService.findLeaderOrgs(courierId);
		Long count = this.getCountForJdbcParam(sqlCount, new Object[]{courierId, positionId});
		if(count != null && count > 0){
			String sql = "select co.courier_id,co.org_id from 0085_courier_org co where co.courier_id in "
					+ " (select cp.courier_id from 0085_courier_position cp where cp.position_id=?) ";
			List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{positionId});
			if(list != null && list.size() > 0){
				for(Map<String, Object> map : list){
					Integer orgId = Integer.parseInt(map.get("org_id").toString());
					if(leaderOrgs.contains(orgId)){
						return Integer.parseInt(map.get("courier_id").toString());
					}
				}
			}
		} else {
			logger.warn("未找到当前快递员【" + courierId + "】领导岗位【 + positionId + 】");
		}
		return null;
	}

	@Override
	public void saveScrambleLog(Integer courierId, AjaxJson j) {
		ScrambleLogEntity scrambleLog = new ScrambleLogEntity(courierId, j.getObj(), j.isSuccess(), j.getMsg());
		scrambleLogService.save(scrambleLog);
	}
	
	@Override
	public Integer confirmFollow(Integer ordId, Integer userId, Date followTime) {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO waimai_order_follow");
		query.append(" (");
		query.append("  order_id, user_id, follow_time");
		query.append(" )");
		query.append(" VALUES (");
		query.append(" ?, ?, ? )");		
		return executeSql(query.toString(), ordId, userId, followTime);
	}

	@Override
	public Map<String, Object> getConfirmFollow(Integer orderId, Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append("select id ");
		query.append(" from waimai_order_follow ");
		query.append(" where ");
		query.append(" order_id = ?  ");
		query.append(" and user_id = ? ");
		return findOneForJdbc(query.toString(), orderId, userId);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> saveUserLocationInRedis(Double mLongitude, Double mLatitude, Integer userId, 
			Double uLongitude, Double uLatitude){
		Map<String, Object> result = new HashMap<String, Object>();
		WUserEntity user = get(WUserEntity.class, userId);
		
		Jedis jedis = null;
		IDistributedLock lock = null;
		String uuid = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return result;
			}
			
			Map<String, String> addressDetail = baiduMapService.getAddressDetail(uLongitude, uLatitude);
			//保存快递员ID到ZSet
			String cityCode = addressDetail.get("cityCode");
			
			int userType = courierInfoService.getCourierTypeByUserId(userId);
			if(userType == Constants.CROWDSOURCING_COURIER){
				if(!StringUtils.equals(cityCode, "0")){
					Map<String, String> position = new HashMap<String, String>();
					position.put("lng", uLongitude.toString());
					position.put("lat", uLatitude.toString());
					position.put("cityCode", cityCode);
					
					//获取锁
					lock = new RedisDistributedLock(jedis);
					uuid = lock.tryAcquireLock(Constants.LOCK_COURIER_POS, 10, 20);
					
					Pipeline pipeline = jedis.pipelined();
					String courierIdKey = RedisKeyUtil.courierIdKeyByCity(cityCode);
					pipeline.zadd(courierIdKey, System.currentTimeMillis()*-1, userId.toString());
					//保存快递位置到Map
					String poisitionKey = RedisKeyUtil.courierPositionKey(userId);
					pipeline.hmset(poisitionKey, position);
					pipeline.expire(poisitionKey, 24*60*60);
					pipeline.sync();;
					
					result.put("userName", user.getUsername());
					result.put("uploadTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
					result.put("distance", baiduMapService.getDistance(mLongitude, mLatitude, uLongitude, uLatitude, Constants.RIDING_MODE, addressDetail.get("city"), addressDetail.get("city")));
					
				}
				else{
					result.put("userName", user.getUsername());
					result.put("uploadTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
					result.put("distance","无法确定上传位置所在城市，经纬度:"+uLongitude+","+uLatitude);
				}
			}
			
		} 
		finally{
			try {
				if(lock != null && uuid != null){
					lock.releaseLock(Constants.LOCK_COURIER_POS, uuid);
				}
				JedisPool pool = MyJedisPool.getInstance().getPool();
				if(pool != null){
					pool.returnResource(jedis);
				}
			} 
			catch (Exception e) {
			}
		}
		return result;
	}

	@Override
	public int getSupplyChainWarehouseCourierCount(String warehouseHandlersStr) {
		if(warehouseHandlersStr == null || warehouseHandlersStr.isEmpty()){
			warehouseHandlersStr = "0";
		}
		StringBuilder sb = new StringBuilder("SELECT COUNT(DISTINCT dw.driver_id) courierCount ");
		sb.append(" FROM 0085_driver_warehouse dw, 0085_courier_info cinf ");
		sb.append(" WHERE dw.warehouse_id IN (");
		sb.append(warehouseHandlersStr);
		sb.append(") ");
		sb.append(" 	AND cinf.courier_type IN(4, 5) ");
		sb.append(" 	AND dw.driver_id = cinf.courier_id ");
		int result = this.findOneForJdbc(sb.toString(), Integer.class);
		return result;
	}

}
