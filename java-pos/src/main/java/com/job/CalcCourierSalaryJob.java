package com.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;

/**
 * 快递员工资计算定时任务
 *
 */
public class CalcCourierSalaryJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(CalcCourierSalaryJob.class);
	
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		DateTime now = DateTime.now();
		String year = now.toString("yyyy");
		String month = now.minusMonths(1).toString("MM");
		logger.info("---------------------------开始计算{}年-{}月工资---------------------------", year, month);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = courierSalaryService.getCourierIdsOfRequriedCalcSalary(year, month);
		
		logger.info("---------------------------本次共计算{}个快递员的工资---------------------------", courirerIds.size());
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				
				logger.info("---------------------------开始计算快递员{},{}年-{}月工资---------------------------", new Object[]{courierId, year, month});
				try {
					//保存快递员每天的考勤薪水
					attendanceSalaryService.updateAttendanceSalaryPerday(courierId, year, month);
					//计算快递员的实际工资
					courierSalaryService.calcAndSaveCourierRealSalary(courierId, year, month);
				} 
				catch (Exception e) {
					logger.error("开始计算快递员{},{}年-{}月工资发送错误,错误原因:{}", new Object[]{courierId, year, month, e.getMessage()});
				}
				
				logger.info("---------------------------计算快递员{},{}年-{}月工资完成---------------------------", new Object[]{courierId, year, month});
			}
		}
		
		logger.info("---------------------------计算{}年-{}月工资完成---------------------------", year, month);
	}
}
