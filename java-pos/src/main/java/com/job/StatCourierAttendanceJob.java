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

import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;

/**
 * 统计快递员的考勤天数定时任务
 *
 */
public class StatCourierAttendanceJob extends QuartzJobBean {
	private final static Logger logger = LoggerFactory.getLogger(StatCourierAttendanceJob.class);
	
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		String date = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
		
		logger.info("---------------------------开始统计日期:{} 快递员考勤---------------------------", date);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = attendanceService.getPunchCourierIds(date);
		
		logger.info("---------------------------本次共统计{}个快递员的考勤---------------------------", courirerIds.size());
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				attendanceSalaryService.statAttendancePerDay(courierId, date);
				logger.info("---------------------------统计日期:{}快递员考勤完成---------------------------", courierId);
			}
		}
		logger.info("---------------------------统计日期:{}快递员考勤完成---------------------------", date);
	}

}
