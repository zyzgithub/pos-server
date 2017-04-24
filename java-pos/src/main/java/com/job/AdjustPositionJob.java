package com.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.entity.couriergroupmember.CourierGroupMemberEntity;
import com.wm.entity.courierinfo.CourierInfoEntity;
import com.wm.entity.courierposition.CourierPositionEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.positiongraderule.PositionGradeRuleEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.couriergroup.CourierGroupServiceI;
import com.wm.service.couriergroupmember.CourierGroupMemberServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.courierorg.CourierOrgServiceI;
import com.wm.service.courierposition.CourierPositionServiceI;
import com.wm.service.courierpositionlog.CourierPositionLogServiceI;
import com.wm.service.couriersalarylog.CourierSalaryLogServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.position.PositionServiceI;
import com.wm.service.positiongraderule.PositionGradeRuleServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 
 * @ClassName: AdjustPositionJob
 * @Description: 定时任务-调整快递员职位
 * @author 黄聪
 * @date 2015年9月1日 下午3:10:23
 *
 */
public class AdjustPositionJob extends QuartzJobBean {

	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private CourierServiceI courierService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	@Autowired
	private OrgServiceI orgService;
	@Autowired
	private CourierOrgServiceI courierOrgService;
	@Autowired
	private PositionServiceI positionService;
	@Autowired
	private CourierPositionServiceI courierPositionService;
	@Autowired
	private CourierGroupServiceI courierGroupService;
	@Autowired
	private CourierGroupMemberServiceI courierGroupMemberService;
	@Autowired
	private CourierPositionLogServiceI courierPositionLogService;
	@Autowired
	private CourierSalaryLogServiceI CourierSalaryLogService;
	@Autowired
	private PositionGradeRuleServiceI positionGradeRuleService;
	
