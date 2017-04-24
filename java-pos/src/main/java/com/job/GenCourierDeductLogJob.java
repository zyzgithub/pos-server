package com.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.statistics.CourierStatisticsDalyServiceI;
import com.wm.service.statistics.OrgStatisticsDaylyServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 定时任务-生成昨天快递员日提成记录,订单统计表
 * @author Simon
 */
public class GenCourierDeductLogJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(GenCourierDeductLogJob.class);

	@Autowired
	OrgServiceI orgService;
	@Autowired
	WUserServiceI wUserService;
	@Autowired
	CourierServiceI courierService;
	@Autowired
	OrgStatisticsDaylyServiceI orgStatisticsDaylyService;
	@Autowired
	CourierStatisticsDalyServiceI courierStatisticsDalyService;
	

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-生成昨天快递员日提成记录,订单统计表");
		
		List<WUserEntity> couriers = courierStatisticsDalyService.getCouriersNeedToCalcDeduct();
		
		if(couriers != null && couriers.size() > 0){
			for (WUserEntity courier : couriers) {
				try {
					courierStatisticsDalyService.statisticsDayly(courier.getId());
				} catch (Exception e) {
					logger.error("生成昨天快递员【" + courier.getId() + "】日提成记录失败！", e);
				}
			}
		}
		
		// 统计所有网点日人均订单量
		List<OrgEntity> orgs = orgService.findHql("from OrgEntity where status=1 and level=6");
		if(orgs != null && orgs.size() > 0){
			for(OrgEntity org : orgs){
				try {
					orgStatisticsDaylyService.statisticsDayly(org);
				} catch (Exception e) {
					logger.error("统计网点【" + org.getId() + "】日人均订单失败！", e);
				}
			}
		}
		
		// 统计、结算管理层（商务经理）日提成
		/*
		List<Integer> bussMngLeaders = courierService.findCourierByPosition(3);
		for(Integer leaderId : bussMngLeaders){
			courierStatisticsDalyService.statisticsLeaderDayly(leaderId);
		}
		*/
		
		// 统计、结算管理层（区域经理）日提成
		/*
		List<Integer> areaMngLeaders = courierService.findCourierByPosition(5);
		for(Integer leaderId : areaMngLeaders){
			courierStatisticsDalyService.statisticsLeaderDayly(leaderId);
		}
		*/
	}
	
}
