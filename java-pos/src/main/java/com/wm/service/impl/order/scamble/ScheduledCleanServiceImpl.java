package com.wm.service.impl.order.scamble;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.wm.service.order.scamble.CourierLocationServiceI;
import com.wm.service.order.scamble.ScheduledCleanServiceI;
import com.wm.util.MyJedisPool;
import com.wm.util.RedisKeyUtil;


@Service("scheduledCleanService")
@Configuration
public class ScheduledCleanServiceImpl implements ScheduledCleanServiceI {
	private final static Logger logger = LoggerFactory.getLogger(ScheduledCleanServiceImpl.class);
	
	@Value("${redis_server}")
	private String redisHost;
	
	@Value("${redis_password}")
	private String password;
	
	@Value("${report_location_minutes_limit}")
	private int minutesLimit;
	
	@Autowired
	private CourierLocationServiceI courierLocationService;

	@Override
	public void cleanExpiredCourierIds() {
		Jedis jedis = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			Set<String> keys = jedis.keys(RedisKeyUtil.allCourierIdKey());
			if(CollectionUtils.isNotEmpty(keys)){
				for(String key: keys){
					double criticalTime = System.currentTimeMillis() - (minutesLimit*60*1000);
					long index = courierLocationService.getZSetIndexByCriticalTimeIntertal(key, criticalTime);
					//所有的member都必须清除
					if(index == -1 ){
						index = 0;
					}
					long len = jedis.zcard(key);
					if(index >= 0 && index <= len){
						Set<String> courierIds = jedis.zrange(key, index, len);
						String[] courierIdArrays =  new String[courierIds.size()];
						courierIds.toArray(courierIdArrays);
						String[] courierPosArrays = new String[courierIds.size()];
						int i = 0;
 						for(String courierId:courierIds){
 							courierPosArrays[i++] = RedisKeyUtil.courierPositionKey(Integer.parseInt(courierId));
 						}
 						Pipeline pipeline = jedis.pipelined();
						pipeline.zrem(key, courierIdArrays);
						pipeline.del(courierPosArrays);
						pipeline.sync();
					}
			
				} 
			}
		}
		finally {
			JedisPool pool = MyJedisPool.getInstance().getPool();
			if(pool != null){
				pool.returnResource(jedis);
			}
		}
	}

}
