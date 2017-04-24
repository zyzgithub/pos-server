package com.job;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.wm.service.order.PushedOrderServiceI;
import com.wm.util.MyJedisPool;

/**
 * 定时任务-监控可抢订单
 * @author Simon
 */
public class MonitorPushedOrderJob extends QuartzJobBean {
	
	private static final Logger logger = Logger.getLogger(MonitorPushedOrderJob.class);
	private static final int EXPIRED_HOURS = 24;

	@Autowired
	PushedOrderServiceI pushedOrderService;
	
	@Value("${redis_password}")
	private String password;
	
	@Value("${is_using_new_algorithm}")
	private boolean isUsingNewAlgorithm;
	
	private void executeRepush(){
		//删除过期的订单
		String before = DateTime.now().minusHours(EXPIRED_HOURS).toString("yyyy-MM-dd HH:mm:ss");
		pushedOrderService.deleteAndBackupExpiredRecord(before);
		
		String sql = "select distinct po.pushed_courier from 0085_pushed_order po ";
		sql += " left join `order` o on po.order_id=o.id where o.courier_id = 0 ";
		List<Map<String, Object>> list = pushedOrderService.findForJdbc(sql);
		if(list != null && list.size() > 0){
			for(Map<String, Object> map : list){
				Integer courierId = Integer.parseInt(map.get("pushed_courier").toString());
				try {
					pushedOrderService.rePushOrder(courierId);
				} catch (Exception e) {
					logger.error("重新提醒快递员【" + courierId + "】抢单失败！", e);
				}
			}
		}
	}
	
	private void executeRepushNew(){
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
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-监控可抢订单");
		
//		DateTime now = new DateTime();
//		if(0 == now.getMinuteOfDay()){
//			// 凌晨清空表
////			List<PushedOrderEntity> allPushedOrders = pushedOrderService.findHql("from PushedOrderEntity");
////			pushedOrderService.deleteAllEntitie(allPushedOrders);
//			Integer delCount = pushedOrderService.deleteInvalid();
//			logger.info("清空可抢订单临时表，影响记录数：" + delCount);
//			return ;
//		}
		//是否可以用新的推单算法
		if(isUsingNewAlgorithm){
			executeRepushNew();
		}
		else {
			executeRepush();
		}
		
	}
	
}
