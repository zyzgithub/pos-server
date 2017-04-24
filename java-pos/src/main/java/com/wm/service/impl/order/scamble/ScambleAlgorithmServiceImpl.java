package com.wm.service.impl.order.scamble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.courier_mana.common.Constants;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.baidu.BaiduMapServiceI;
import com.wm.service.courier.TlmStatisticsServiceI;
import com.wm.service.order.PushedOrderServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.order.scamble.CourierLocationServiceI;
import com.wm.service.order.scamble.ScambleAlgorithmServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.util.DistanceUtil;
import com.wm.util.IDistributedLock;
import com.wm.util.MyJedisPool;
import com.wm.util.RedisDistributedLock;
import com.wm.util.RedisKeyUtil;

@Service("scambleAlgorithmService")
@Configuration
public class ScambleAlgorithmServiceImpl extends CommonServiceImpl implements ScambleAlgorithmServiceI{
	private static final Logger logger = LoggerFactory.getLogger(ScambleAlgorithmServiceImpl.class);
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final int TOLERANCE = 200;
	private static final String COURIER_SCAMBLE_LOCK_PREFIX = "courier_scamble_lock_prefix_";

	@Autowired
	private BaiduMapServiceI baiduMapService;
	
	@Autowired
	private CourierLocationServiceI courierPositionService;
	
	@Autowired
	private SystemconfigServiceI systemconfigService;
	
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private JpushServiceI jpushService;
	
	@Autowired
    private TlmStatisticsServiceI tlmStatisticsService;
	
	@Autowired
	private PushedOrderServiceI pushedOrderService;
	
	@Value("${redis_server}")
	private String redisHost;
	
	@Value("${redis_password}")
	private String password;
	
	@Value("${report_location_minutes_limit}")
	private int minutesLimit;
	
	/**
	 * 判断所抢订单是否有效
	 * @param orderId
	 * @return
	 */
	private boolean isScambleOrderValid(Integer orderId) {
		boolean success = true;
		String sqltmp = "select id, courier_id, rstate, state  from `order` where id=? ";
        Map<String, Object> orderInfo = findOneForJdbc(sqltmp, new Object[] { orderId });
        
        if (orderInfo == null) {
            logger.warn("该订单【" + orderId + "】不存在");
            success = false;
        } else {
            String orderCourierId = orderInfo.get("courier_id") == null? "0": orderInfo.get("courier_id").toString();
            if(!"0".equals(orderCourierId)){
                logger.warn("该订单{}已被其他快递员{}抢走!", orderId, orderCourierId);
                success = false;
            }
            
            if(!StringUtils.equals("accept", orderInfo.get("state").toString())){
            	logger.warn("该订单{}商家还未接单!", orderId);
                success = false;
            }
            String restate = orderInfo.get("rstate") == null? "": orderInfo.get("rstate").toString();
            if(StringUtils.equals("berefund", restate)){
            	logger.warn("该订单【{}】用户已退款", orderId);
                success = false;
            }
        }
		return success;
	}
	
	/**
	 * 更新订单对应的快递员
	 * @param orderId
	 * @param courierId
	 * @return
	 */
	private boolean updateOrderCourier(Integer orderId, Integer courierId){
		logger.info("保存快递员{}到订单{}记录中", orderId, courierId);
		int updateRows = executeSql("update `order` set courier_id = ? where id=?", courierId, orderId);
		return updateRows == 1;
	}
	
	/**
	 * 快递员是否存在可抢订单
	 * @param courierId
	 * @return
	 */
	@Override
	public boolean existCanScambleOrder(Integer courierId) {
		Jedis jedis = null;
		boolean success = false;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return false;
			}
			List<String> orderIdStrs = jedis.lrange(RedisKeyUtil.queueCanScambleOrdersKey(courierId), 0, -1);
			
