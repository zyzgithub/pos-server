package courier.salary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.courier_mana.common.Constants;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courieraccount.CourierAccountSuspendServiceI;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.OrSuspendAccountRegular;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByPunch;
import com.wm.service.impl.courieraccount.regular.SuspendAccountByScramble;
import com.wm.service.impl.courieraccount.regular.AbstractSuspendAccountRegular.SuspendAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierSalaryTest {

	private final static Logger logger = LoggerFactory.getLogger(CourierSalaryTest.class);
	
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private AttendanceSalaryServiceI attendanceSalaryService;
	
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Autowired
	private CourierAccountSuspendServiceI courierAccountSuspendService;
	
	@Test
	public void testGetCourierIdsOfRequriedCalcSalary(){
		courierSalaryService.getCourierIdsOfRequriedCalcSalary("2016", "01");
	}
	
	@Test
	public void testGetAttendceRecords(){
		List<Map<String, Object>> ondutyRecords = attendanceService.getAttendceRecords(1, "2016-01-01", "2016-01-31", Constants.ONDUTY);
		List<Map<String, Object>> offdutyRecords = attendanceService.getAttendceRecords(1, "2016-01-01", "2016-01-31", Constants.OFFDUTY);
	}
	
	@Test
	public void testCalcWorkHours(){
		DateTime now = DateTime.now();
		//获取所有待计算的快递员列表
		String year = now.toString("yyyy");
		String month = now.minusMonths(1).toString("MM");
		
		DateTime firstDayLastMonth = now.minusMonths(1).withDayOfMonth(1);
		DateTime firstDayThisMonth = now.withDayOfMonth(1);
		
		
		List<Integer> courirerIds = courierSalaryService.getCourierIdsOfRequriedCalcSalary(year, month);
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				
				System.out.println("计算快递员:" + courierId);
				//获取每个快递员的考勤记录
				List<Map<String, Object>> ondutyRecords 
					= attendanceService.getAttendceRecords(courierId, firstDayLastMonth.toString("yyyy-MM-dd"), firstDayThisMonth.toString("yyyy-MM-dd"), Constants.ONDUTY);
				System.out.println("计算快递员:" + courierId + ", 打卡上班记录数:" + ondutyRecords.size());
				List<Map<String, Object>> offdutyRecords
					= attendanceService.getAttendceRecords(courierId, firstDayLastMonth.toString("yyyy-MM-dd"), firstDayThisMonth.toString("yyyy-MM-dd"), Constants.OFFDUTY);
				System.out.println("计算快递员:" + courierId + ", 打卡上班记录数:" + offdutyRecords.size());
				
				//对于该月的每一天，计算快递员的考勤记录
				DateTime iteration = firstDayLastMonth;
				while(iteration.isBefore(firstDayThisMonth)){
					String calDate = iteration.toString("yyyy-MM-dd");
					
					//首次打卡上班的记录
					Map<String, Object> firstPunchOndutyRecord = attendanceService.getFirstPunchOndutyRecord(courierId, calDate, ondutyRecords);
					Map<String, Object> firstPunchOffdutyRecord = attendanceService.getFirstPunchOffdutyRecord(courierId, calDate, offdutyRecords);
					
					if(firstPunchOndutyRecord != null){
						if(firstPunchOffdutyRecord == null){
							firstPunchOffdutyRecord = new HashMap<String, Object>();
							firstPunchOffdutyRecord.put("courierId", courierId);
							firstPunchOffdutyRecord.put("date", calDate);
							firstPunchOffdutyRecord.put("punchTime", calDate + " 23:59:59.0");
							firstPunchOffdutyRecord.put("type", 1);
							firstPunchOffdutyRecord.put("address", firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString());
						}
						
						double workhours = attendanceService.calPerDayWorkhours(courierId, calDate, firstPunchOndutyRecord, firstPunchOffdutyRecord);
						
						attendanceSalaryService.save(courierId, calDate, 
								firstPunchOndutyRecord.get("punchTime").toString(), 
								firstPunchOffdutyRecord.get("punchTime").toString(),
								firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString(),
								firstPunchOffdutyRecord.get("address") == null?"": firstPunchOffdutyRecord.get("address").toString(),
								workhours, 0.0);
						
						System.out.println("插入couirierId:" + courierId + ", 上班下班记录");
					}
					else {
						attendanceSalaryService.save(courierId, calDate, 
								calDate + " 00:00:00.0", 
								calDate + " 00:00:00.0",
								"",
								"",
								0.0, 0.0);
						
						System.out.println("插入couirierId:" + courierId + ", 上班下班记录--------");
					}
					
					
					iteration = iteration.plusDays(1);
				}
				
				//保存快递员记薪天数、出勤工资
			
				//
			}
		}
	}
	
	
	@Test
	public void testAutoCalcSalary(){
		DateTime now = DateTime.now();
		String year = now.toString("yyyy");
		String month = now.minusMonths(1).toString("MM");
		logger.info("---------------------------开始计算{}年-{}月工资---------------------------", year, month);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = courierSalaryService.getCourierIdsOfRequriedCalcSalary(year, month);
		
//		List<Integer> courirerIds = new ArrayList<Integer>();
//		courirerIds.add(168490);
		logger.info("---------------------------本次共计算{}个快递员的工资---------------------------", courirerIds.size());
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				
				logger.info("---------------------------开始计算快递员{},{}年-{}月工资---------------------------", new Object[]{courierId, year, month});
				//计算快递员的考勤工资
//				attendanceSalaryService.calcAndSaveAttendanceSalary(courierId, year, month);
				try{
					//计算快递员的实际工资
					courierSalaryService.calcAndSaveCourierRealSalary(courierId, year, month);
					logger.info("---------------------------计算快递员{},{}年-{}月工资完成---------------------------", new Object[]{courierId, year, month});
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
		logger.info("---------------------------计算{}年-{}月工资完成---------------------------", year, month);
	}
	
	@Test
	public void testStatCourierAttendanceSalary(){
		DateTime now = DateTime.now();
		String year = now.toString("yyyy");
		String month = now.minusMonths(2).toString("MM");
		logger.info("---------------------------开始计算{}年-{}月工资---------------------------", year, month);
		//获取所有待计算的快递员列表
		List<Integer> courirerIds = courierSalaryService.getCourierIdsOfRequriedCalcSalary(year, month);
		
//		List<Integer> courirerIds = new ArrayList<Integer>();
//		courirerIds.add(168490);
		logger.info("---------------------------本次共计算{}个快递员的工资---------------------------", courirerIds.size());
		if(CollectionUtils.isNotEmpty(courirerIds)){
			for(Integer courierId: courirerIds){
				
				logger.info("---------------------------开始计算快递员{},{}年-{}月工资---------------------------", new Object[]{courierId, year, month});
				//计算快递员的考勤工资
				attendanceSalaryService.calcAndSaveAttendanceSalary(courierId, year, month);
				
			}
		}
		
		logger.info("---------------------------计算{}年-{}月工资完成---------------------------", year, month);
	}
	
	@Test
	public void testCalPerDayWorkhours()throws Exception{
		attendanceService.calPerDayWorkhours(59274, "2016-01-27");
	}
	
	@Test
	public void testStatCourierAttendancePerDay()throws Exception{
		String date = DateTime.now().minusDays(4).toString("yyyy-MM-dd");
		
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
	
	
	@Test
	public void testSuspendCourierAccount(){
		logger.info("启动快递员封号定时任务...");
		AbstractSuspendAccountRegular regularByPunch = null;
		AbstractSuspendAccountRegular regularByScamble = null;
		//如果打卡的约束规则生效
		if(courierAccountSuspendService.isPunchConstraintEnable()){
			regularByPunch = new SuspendAccountByPunch();
			regularByPunch.setAttendanceService(attendanceService);
			regularByPunch.setCourierAccountSuspendService(courierAccountSuspendService);
			regularByPunch.setCourierSalaryService(courierSalaryService);
		}
		
		//如果抢单的约束规则生效
		if(courierAccountSuspendService.isScrambleConstraintEnable()){
			regularByScamble = new SuspendAccountByScramble();
			regularByScamble.setAttendanceService(attendanceService);
			regularByScamble.setCourierAccountSuspendService(courierAccountSuspendService);
			regularByScamble.setCourierSalaryService(courierSalaryService);
		}
		
		AbstractSuspendAccountRegular finalRegular = null;
		if(regularByPunch != null && regularByScamble == null){
			finalRegular = regularByPunch;
		}
		else if(regularByPunch == null && regularByScamble != null){
			finalRegular = regularByScamble;
		}
		else if(regularByPunch != null && regularByScamble != null){
			finalRegular = new OrSuspendAccountRegular(regularByPunch, regularByScamble);
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
