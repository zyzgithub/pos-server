package com.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.rebate.RebateServiceI;

/**
 * 定时任务：商家返点奖励补贴发放
 * 时间：每个月1号统计上个月16号-月末的扫码订单
 * 	        每个月16号统计当月1-15号的扫码订单
 * @author suyuqiang
 */
public class MerchantGrantByMonthJob extends QuartzJobBean{
	private static final Logger logger = Logger.getLogger(MerchantGrantByMonthJob.class);
	
	@Autowired
	private RebateServiceI rebateService; 

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("定时任务-商家返点奖励补贴发放定时器");
		rebateService.payMerchantRebate();
	}

}
