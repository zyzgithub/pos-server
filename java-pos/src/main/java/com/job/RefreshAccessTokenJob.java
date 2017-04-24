package com.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wp.AccessTokenContext;

/**
 * 定时任务-重新获取微信公众号ACCESS_TOKEN
 * @author Simon
 */
public class RefreshAccessTokenJob extends QuartzJobBean {
	
	private static final Logger logger = Logger.getLogger(RefreshAccessTokenJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-重新获取微信公众号ACCESS_TOKEN");
		
		AccessTokenContext.setAccessToken();
		
		AccessTokenContext.setJsapiTicket();
	}
	
}
