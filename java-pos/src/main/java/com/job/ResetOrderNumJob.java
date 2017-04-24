package com.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.util.AliOcs;

/**
 * 定时任务-重置订单排号
 * @author Simon
 */
public class ResetOrderNumJob extends QuartzJobBean {

	private static final Logger logger = Logger.getLogger(ResetOrderNumJob.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-重置订单排号");
		
		AliOcs.initOrderNum();
	}
	
}
