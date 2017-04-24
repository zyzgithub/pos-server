package com.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.menu.MenuServiceI;

/**
 * 定时任务-初始化库存
 * @author Simon
 */
public class InitRepertoryJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(InitRepertoryJob.class);

	@Autowired
	MenuServiceI menuService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-初始化库存");
		
		String sql = "UPDATE menu m SET m.today_repertory=m.repertory";
		menuService.updateBySqlString(sql);
		
	}
	
}
