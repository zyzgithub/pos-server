package com.wm.service.impl.jpush;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.jpush.JPushLogEntity;
import com.wm.service.jpush.JPushLogServiceI;

@Service("jPushLogService")
@Transactional
public class JPushLogServiceImpl extends CommonServiceImpl implements JPushLogServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(JPushLogServiceImpl.class);

	@Override
	public void changeIsFeedBack(Integer jpushLogId, JPushLogEntity jPushLogEntity) {
		logger.info("推送日志id ：" + jpushLogId);
		jPushLogEntity.setIsFeedBack(1);
		jPushLogEntity.setFeedBackTime((long) DateUtils.getSeconds());
		saveOrUpdate(jPushLogEntity);
	}
	
}