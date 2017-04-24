package com.wm.service.impl.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.courier_mana.common.Constants;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.ExpiredPushedOrderEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.PushedOrderEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.baidu.BaiduMapServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PushedOrderServiceI;
import com.wm.service.order.scamble.ScambleAlgorithmServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.util.MyJedisPool;
import com.wm.util.RedisKeyUtil;

@Service("pushedOrderService")
@Transactional
public class PushedOrderServiceImpl extends CommonServiceImpl implements PushedOrderServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(PushedOrderServiceImpl.class);
	
	private static final int TOLERANCE = 200;
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final int EXPIRED_HOURS = 24;
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private SystemconfigServiceI systemconfigService;
	
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	@Autowired
	private BaiduMapServiceI baiduMapService;
	
	@Autowired
	private ScambleAlgorithmServiceI scambleAlgorithmService;
	
	@Value("${report_location_minutes_limit}")
	private int minutesLimit;
	
	@Value("${repush_delay_time}")
	private String repushDelayTime;
	
	@Value("${redis_password}")
	private String password;
	
	@Value("${is_using_new_algorithm}")
	private boolean isUsingNewAlgorithm;
	
	public Long canScrambleNum(Integer courierId, Boolean isRepush){
		if(!attendanceService.isOnDuty(courierId)){
			logger.warn("快递员【" + courierId + "】不在上班时间!");
			return 0L;
		}
		String sql = "select count(*) from 0085_pushed_order po ";
		sql += " where po.pushed_courier=? ";
		if(isRepush){
			sql += " and timediff(CURTIME(),FROM_UNIXTIME(po.latest_update_time,'%H:%i:%s'))>=? ";
			if(StringUtils.isEmpty(repushDelayTime)){
				repushDelayTime = "00:03:00";
			}
			return this.getCountForJdbcParam(sql, new Object[]{courierId, repushDelayTime});
		}
		return this.getCountForJdbcParam(sql, new Object[]{courierId});
	}
	
	public List<Map<String, Object>> canScramble(Integer courierId, Boolean isRepush) {
		if(!attendanceService.isOnDuty(courierId)){
			logger.warn("快递员【" + courierId + "】不在上班时间!");
			return null;
		}
		String sql = "select po.* from 0085_pushed_order po ";
		sql += " where po.pushed_courier=? ";
		if(isRepush){
			sql += " and timediff(CURTIME(),FROM_UNIXTIME(po.latest_update_time,'%H:%i:%s'))>=? ";
			if(StringUtils.isEmpty(repushDelayTime)){
				repushDelayTime = "00:03:00";
			}
			return this.findForJdbc(sql, new Object[]{courierId, repushDelayTime});
		}
		return this.findForJdbc(sql, new Object[]{courierId});
	}
	
	
	@SuppressWarnings("deprecation")
	public List<Map<String, Object>> canScrambleNew(Integer courierId, Boolean isRepush) {
		if(!attendanceService.isOnDuty(courierId)){
			logger.warn("快递员【" + courierId + "】不在上班时间!");
			return null;
		}
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			
			if(jedis != null){
				if(isRepush){
					
					List<String> orders =  jedis.lrange(RedisKeyUtil.queueCanScambleOrdersKey(courierId), 0, -1);
					for(String orderId: orders){
						String key = RedisKeyUtil.orderCourierKey(Integer.parseInt(orderId), courierId);
					    Map<String, String> pushOrderInfo = jedis.hgetAll(key);
					    
					    
					    DateTime updateTime = DateTimeFormat.forPattern(DATETIME_FORMAT).parseDateTime(pushOrderInfo.get("update_time"));
					    DateTime curTime = DateTime.now();
					    
					    if(Minutes.minutesBetween(updateTime, curTime).getMinutes() >= 3){
//					    	jedis.hset(key, "update_time", curTime.toString(DATETIME_FORMAT));
					    	
					    	Map<String, Object> pushOrder = new HashMap<String, Object>();
					    	pushOrder.putAll(pushOrderInfo);
					    	pushOrder.put("update_time", curTime.toString(DATETIME_FORMAT));
					    	result.add(pushOrder);
					    }
					}
					
				}
				else {
					List<String> orders =  jedis.lrange(RedisKeyUtil.queueCanScambleOrdersKey(courierId), 0, -1);
					for(String orderId: orders){
						String key = RedisKeyUtil.orderCourierKey(Integer.parseInt(orderId), courierId);
					    Map<String, String> pushOrderInfo = jedis.hgetAll(key);
						Map<String, Object> pushOrder = new HashMap<String, Object>();
				    	pushOrder.putAll(pushOrderInfo);
				    	result.add(pushOrder);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		finally{
			JedisPool pool = MyJedisPool.getInstance().getPool();
			if(pool != null){
				pool.returnResource(jedis);
			}
		}
		return result;
	}
	
	/**
	 * 过滤离商家已经超过允许距离、或者长时间没有上传位置的快递员，不再向其推单
	 * @param list
	 */
	private void filterInvalidPushedOrder(List<Map<String, Object>> list ){
		if(list != null && list.size() > 0){
			Jedis jedis = null;
			try {
				jedis = MyJedisPool.getInstance().getJedis(password);
				if(jedis == null){
					logger.warn("无法连接到redis服务器...");
					return;
				}
				
				Iterator<Map<String, Object>> iter = list.iterator();
				while (iter.hasNext()){
					Map<String, Object> map = iter.next();
					Integer orderId = Integer.parseInt(map.get("order_id").toString());
					Integer courierId = Integer.parseInt(map.get("pushed_courier").toString());
					
					OrderEntity order = get(OrderEntity.class, orderId);
					//根据订单ID找不到对应的订单
					if( order == null){
						continue;
					}
					
					MerchantEntity merchant = null;
					try {
						merchant = order.getMerchant();
						//无法确定订单对应的商家、商家经纬度
						if(merchant == null || merchant.getLng() == null || merchant.getLat() == null){
							continue;
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					//推单信息是否有效：判断众包快递员是否还在商家附近
					boolean isPushedOrderValid = false;
					Map<String, String> addressDetail = baiduMapService.getAddressDetail(merchant.getLng(), merchant.getLat());
					//能够确定商家所在的城市
					if(addressDetail != null && !StringUtils.equals("0", addressDetail.get("cityCode"))){
						
						String courierIdsKey = RedisKeyUtil.courierIdKeyByCity(addressDetail.get("cityCode"));
						Double score = jedis.zscore(courierIdsKey, courierId.toString());
						if(score != null){
							long uploadTime = score.longValue()*(-1);
							long latestTime = System.currentTimeMillis() - minutesLimit*60*1000;
							//如果最新上传时间是当前时间minutesLimit多少分钟后
							if(uploadTime > latestTime){
								String positionKey = RedisKeyUtil.courierPositionKey(courierId);
								Map<String, String> courierPosition = jedis.hgetAll(positionKey);
								if(courierPosition.get("lng") != null && courierPosition.get("lat") != null){
									int distiance = baiduMapService.getDistance(merchant.getLng(), merchant.getLat(), 
											Double.parseDouble(courierPosition.get("lng")), Double.parseDouble(courierPosition.get("lat")), 
											Constants.RIDING_MODE, addressDetail.get("city"), addressDetail.get("city"));
									int radis = 1500;
									try {
										radis = Integer.parseInt(systemconfigService.getValByCode("push_order_distance"));
									} catch (Exception e) {
										logger.error("获取众包快递员推单距离（单位：米）设置失败，设置为默认值1500米");
									}
									if(distiance < radis+TOLERANCE){
										isPushedOrderValid = true;
									}
								}
							}
						}
					}
					
					//无效的推单记录
					if(!isPushedOrderValid){
						String sql = " delete from 0085_pushed_order where order_id=? and pushed_courier=?";
						this.executeSql(sql, orderId, courierId);
						iter.remove();
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if(jedis != null){
					MyJedisPool.getInstance().close(jedis);
				}
			}
		}
	}

	public void rePushOrder(Integer courierId) {
		List<Map<String, Object>> list = this.canScramble(courierId, true);

		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		if(courierType != null && courierType.intValue() == Constants.CROWDSOURCING_COURIER){
			filterInvalidPushedOrder(list);
		}
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map : list){
				if (attendanceService.isOnDuty(courierId)) {
					Integer orderId = Integer.parseInt(map.get("order_id").toString());
					Integer canScrambleNum = this.canScrambleNum(courierId, true).intValue();
					logger.info("快递员[" + courierId + "]重新获得订单[" + orderId + "]推送消息:" + canScrambleNum);
					orderService.pushCanScramble(courierId, orderId, canScrambleNum);
				}
			}
		}
	}
	
	public void rePushOrderNew(Integer courierId) {
		List<Map<String, Object>> list = this.canScrambleNew(courierId, true);

		Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
		if(courierType != null && courierType.intValue() == Constants.CROWDSOURCING_COURIER){
			filterInvalidPushedOrder(list);
		}
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map : list){
				if (attendanceService.isOnDuty(courierId)) {
					Integer orderId = Integer.parseInt(map.get("order_id").toString());
					Integer canScrambleNum = scambleAlgorithmService.canScrambleNum(courierId, true).intValue();
					logger.info("快递员[" + courierId + "]重新获得订单[" + orderId + "]推送消息:" + canScrambleNum);
					scambleAlgorithmService.pushToCourier(orderId, courierId);
				}
			}
		}
	}


	public List<PushedOrderEntity> findPushedOrders(Integer orderId) {
		return this.findByProperty(PushedOrderEntity.class, "orderId", orderId);
	}
	
	public Integer deletePushedOrders(Integer orderId){
		String sql = "delete from 0085_pushed_order where order_id=?";
		return this.executeSql(sql, new Object[]{orderId});
	}

	@Override
	public Integer deleteInvalid() {
		String sql = "delete from 0085_pushed_order where from_unixtime(create_time, '%y-%m-%d')<curdate()";
		return this.executeSql(sql);
	}
	
	@Override
	public void deleteAndBackupExpired(Integer oid) {
		List<Integer> expiredIds = new ArrayList<Integer>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select p.id, p.order_id, p.pushed_courier, p.create_time ");
			sql.append(" from 0085_pushed_order p where order_id = ?");
			List<Map<String, Object>> expiredPushedOrderList = findForJdbc(sql.toString(), oid);
			
			if(CollectionUtils.isNotEmpty(expiredPushedOrderList)){
				for(Map<String, Object> pushedOrder: expiredPushedOrderList){
					Integer id = Integer.parseInt(pushedOrder.get("id").toString());
					expiredIds.add(id);
				}
				executeSql("delete from 0085_pushed_order where id in ("+ StringUtils.join(expiredIds, ",") + ")");
					logger.info("删除自动完成的订单成功,推送订单数为:" + expiredIds.size() + ", 推送订单id列表:" + expiredIds );
			}
		} 
		catch (Exception e) {
			logger.error("删除自动完成的订单失败,推送订单数为:" +  expiredIds.size() + ", 推送订单id列表:" + expiredIds);
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteAndBackupExpiredRecord(String before) {
		List<Integer> expiredIds = new ArrayList<Integer>();
		List<ExpiredPushedOrderEntity> expiredPushedOrders = new ArrayList<ExpiredPushedOrderEntity>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select p.id, p.order_id, p.pushed_courier, p.create_time ");
			sql.append(" from 0085_pushed_order p where from_unixtime(create_time, '%Y-%m-%d %H:%i:%S') < ?");
			List<Map<String, Object>> allExpiredPushedOrders = findForJdbc(sql.toString(), before);
			
			if(CollectionUtils.isNotEmpty(allExpiredPushedOrders)){
				for(Map<String, Object> pushedOrder: allExpiredPushedOrders){
					Integer id = Integer.parseInt(pushedOrder.get("id").toString());
					Integer orderId = Integer.parseInt(pushedOrder.get("order_id").toString());
					Integer courierId = Integer.parseInt(pushedOrder.get("pushed_courier").toString());
					long createTime = Long.parseLong(pushedOrder.get("create_time").toString());
					
					expiredIds.add(id);
					
					ExpiredPushedOrderEntity expiredPushedOrder = new ExpiredPushedOrderEntity();
					expiredPushedOrder.setOrderId(orderId);
					expiredPushedOrder.setPushedCourier(courierId);
					expiredPushedOrder.setOriginCreateTime(createTime);
					expiredPushedOrder.setCreateTime(System.currentTimeMillis()/1000);
					expiredPushedOrders.add(expiredPushedOrder);
				}
				
				batchSave(expiredPushedOrders);
				executeSql("delete from 0085_pushed_order where id in ("+ StringUtils.join(expiredIds, ",") + ")");
				logger.info("删除并备份过期的订单成功,过期的订单数:" + expiredPushedOrders.size() + ", 订单列表:" + expiredPushedOrders);
			}
		} 
		catch (Exception e) {
			logger.error("删除并备份过期的订单失败！！！！！过期的订单数:" + expiredPushedOrders.size() + ", 订单列表:" + expiredPushedOrders);
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveExpiredOrder(Integer orderId, Integer courierId, Long createTime){
		ExpiredPushedOrderEntity expiredPushedOrder = new ExpiredPushedOrderEntity();
		expiredPushedOrder.setOrderId(orderId);
		expiredPushedOrder.setPushedCourier(courierId);
		expiredPushedOrder.setOriginCreateTime(createTime);
		expiredPushedOrder.setCreateTime(System.currentTimeMillis()/1000);
		save(expiredPushedOrder);
	}
	
	@Override
	public void executeRepush()
	{
		//删除过期的订单
		String before = DateTime.now().minusHours(EXPIRED_HOURS).toString("yyyy-MM-dd HH:mm:ss");
		deleteAndBackupExpiredRecord(before);
		
		String sql = "select distinct po.pushed_courier from 0085_pushed_order po ";
		sql += " left join `order` o on po.order_id=o.id where o.courier_id = 0 ";
		List<Map<String, Object>> list = findForJdbc(sql);
		if(list != null && list.size() > 0){
			for(Map<String, Object> map : list){
				Integer courierId = Integer.parseInt(map.get("pushed_courier").toString());
				try {
					rePushOrder(courierId);
				} catch (Exception e) {
					logger.error("重新提醒快递员【" + courierId + "】抢单失败！", e);
				}
			}
		}
	}
}