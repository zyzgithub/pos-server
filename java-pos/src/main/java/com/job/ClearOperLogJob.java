package com.job;

import jeecg.system.service.SystemService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务-删除日志
 * @author Simon
 */
public class ClearOperLogJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ClearOperLogJob.class);

	@Autowired
	SystemService systemService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-删除历史日志");
		
		//删除操作日志-7天前
		String sql = "call p_delete_c_operate_log()";
		systemService.executeSql(sql);
		//删除极光推送日志-1个月前
		sql = "call p_delete_0085_jpush_log()";
		systemService.executeSql(sql);
		//删除快递员位置记录-1个月前
		sql = "call p_delete_0085_courier_location()";
		systemService.executeSql(sql);
		//删除快递员抢单日志-1个月前
		sql = "call p_delete_0085_courier_scramble_log()";
		systemService.executeSql(sql);
	}
	
}
