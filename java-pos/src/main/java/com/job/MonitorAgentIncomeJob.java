package com.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.flow.FlowServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;

public class MonitorAgentIncomeJob extends QuartzJobBean {
	private static final Logger logger = Logger.getLogger(MonitorAgentIncomeJob.class);
	
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	@Autowired
	private FlowServiceI flowService;

	@Override
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		logger.info("代理商定时器结算开始");
		String sql = "SELECT id FROM agent_income_timer WHERE FROM_UNIXTIME(create_time,'%Y-%m-%d')=DATE_SUB(CURDATE(),INTERVAL 1 DAY) ORDER BY order_id ";
		List<Map<String, Object>> list = orderIncomeService.findForJdbc(sql);
		orderIncomeService.unAgentIncome(list);
	}
	

}
