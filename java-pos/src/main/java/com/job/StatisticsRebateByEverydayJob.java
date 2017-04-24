package com.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.rebate.RebateServiceI;

/**
 * 定时任务：1、商家每天返点奖励补贴计算  2、物流人员每天奖励计算
 * 时间：每天凌晨统计前一天的扫码支付
 * @author suyuqiang
 */
public class StatisticsRebateByEverydayJob extends QuartzJobBean{
	private static final Logger logger = Logger.getLogger(StatisticsRebateByEverydayJob.class);
	
	@Autowired
	RebateServiceI rebateService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("定时任务-商家每天返点奖励补贴计算");
		rebateService.statMerchantRebateByEveryday();
		
		logger.info("定时任务-物流人员每天奖励计算");
		rebateService.statCourierRebateByEveryday();
	}

}
