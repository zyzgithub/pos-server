package com.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.rebate.RebateServiceI;

/**
 * 定时任务：物流人员奖励发放
 * 时间：每个月十号统计上个月整个月的扫码订单
 * @author suyuqiang
 */
public class CourierGrantByMonthJob extends QuartzJobBean{
	private static final Logger logger = Logger.getLogger(CourierGrantByMonthJob.class);
	
	private RebateServiceI rebateService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("定时任务-物流人员奖励发放");
		try {
			this.rebateService.payCourierRebate();
		} catch (Exception e) {
			logger.error("物流人员奖励发放失败");
		}
	}

}
