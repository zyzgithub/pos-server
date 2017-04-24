package com.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular.SuspendAccount;
import com.wm.service.impl.courieraccount.regular.OrSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByOpenUp;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByPunch;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByScramble;
import com.wm.service.position.PositionServiceI;

/**
 * 快递员封号定时任务
 *
 */
public class SuspendCourierAccountJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(SuspendCourierAccountJob.class);
	
	@Autowired
	private AttendanceServiceI attendanceService;

	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Autowired
	private CourierAccountSuspendServiceI courierAccountSuspendService;
	
	@Autowired
	private PositionServiceI positionService;
	
	@Override
	protected void executeInternal(JobExecutionContext context){
		logger.info("启动快递员封号定时任务...");
		AbstractSuspendAccountRegular regularByPunch = null;
		AbstractSuspendAccountRegular regularByScamble = null;
		AbstractSuspendAccountRegular regularByOpenup = null;
		//如果打卡的约束规则生效
		if(courierAccountSuspendService.isPunchConstraintEnable()){
			regularByPunch = new SuspendAccountByPunch();
			regularByPunch.setAttendanceService(attendanceService);
			regularByPunch.setCourierAccountSuspendService(courierAccountSuspendService);
			regularByPunch.setCourierSalaryService(courierSalaryService);
			regularByPunch.setPositionService(positionService);
		}
		
		//如果抢单的约束规则生效
		if(courierAccountSuspendService.isScrambleConstraintEnable()){
			regularByScamble = new SuspendAccountByScramble();
			regularByScamble.setAttendanceService(attendanceService);
			regularByScamble.setCourierAccountSuspendService(courierAccountSuspendService);
			regularByScamble.setCourierSalaryService(courierSalaryService);
			regularByScamble.setPositionService(positionService);
		}
		
		if(courierAccountSuspendService.isOpenupConstraintEnable()){
			regularByOpenup = new SuspendAccountByOpenUp();
			regularByOpenup.setAttendanceService(attendanceService);
			regularByOpenup.setCourierAccountSuspendService(courierAccountSuspendService);
			regularByOpenup.setCourierSalaryService(courierSalaryService);
			regularByOpenup.setPositionService(positionService);
		}
		
		AbstractSuspendAccountRegular finalRegular = null;
		if(regularByPunch != null && (regularByScamble == null && regularByOpenup == null)){
			finalRegular = regularByPunch;
		}
		else if( regularByScamble != null && (regularByPunch == null && regularByOpenup == null)){
			finalRegular = regularByScamble;
		}
		else if( regularByOpenup != null && (regularByPunch == null && regularByScamble == null)){
			finalRegular = regularByOpenup;
		}
		else if( regularByScamble != null && regularByPunch != null && regularByOpenup == null) {
			finalRegular = new OrSuspendAccountRegular(regularByScamble, regularByPunch);
		}
		else if( regularByPunch != null &&  regularByOpenup != null && regularByScamble == null) {
			finalRegular = new OrSuspendAccountRegular(regularByPunch, regularByOpenup);
		}
		else if( regularByScamble != null && regularByOpenup != null && regularByPunch == null) {
			finalRegular = new OrSuspendAccountRegular(regularByScamble, regularByPunch);
		}
		else if(regularByPunch != null && regularByScamble != null && regularByOpenup != null){
			finalRegular = new OrSuspendAccountRegular(
					new OrSuspendAccountRegular(regularByPunch, regularByScamble), regularByOpenup) ;
		}
		else{
			logger.info("后台没有设置封号规则，定时任务结束!");
			return;
		}
		
		
		List<SuspendAccount> suspendAccounts = finalRegular.findSuspendAccountsByRegular();
		logger.info("快递员封号定时任务, 本次共封号的账号数为:{}", suspendAccounts.size());
		if(CollectionUtils.isNotEmpty(suspendAccounts)){
			for(SuspendAccount suspendAccount: suspendAccounts){
				courierAccountSuspendService.suspendAccount(suspendAccount.getCourierId(), suspendAccount.getSuspendReason());
				logger.info("对快递员:{}封号，封号原因:{}", suspendAccount.getCourierId(), suspendAccount.getSuspendReason());
			}
		}
		
		logger.info("完成快递员封号定时任务...");
	}

}
