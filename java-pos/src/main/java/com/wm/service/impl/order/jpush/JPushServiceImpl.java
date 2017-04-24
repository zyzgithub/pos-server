package com.wm.service.impl.order.jpush;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.base.schedule.ScheduledUtil;
import com.jpush.JPush;
import com.jpush.SoundFile;
import com.wm.entity.jpush.JPushLogEntity;
import com.wm.service.jpush.JPushLogServiceI;
import com.wm.service.order.jpush.JpushServiceI;

@Service("jpushService")
public class JPushServiceImpl implements JpushServiceI{

	private final static Logger logger = LoggerFactory.getLogger(JPushServiceImpl.class);
	
	@Autowired
	private JPushLogServiceI jPushLogService;
	
	@Override
	public void pushNewOrderToCourier(Integer orderId, Integer courierId, Integer canScramble){
		Map<String, String> pushMap = new HashMap<String, String>();
		pushMap.put("orderId", orderId.toString());
		pushMap.put("scramble", canScramble.toString());// 可抢单数
		
		String title = "您有一条新的可抢订单";
		pushMap.put("title", title);
		pushMap.put("content", title);
		pushMap.put("voiceFile", SoundFile.SOUND_NEW_ORDER);
		
		push(courierId, pushMap);
	}
	
	public void push(final Integer userId, final Map<String, String> pushMap) {
		
		pushMap.put("userId", userId.toString());
		
		/*final String title = pushMap.get("title");
		final String content = pushMap.get("content");
		final String voiceFile = pushMap.get("voiceFile");

		final JPushLogEntity jpushLog = new JPushLogEntity();
        jpushLog.setPushTime(DateUtils.getSeconds());
        jpushLog.setTitle(title);
		jpushLog.setContent(content);
		jpushLog.setVoiceFile(voiceFile);
		jpushLog.setTargets(userId.toString());
		jpushLog.setExtras(pushMap.toString());
		jpushLog.setIsFeedBack(0);
        
		jPushLogService.save(jpushLog);
		pushMap.put("jpushLogId", jpushLog.getId().toString());
		logger.info("jpush send：userId:{}, pushMap:{}, jpushLogId:{}", userId, pushMap, jpushLog.getId());
		String result = JPush.push(new String[] { userId.toString() }, title, content, voiceFile, pushMap);
		logger.info("jpush receive： result:{}", result);
		jpushLog.setExtras(pushMap.toString());
		jpushLog.setResultTime(DateUtils.getSeconds());
		jpushLog.setResult(result);
		logger.info("jpushLog : {}", jpushLog);
		jPushLogService.saveOrUpdate(jpushLog);*/
        
		logger.info("pushMap:{}", JSONObject.toJSONString(pushMap));
		PushTask task = new PushTask(pushMap);
		ScheduledUtil.runNodelayTask(task, ScheduledUtil.JPUSH_POOL);
	}
	
	public class PushTask implements Runnable {
		
		private Map<String, String> pushMap;

		public PushTask(Map<String, String> pushMap) {
			this.pushMap = pushMap;
		}

		public void run() {
			push();
		}

		private void push() {
			String userId = pushMap.get("userId");
			String title = pushMap.get("title");
			String content = pushMap.get("content");
			String voiceFile = pushMap.get("voiceFile");

			JPushLogEntity jpushLog = new JPushLogEntity();
	        jpushLog.setPushTime(DateUtils.getSeconds());
	        jpushLog.setTitle(title);
			jpushLog.setContent(content);
			jpushLog.setVoiceFile(voiceFile);
			jpushLog.setTargets(userId);
			jpushLog.setExtras(pushMap.toString());
			jpushLog.setIsFeedBack(0);
			
			jPushLogService.save(jpushLog);
			pushMap.put("jpushLogId", jpushLog.getId().toString());
			logger.info("jpush send：userId:{}, pushMap:{}, jpushLogId:{}", userId, pushMap, jpushLog.getId());
			String result = JPush.push(new String[] { userId }, title, content, voiceFile, pushMap);
			logger.info("jpush receive： result:{}", result);
			jpushLog.setExtras(pushMap.toString());
			jpushLog.setResultTime(DateUtils.getSeconds());
			jpushLog.setResult(result);
			logger.info("jpushLog : {}", JSONObject.toJSONString(jpushLog));
			jPushLogService.saveOrUpdate(jpushLog);
		} 
	}
}

