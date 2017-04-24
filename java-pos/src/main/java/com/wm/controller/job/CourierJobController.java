package com.wm.controller.job;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.open_api.OpenResult.State;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.OrSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByOpenUp;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByPunch;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByScramble;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular.SuspendAccount;
import com.wm.service.org.OrgServiceI;
import com.wm.service.position.PositionServiceI;
import com.wm.service.rebate.RebateServiceI;
import com.wm.service.statistics.CourierStatisticsDalyServiceI;
import com.wm.service.statistics.OrgStatisticsDaylyServiceI;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/courier")
public class CourierJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(CourierJobController.class);

	@Autowired
	private OrgServiceI orgService;
	@Autowired
	private RebateServiceI rebateService;
	@Autowired
	private PositionServiceI positionService;
	@Autowired
	private AttendanceServiceI attendanceService;
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	@Autowired
	private OrgStatisticsDaylyServiceI orgStatisticsDaylyService;
	@Autowired
	private CourierAccountSuspendServiceI courierAccountSuspendService;
	@Autowired
	private CourierStatisticsDalyServiceI courierStatisticsDalyService;
	

	/**
	 * 生成昨天快递员日提成记录,订单统计表
	 */
	@RequestMapping("/genDeductLog")
	@ResponseBody
	public AjaxJson genDeductLog(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		List<WUserEntity> couriers = courierStatisticsDalyService.getCouriersNeedToCalcDeduct();
		int size = couriers.size();
		logger.info("genDeductLog courier total size:" + size);
		if(couriers != null && size > 0){
			for (WUserEntity courier : couriers) {
				logger.info("courier current index:" + size--);
				try {
					courierStatisticsDalyService.statisticsDayly(courier.getId());
				} catch (Exception e) {
					logger.error("生成昨天快递员【" + courier.getId() + "】日提成记录失败！", e);
				}
			}
		}
		
		// 统计所有网点日人均订单量
		List<OrgEntity> orgs = orgService.findHql("from OrgEntity where status=1 and level=6");
		size = couriers.size();
		logger.info("genDeductLog orgs total size:" + size);
		if(orgs != null && orgs.size() > 0){
			for(OrgEntity org : orgs){
				logger.info("courier current index:" + size--);
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
		j.setSuccess(true);
		j.setMsg("生成昨天快递员日提成记录,订单统计表完成!!");
		return j;
	}
	
	/**
	 * 计算快递员工资
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/calcSalary")
	@ResponseBody
	public AjaxJson calcSalary(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		DateTime now = DateTime.now();
		String year = now.toString("yyyy");
		String month = now.minusMonths(1).toString("MM");
		logger.info("---------------------------开始计算{}年-{}月工资---------------------------", year, month);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = courierSalaryService.getCourierIdsOfRequriedCalcSalary(year, month);
		if(CollectionUtils.isNotEmpty(courirerIds)){
			int size = courirerIds.size();
			logger.info("calcSalary courier total size:" + size);
			logger.info("---------------------------本次共计算{}个快递员的工资---------------------------", size);
			for(Integer courierId: courirerIds){
				logger.info("courier current index:" + size--);
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
		j.setSuccess(true);
		j.setMsg("计算快递员工资完成!!");
		return j;
	}
	
	/**
	 * 物流人员奖励发放
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/rebateGrant")
	@ResponseBody
	public AjaxJson rebateGrant(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			logger.info("定时任务-物流人员奖励发放");
			rebateService.payCourierRebate();
		} catch (Exception e) {
			logger.error("物流人员奖励发放失败", e);
		}
		j.setSuccess(true);
		j.setMsg("物流人员奖励发放完成!!");
		return j;
	}
	
	/**
	 * 统计快递员的考勤天数
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/statAttend")
	@ResponseBody
	public AjaxJson statAttend(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		String date = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
		logger.info("---------------------------开始统计日期:{} 快递员考勤---------------------------", date);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = attendanceService.getPunchCourierIds(date);
		if(CollectionUtils.isNotEmpty(courirerIds)){
			int size = courirerIds.size();
			logger.info("statAttend courier total size:" + size);
			logger.info("---------------------------本次共统计{}个快递员的考勤---------------------------", size);
			for(Integer courierId: courirerIds){
				try {
					attendanceSalaryService.statAttendancePerDay(courierId, date);
					logger.info("---------------------------统计日期:{}快递员考勤完成---------------------------", courierId);
				}
				catch (Exception e) {
					logger.error("统计快递员{}的考勤天数错误,错误原因:{}", new Object[]{courierId, e.getMessage()});
				}
			}
		}
		logger.info("---------------------------统计日期:{}快递员考勤完成---------------------------", date);
		
		j.setSuccess(true);
		j.setMsg("统计快递员的考勤天数完成!!");
		return j;
	}
	
	/**
	 * 封号
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/suspend")
	@ResponseBody
	public AjaxJson suspend(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
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
			j.setSuccess(false);
			j.setMsg("后台没有设置封号规则！");
			logger.error("后台没有设置封号规则，定时任务结束!");
			return j;
		}
		
		List<SuspendAccount> suspendAccounts = finalRegular.findSuspendAccountsByRegular();
		logger.info("快递员封号定时任务, 本次共封号的账号数为:{}", suspendAccounts.size());
		if(CollectionUtils.isNotEmpty(suspendAccounts)){
			int size = suspendAccounts.size();
			logger.info("suspend courier total size:" + size);
			for(SuspendAccount suspendAccount: suspendAccounts){
				Integer courierId = suspendAccount.getCourierId();
				try{
					courierAccountSuspendService.suspendAccount(courierId, suspendAccount.getSuspendReason());
					logger.info("对快递员:{}封号，封号原因:{}", courierId, suspendAccount.getSuspendReason());
				} catch (Exception e) {
					logger.error("对快递员:{}封号失败,错误原因:{}", new Object[]{courierId, e.getMessage()});
				}
			}
		}
		logger.info("完成快递员封号定时任务...");
		
		j.setSuccess(true);
		j.setMsg("物流人员封号完成!!");
		return j;
	}

}
