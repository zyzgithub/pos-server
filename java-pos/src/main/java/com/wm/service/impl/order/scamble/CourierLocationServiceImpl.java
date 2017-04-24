package com.wm.service.impl.order.scamble;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.courier_mana.common.Constants;
import com.wm.service.order.scamble.CourierLocationServiceI;
import com.wm.util.IDistributedLock;
import com.wm.util.MyJedisPool;
import com.wm.util.RedisDistributedLock;
import com.wm.util.RedisKeyUtil;

@Service("courierLocationService")
@Configuration
public class CourierLocationServiceImpl implements CourierLocationServiceI {

	private final static Logger logger = LoggerFactory.getLogger(CourierLocationServiceImpl.class);
	
	@Value("${redis_server}")
	private String redisHost;
	
	@Value("${redis_password}")
	private String password;
	
	@Override
	public long getZSetIndexByCriticalTime(String cityCode, double criticalTime) {
		String key = RedisKeyUtil.courierIdKeyByCity(cityCode);
		return getZSetIndexByCriticalTimeIntertal(key, criticalTime);
	}
	
	@Override
	public long getZSetIndexByCriticalTimeIntertal(String key, double criticalTime){
		long index = -1;
		
		IDistributedLock lock = null;
		Jedis jedis = null;
		String uuid = null;
		try {
			jedis = MyJedisPool.getInstance().getJedis(password);
			if(jedis == null){
				logger.error("无法连接redis服务器");
				return -1;
			}
			//获取锁
			lock = new RedisDistributedLock(jedis);
			uuid = lock.tryAcquireLock(Constants.LOCK_COURIER_POS, 10, 20);
			
			if(uuid != null){
				long low = 0;
				long high = jedis.zcard(key);
				
				
				if(high > 0){
					Set<String> firstMember = jedis.zrange(key, low, low);
					double firstMemberTime = jedis.zscore(key, firstMember.toArray()[0].toString())*(-1);
					//如果第一个成员的时间比临界点时间要早
					if(firstMemberTime < criticalTime){
						index = -1;
					}
					else {
						Set<String> lastMember = jedis.zrange(key, high-1, high-1);
						double lastMemberTime = jedis.zscore(key, lastMember.toArray()[0].toString())*(-1);
						//如果最后一行的时间比临界点时间要晚
						if(lastMemberTime >= criticalTime){
							index = high;
						}
						else {
							boolean stop = true;
							
							do{
								//取中间位置
								index = (low + high) /2;
								Set<String> curMember = jedis.zrange(key, index, index);
								//获取中间位置快递员上传位置的时间
								double curMemberTime = jedis.zscore(key, curMember.toArray()[0].toString())*(-1);
								
								//如果当前行快递员上传的时间比临界点的时间要早，往上找，往上早时间更晚
								if(curMemberTime < criticalTime){
									
									Set<String> preMember = jedis.zrange(key, index-1, index-1);
									double preMemberTime = jedis.zscore(key, preMember.toArray()[0].toString())*(-1);
									
									//临界点时间刚好比当前时间点要晚，比上一个时间点要早
									if(criticalTime >= curMemberTime && criticalTime <= preMemberTime){
										stop = false; 
										index = index - 1;
									}
									else{
										high = index - 1;
									}
								}
								//如果当前行快递员上传的时间比临界点的时间要晚，往下找，往下早时间更早
								else {
									Set<String> nextMember = jedis.zrange(key, index+1, index+1);
									double nextMemberTime = jedis.zscore(key, nextMember.toArray()[0].toString())*(-1);
									
									//临界点时间刚好比当前时间点要晚，比下一个时间点要早
									if(criticalTime <= curMemberTime && criticalTime >= nextMemberTime){
										stop = false;  
									}
									else{
										low = index + 1;
									}
								}
							}while(stop && low < high);
						}
					}
				}
			}
		} 
		finally {
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
		
		return index;
	}

}