	private static final Logger logger = LoggerFactory.getLogger(AdjustPositionJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		logger.info("定时任务-调整快递员职位");
		
		String state = "confirm";
		String beginTime = getLastDay();
		String endTime = getFirstDay();
		List<WUserEntity> users = wUserService.findUserByType("courier");
		CourierInfoEntity courierInfo = new CourierInfoEntity();
		CourierPositionEntity courierPosition = new CourierPositionEntity();
		List<PositionGradeRuleEntity> positionGradeRules = 	positionGradeRuleService.findByPropertyisOrder(PositionGradeRuleEntity.class, "", "", false);
		if (users != null && users.size() > 0) {
			for (WUserEntity wUserEntity : users) {
				String courierOrderCountStr = "0";//快递员订单总数
				int oldPositionId = 0;//快递员职称ID
				int oldSalary = 0;//快递员原待遇
				int courierId = wUserEntity.getId();
				List<CourierInfoEntity> courierInfos = courierInfoService.findByProperty(CourierInfoEntity.class, "courierId", courierId);
				if(courierInfos != null && courierInfos.size() > 0){
					 courierInfo = courierInfos.get(0);
				}
				if(courierInfo != null){
					oldSalary = courierInfo.getSalary();
					CourierOrgVo courierOrgVo= courierService.getOrgByUserId(courierId);
					if(courierOrgVo != null){
						oldPositionId = courierOrgVo.getPositionId();
						if(courierOrgVo.getPositionSortOrder() <= 2){//见习员工和业务员调整
							 List<Map<String, Object>> list = orderService
										.getOrderNumbersByCourierId(courierId, state,
												beginTime, endTime);
								if (list != null && list.size() > 0) {
									Map<String, Object> map = list.get(0);
									courierOrderCountStr = map.get("c").toString();
									if(positionGradeRules!=null && positionGradeRules.size() > 0){
										for (PositionGradeRuleEntity positionGradeRuleEntity : positionGradeRules) {
											if(Integer.parseInt(courierOrderCountStr)>=positionGradeRuleEntity.getTotalOrder()){
												int newSalary = positionGradeRuleEntity.getSalary();
												int newPositionId = positionGradeRuleEntity.getPositionId();
												courierService.saveAdjustPosition(courierId, oldSalary, newSalary, courierInfo, oldPositionId, newPositionId, courierPosition);
												break ;
											}
										}
									}
								}
						}else if(courierOrgVo.getPositionSortOrder() == 3){//物流组长调整
							List<CourierGroupMemberEntity> courierGroupMembers = courierGroupMemberService.findByProperty(CourierGroupMemberEntity.class, "groupId", courierOrgVo.getGroupId());
							if(courierGroupMembers!=null && courierGroupMembers.size() > 0){
								int courierTotleCount = 0;
								for (CourierGroupMemberEntity courierGroupMemberEntity : courierGroupMembers) {
									List<Map<String, Object>> list = orderService
											.getOrderNumbersByCourierId(courierGroupMemberEntity.getUserId(), state,
													beginTime, endTime);
									if (list != null && list.size() > 0) {
										Map<String, Object> map = list.get(0);
										courierOrderCountStr = map.get("c").toString();
										courierTotleCount = courierTotleCount+Integer.parseInt(courierOrderCountStr);
									}
									if(positionGradeRules!=null && positionGradeRules.size() > 0){
										for (PositionGradeRuleEntity positionGradeRuleEntity : positionGradeRules) {
											if(courierTotleCount>=positionGradeRuleEntity.getTotalOrder()){
												int newSalary = positionGradeRuleEntity.getSalary();
												int newPositionId = positionGradeRuleEntity.getPositionId();
												courierService.saveAdjustPosition(courierId, oldSalary, newSalary, courierInfo, oldPositionId, newPositionId, courierPosition);
												break ;
											}
										}
									}
								}					
							}
						}else if(courierOrgVo.getPositionSortOrder() == 4 || courierOrgVo.getPositionSortOrder() == 5){//见习片区经理和片区经理调整
							List<CourierOrgVo> courierOrgs = courierService.queryOrgByOrgId(courierOrgVo.getOrgId());
							if(courierOrgs !=null && courierOrgs.size() > 0){
								int courierTotleCount = 0;
								for (CourierOrgVo courierOrg : courierOrgs) {
									List<Map<String, Object>> list = orderService
											.getOrderNumbersByCourierId(courierOrg.getCourierId(), state,
													beginTime, endTime);
									if (list != null && list.size() > 0) {
										Map<String, Object> map = list.get(0);
										courierOrderCountStr = map.get("c").toString();
										courierTotleCount = courierTotleCount+Integer.parseInt(courierOrderCountStr);
									}
									if(positionGradeRules!=null && positionGradeRules.size() > 0){
										for (PositionGradeRuleEntity positionGradeRuleEntity : positionGradeRules) {
											if(courierTotleCount>=positionGradeRuleEntity.getTotalOrder()){
												int newSalary = positionGradeRuleEntity.getSalary();
												int newPositionId = positionGradeRuleEntity.getPositionId();
												courierService.saveAdjustPosition(courierId, oldSalary, newSalary, courierInfo, oldPositionId, newPositionId, courierPosition);
												break ;
											}
										}
									}
								}					
							}
						}else{//区域副经理和区域经理调整
							List<OrgEntity> orgList = orgService.findByProperty(OrgEntity.class, "pid", courierOrgVo.getOrgPid());
							if (orgList != null && orgList.size() > 0) {
								for (OrgEntity orgEntity : orgList) {
									List<CourierOrgVo> courierOrgs = courierService.queryOrgByOrgId(orgEntity.getId());
									if(courierOrgs !=null && courierOrgs.size() > 0){
										int courierTotleCount = 0;
										for (CourierOrgVo courierOrg : courierOrgs) {
											List<Map<String, Object>> list = orderService
													.getOrderNumbersByCourierId(courierOrg.getCourierId(), state,
															beginTime, endTime);
											if (list != null && list.size() > 0) {
												Map<String, Object> map = list.get(0);
												courierOrderCountStr = map.get("c").toString();
												courierTotleCount = courierTotleCount+Integer.parseInt(courierOrderCountStr);
											}
											if(positionGradeRules!=null && positionGradeRules.size() > 0){
												for (PositionGradeRuleEntity positionGradeRuleEntity : positionGradeRules) {
													if(courierTotleCount>=positionGradeRuleEntity.getTotalOrder()){
														int newSalary = positionGradeRuleEntity.getSalary();
														int newPositionId = positionGradeRuleEntity.getPositionId();
														courierService.saveAdjustPosition(courierId, oldSalary, newSalary, courierInfo, oldPositionId, newPositionId, courierPosition);
														break ;
													}
												}
											}
										}					
									}
								}
								
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 当月第一天
	 * 
	 * @return
	 */
	private static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		return day_first;

	}

	/**
	 * 上月第一天
	 * 
	 * @return
	 */
	private static String getLastDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date theDate = calendar.getTime();
		String s = df.format(theDate);
		return s;
	}

	public static void main(String str[]) throws ParseException {
		System.out.println(	DateTime.now().dayOfMonth().withMinimumValue()  
                .toString("yyyy-MM-dd"));
		System.out.println(DateTime.now().plusMonths(-1).dayOfMonth().withMinimumValue()  
                .toString("yyyy-MM-dd"));
	}
}