			if(CollectionUtils.isNotEmpty(orderIdStrs)){
				for(String orderId: orderIdStrs){
					if(isScambleOrderValid(Integer.parseInt(orderId))){
						success = true;
						break;
					}
					else{
						deleteOrderCascade(Integer.parseInt(orderId));
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			success = false;
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return success;
	}
	
	private Integer deQueueOneOrder(Integer courierId) {
		Integer orderId = null;
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(existCanScambleOrder(courierId)){
				String orderIdStr = jedis.rpop(RedisKeyUtil.queueCanScambleOrdersKey(courierId));
				orderId = Integer.parseInt(orderIdStr);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return orderId;
	}
	
	@Override
	public void courierScambleOrder(Integer courierId, Integer orderId){
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis != null){
				
				//获取能抢该订单对应的快递员队列
				List<String> courierIds = jedis.lrange(RedisKeyUtil.queueCanScambleCouriersKey(orderId), 0, -1);
				Integer deleteRows = courierIds.size();
				Pipeline pipe = jedis.pipelined();
				if(CollectionUtils.isNotEmpty(courierIds)){
					for(String courierIdStr: courierIds){
						pipe.del(RedisKeyUtil.orderCourierKey(orderId, Integer.parseInt(courierIdStr)));
						pipe.lrem(RedisKeyUtil.queueCanScambleOrdersKey(Integer.parseInt(courierIdStr)), 1, orderId.toString());
					}
				}
				
				logger.info("抢单临时记录已删除，orderId=" + orderId + ",删除记录数=" + deleteRows);
				pipe.del(RedisKeyUtil.queueCanScambleCouriersKey(orderId));
				pipe.srem(RedisKeyUtil.allOrdersKey(), orderId.toString());
				pipe.sync();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
	}
	
	@Override
	public List<Integer> canScambleOrders(Integer courierId) {
		List<Integer> orderIds = new ArrayList<Integer>();
		
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis != null){
				List<String> orderIdStrs = jedis.lrange(RedisKeyUtil.queueCanScambleOrdersKey(courierId), 0, -1);
				
				for(String orderId: orderIdStrs){
					orderIds.add(Integer.parseInt(orderId));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return orderIds;
	}
	
	
	@Override
	public AjaxJson courierScamble(Integer courierId){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		
		if(!attendanceService.isOnDuty(courierId)){
			j.setMsg("您还没打卡上班,不能抢单");
			return j;
		}
		
		Jedis jedis = null;
		String lockName = COURIER_SCAMBLE_LOCK_PREFIX + courierId;
		IDistributedLock lock = null;
		String uuid = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				j.setMsg("抢单失败，请重试");
				logger.error("获取redis连接失败...");
				return j;
			}
			
			lock = new RedisDistributedLock(jedis);
			uuid = lock.tryAcquireLock(lockName, 10, 20);
			if(uuid == null){
				j.setMsg("抢单失败，请重试");
				logger.warn("另一个线程正在更新快递员{}可抢订单队列，无法获取锁", courierId);
				return j;
			}
			
			if(!existCanScambleOrder(courierId)){
				j.setMsg("您目前没有可抢订单");
				return j;
			}
			
			Integer orderId = deQueueOneOrder(courierId);
			if(orderId == null){
				j.setMsg("您目前没有可抢订单");
				return j;
			}
			else {
				courierScambleOrder(courierId, orderId);
				updateOrderCourier(orderId, courierId);
				j.setObj(orderId);
				j.setSuccess(true);
				j.setMsg("抢单成功！");
			}
			
			return j;
		} 
		finally{
			MyJedisPool.getInstance().close(jedis);
			if(uuid != null){
				lock.releaseLock(lockName, uuid);
			}
		}
	}
	
	
	private boolean deleteOrderCascade(Integer orderId){
		boolean success = true;
		Jedis jedis = null;
		
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return false;
			}
			
			List<String> courierIds = jedis.lrange(RedisKeyUtil.queueCanScambleCouriersKey(orderId), 0, -1);
			Pipeline pipeline = jedis.pipelined();
			//删除订单
			pipeline.srem(RedisKeyUtil.allOrdersKey(), orderId.toString());
			
			if(CollectionUtils.isNotEmpty(courierIds)){
				for(String courierId: courierIds){
					//删除快递员订单信息
					pipeline.del(RedisKeyUtil.orderCourierKey(orderId, Integer.parseInt(courierId)));
					//删除快递员的可抢订单
					pipeline.lrem(RedisKeyUtil.queueCanScambleOrdersKey(Integer.parseInt(courierId)), 1, orderId.toString());
				}
			}
			
			//删除订单对应的快递员
			pipeline.del(RedisKeyUtil.queueCanScambleCouriersKey(orderId));
			pipeline.sync();
			success = true;
		}
		catch(Exception e){
			e.printStackTrace();
			success = false;
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return success;
	}
	
	/**
	 * 处理已退款订单
	 * @param orderId
	 * @return
	 */
	@Override
	public boolean deleteRefundOrder(Integer orderId){
		return deleteOrderCascade(orderId);
	}
	
	
	private boolean exsitCourierOrderRela(Integer orderId, Integer courierId){
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis != null){
				
				String key = RedisKeyUtil.orderCourierKey(orderId, courierId);
				Map<String, String> pushOrderInfo = jedis.hgetAll(key);
				return (pushOrderInfo != null && pushOrderInfo.size() > 0);
			}
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return false;
	}
	
