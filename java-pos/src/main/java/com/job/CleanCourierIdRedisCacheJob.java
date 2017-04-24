package com.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.order.scamble.ScheduledCleanServiceI;

public class CleanCourierIdRedisCacheJob extends QuartzJobBean{

	private final static Logger logger = LoggerFactory.getLogger(CleanCourierIdRedisCacheJob.class);
	
	@Autowired
	private ScheduledCleanServiceI scheduledCleanService;
	
	@Override
	protected void executeInternal(JobExecutionContext context)throws JobExecutionException {
		logger.info("begin to clean redis Cache ...");
		scheduledCleanService.cleanExpiredCourierIds();
		logger.info("Clean redis Cache end!");
	}
	
}