	/**
	 * 建立快递员与订单的关联关系
	 * @param orderId
	 * @return
	 */
	private void createCourierOrderRela(Jedis jedis, Integer orderId, Integer courierId){
		Pipeline pipeline = jedis.pipelined();
		//订单入订单队列
		pipeline.sadd(RedisKeyUtil.allOrdersKey(), orderId.toString());
		//订单匹配的快递员
		pipeline.lpush(RedisKeyUtil.queueCanScambleCouriersKey(orderId), courierId.toString());
		//快递员对应的订单
		pipeline.lpush(RedisKeyUtil.queueCanScambleOrdersKey(courierId), orderId.toString());
		
		Map<String, String> pushOrderInfo = new HashMap<String, String>();
		pushOrderInfo.put("order_id", orderId.toString());
		pushOrderInfo.put("courier_id", courierId.toString());
		pushOrderInfo.put("create_time", DateTime.now().toString(DATETIME_FORMAT));
		pushOrderInfo.put("update_time", DateTime.now().toString(DATETIME_FORMAT));
		pipeline.hmset(RedisKeyUtil.orderCourierKey(orderId, courierId), pushOrderInfo);
		pipeline.sync();
	}
	
	/**
	 * 更新订单重推时间
	 * @param orderId
	 * @param courierId
	 */
	private void updateOrderRepushTime(Integer orderId, Integer courierId){
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis != null){
				String key = RedisKeyUtil.orderCourierKey(orderId, courierId);
				jedis.hset(key, "update_time", DateTime.now().toString(DATETIME_FORMAT));
			}
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
	}
	
	@Override
	public Long canScrambleNum(Integer courierId, Boolean isRepush){
		if(!attendanceService.isOnDuty(courierId)){
			logger.warn("快递员【" + courierId + "】不在上班时间!");
			return 0L;
		}
		
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			
			if(jedis != null){
				//重推
				if(isRepush){
					long len = 0;
					//获取快递员可抢订单队列
					List<String> orders =  jedis.lrange(RedisKeyUtil.queueCanScambleOrdersKey(courierId), 0, -1);
					for(String orderId: orders){
						String key = RedisKeyUtil.orderCourierKey(Integer.parseInt(orderId), courierId);
					    Map<String, String> pushOrderInfo = jedis.hgetAll(key);
					    
					    DateTime updateTime = DateTimeFormat.forPattern(DATETIME_FORMAT).parseDateTime(pushOrderInfo.get("update_time"));
					    DateTime curTime = DateTime.now();
					    
					    if(Minutes.minutesBetween(updateTime, curTime).getMinutes() > 3){
					    	len++;
					    }
					}
					return len;
				}
				else {
					return jedis.llen(RedisKeyUtil.queueCanScambleOrdersKey(courierId));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
		return 0L;
	}
	
	/**
	 * 建立订单与快递、快递员与订单相对于的模型,并推送消息给快递员
	 * @param jedis
	 * @param orderId
	 * @param courierId
	 */
	private void internalPushToCourier(Jedis jedis, Integer orderId, Integer courierId){
		//如果不存在快递员与订单的对应关系
		if (!exsitCourierOrderRela(orderId, courierId)) {
			createCourierOrderRela(jedis, orderId, courierId);
		}
		else {
			updateOrderRepushTime(orderId, courierId);
		}
		
		Integer canScramble = canScrambleNum(courierId, false).intValue();
		jpushService.pushNewOrderToCourier(orderId, courierId, canScramble);
	}
	
	
	@Override
	public void pushToCouriers(Integer orderId, List<Integer> courierIds) {
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(CollectionUtils.isNotEmpty(courierIds)){
				for(Integer courierId: courierIds){
					String lockName = COURIER_SCAMBLE_LOCK_PREFIX + courierId;
					IDistributedLock lock = null;
					String uuid = null;
					try{
						lock = new RedisDistributedLock(jedis);
						uuid = lock.tryAcquireLock(lockName, 10, 20);
						if(uuid == null){
							logger.warn("推单{},另一个线程也在更新快递员{}可抢订单队列、快递员订单映射，无法获取锁", orderId, courierId);
							break;
						}
						internalPushToCourier(jedis, orderId, courierId);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally{
						if(uuid != null){
							lock.releaseLock(lockName, uuid);
						}
					}
				}
			}
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
	}
	
	
	@Override
	public void pushToCourier(Integer orderId, Integer courierId) {
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			String lockName = "courier_lock_" + courierId;
			IDistributedLock lock = null;
			String uuid = null;
			try{
				lock = new RedisDistributedLock(jedis);
				uuid = lock.tryAcquireLock(lockName, 10, 20);
				if(uuid == null){
					logger.warn("推单{},另一个线程也在更新快递员{}可抢订单队列、快递员订单映射，无法获取锁", orderId, courierId);
					return;
				}
				internalPushToCourier(jedis, orderId, courierId);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(uuid != null){
					lock.releaseLock(lockName, uuid);
				}
			}
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
	}
	
	@Override
	public void deleteAndBackupExpiredRecord(String before){
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			
			Set<String> keys = jedis.keys("courier:scamble:order_courier:*");
			
			if(CollectionUtils.isNotEmpty(keys)){
				for(String key: keys){
					Map<String, String> orderInfo = jedis.hgetAll(key);
					String createTimeStr = orderInfo.get("create_time");
					DateTime createTime = DateTimeFormat.forPattern(DATETIME_FORMAT).parseDateTime(createTimeStr);
					DateTime currentTime = DateTimeFormat.forPattern(DATETIME_FORMAT).parseDateTime(before);
					
					//早于某个时间的单
					if(createTime.compareTo(currentTime) < 0){
						Integer courierId = Integer.parseInt(orderInfo.get("courier_id"));
						String lockName = "courier_lock_" + courierId;
						IDistributedLock lock = null;
						String uuid = null;
						try{
							lock = new RedisDistributedLock(jedis);
							uuid = lock.tryAcquireLock(lockName, 10, 20);
							
							Integer orderId = Integer.parseInt(orderInfo.get("order_id"));
							deleteOrderCascade(orderId);
							pushedOrderService.saveExpiredOrder(orderId, courierId, createTime.getMillis()/1000);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						finally{
							if(uuid != null){
								lock.releaseLock(lockName, uuid);
							}
						}
					}
				}
			}
		}
		finally{
			MyJedisPool.getInstance().close(jedis);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void executeRepushNew(){
		//删除过期的订单
		String before = DateTime.now().minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
		deleteAndBackupExpiredRecord(before);
		
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			
			if(jedis != null){
				//获取没有被抢的订单
				Set<String> keys = jedis.keys("courier:scamble:queue_by_courier*");
				if(CollectionUtils.isNotEmpty(keys)){
					Iterator<String> iter = keys.iterator();
					
					while(iter.hasNext()){
						String key = iter.next();
						String[] infos = key.split(":");
						Integer courierId = Integer.parseInt(infos[infos.length-1]);
						pushedOrderService.rePushOrderNew(courierId);
					}
				}
			}
		}
		finally{
			JedisPool pool = MyJedisPool.getInstance().getPool();
			if(pool != null){
				pool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public List<Integer> getLatestReportPosCourierIds(double lng, double lat, int minutes){
		
		Map<String, String> addressDetail = baiduMapService.getAddressDetail(lng, lat);
		
		if(addressDetail == null || "0".equals(addressDetail.get("cityCode"))){
			logger.warn("lng:{}， lat:{}，参数错误，无法根据经纬度反查经纬度所在的城市！", lng, lat);
			return new ArrayList<Integer>();
		}
		else {
			double criticalTime = System.currentTimeMillis() - (minutesLimit*60*1000);
			long index = courierPositionService.getZSetIndexByCriticalTime(addressDetail.get("cityCode"), criticalTime);
			if(index >= 0){
				IDistributedLock lock = null;
				Jedis jedis = null;
				String uuid = null;
				try {
					jedis = MyJedisPool.getInstance().getJedis(password);
					if(jedis == null){
						logger.error("无法连接redis服务器");
						return new ArrayList<Integer>();
					}
					//获取锁
					lock = new RedisDistributedLock(jedis);
					uuid = lock.tryAcquireLock(Constants.LOCK_COURIER_POS, 10, 20);
					List<Integer> courierIds = new ArrayList<Integer>();
					if(uuid != null){
						String key = RedisKeyUtil.courierIdKeyByCity(addressDetail.get("cityCode"));
						Set<String> couriers = jedis.zrange(key, 0, index);
						
						for(String courierId:couriers){
							courierIds.add(Integer.valueOf(courierId));
						}
					}
					else {
						logger.warn("无法获取锁...");
					}
					return courierIds;
				} 
				finally{
					try {
						if(lock != null && uuid != null){
							lock.releaseLock(Constants.LOCK_COURIER_POS, uuid);
						}
						MyJedisPool.getInstance().close(jedis);
					}
					catch (Exception e) {
					}
				}
			}
			else {
				return new ArrayList<Integer>();
			}
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Integer> findNearestCouiriers(double lng, double lat, 
			 int radis, int num){
		
		List<Integer> courierIds = this.getLatestReportPosCourierIds(lng, lat, minutesLimit);
		//根据快递员到商家的直线距离过滤掉那些距离商家比较远的快递员
		filterByLineDistance(courierIds, lng, lat);
		
		Jedis jedis = null;
		
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return new ArrayList<Integer>();
			}
			List<CourierDistance> courierDistances = new ArrayList<ScambleAlgorithmServiceImpl.CourierDistance>();
			for(Integer courierId: courierIds){
				
				String courierPostionKey = RedisKeyUtil.courierPositionKey(courierId);
				Map<String, String> position = jedis.hgetAll(courierPostionKey);
				
				if(position != null){
					Map<String, String> addressDetail = baiduMapService.getAddressDetail(lng, lat);
					if(addressDetail == null){
						logger.warn("无法根据经纬度获取商家所在的城市,经度:{},纬度:{}", lng, lat);
						continue;
					}
					
					int distance = baiduMapService.getDistance(lng, lat, 
							Double.parseDouble(position.get("lng")), Double.parseDouble(position.get("lat")),
							Constants.RIDING_MODE, addressDetail.get("city"), addressDetail.get("city"));
					if(distance == Integer.MAX_VALUE){
						distance = (int)DistanceUtil.getShortDistance(lng, lat, Double.parseDouble(position.get("lng")), Double.parseDouble(position.get("lat")));
					}
					logger.info("courierId:{}, lng:{}, lat:{}, distance:{}", courierId, position.get("lng"), position.get("lat"), distance);
					CourierDistance courierDistance = new CourierDistance(distance, courierId);
					courierDistances.add(courierDistance);
				}
			}
			
			Collections.sort(courierDistances, new Comparator<CourierDistance>(){
				@Override
				public int compare(CourierDistance o1, CourierDistance o2){
					if(o1.getDistance() < o2.getDistance()){
						return 1;
					}
					if(o1.getDistance() > o2.getDistance()){
						return -1;
					}
					return 0;
				}
				
			});
			
			int count = 0;
			List<Integer> ids = new ArrayList<Integer>();
			for(CourierDistance courierDistance: courierDistances){
				if(courierDistance.getDistance() < radis+TOLERANCE && count < num){
					ids.add(courierDistance.getCourierId());
					count++;
				}
			}
			return ids;
		} 
		finally{
			JedisPool pool = MyJedisPool.getInstance().getPool();
			if(pool != null){
				pool.returnResource(jedis);
			}
		}
		
	}
	
	@SuppressWarnings("deprecation") 
	@Override
	public void filterByLineDistance(List<Integer> courierIds, double lng, double lat){
		if(CollectionUtils.isNotEmpty(courierIds)){
			Jedis jedis = null;
			try {
				jedis = MyJedisPool.getInstance().getJedis(password);
				if(jedis == null){
					logger.error("无法连接redis服务器");
					return;
				}
				Iterator<Integer> iter = courierIds.iterator();
				while(iter.hasNext()){
					Integer courierId = iter.next();
					String courierPostionKey = RedisKeyUtil.courierPositionKey(courierId);
					Map<String, String> position = jedis.hgetAll(courierPostionKey);
					
					if(position != null){
						double distance = DistanceUtil.getShortDistance(
								Double.parseDouble(position.get("lng")), 
								Double.parseDouble(position.get("lat")), lng, lat);
						int radis = 1500;
						try {
							radis = Integer.parseInt(systemconfigService.getValByCode("push_order_distance"));
						} catch (Exception e) {
							logger.warn("获取众包快递员推单距离（单位：米）设置失败，设置为默认值1500米");
						}
						
						//直线距离大于设定的距离
						if(distance > radis+TOLERANCE){
							logger.info("移除距离比较远的快递员,快递员ID:{}", courierId);
							iter.remove();
						}
					}
					
				}
			}
			finally{
				JedisPool pool = MyJedisPool.getInstance().getPool();
				if(pool != null){
					pool.returnResource(jedis);
				}
			}
			
		}
	}
	
	
	public static class CourierDistance{
		private final int distance;
		private final Integer courierId;
		
		public CourierDistance(int distance, Integer courierId){
			this.distance = distance;
			this.courierId = courierId;
		}

		public double getDistance() {
			return distance;
		}

		public Integer getCourierId() {
			return courierId;
		}
	}
}
