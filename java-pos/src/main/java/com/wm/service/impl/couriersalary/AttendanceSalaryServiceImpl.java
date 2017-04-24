package com.wm.service.impl.couriersalary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.Constants;
import com.wm.entity.couriersalary.AttendanceSalaryEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.couriersalary.AttendanceSalaryServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;

@Service("attendanceSalaryService")
public class AttendanceSalaryServiceImpl extends CommonServiceImpl implements
		AttendanceSalaryServiceI {
	
	@Autowired
	private AttendanceServiceI attendanceService;
	
	@Autowired
	private CourierSalaryServiceI courierSalaryService;
	
	@Override
	public void save(Integer courierId, String attendanceDate,
			String startTime, String endTime, String ondutyAddress, String offdutyAddress,  double workHours, double daySalary) {
		
		
		AttendanceSalaryEntity entity = new AttendanceSalaryEntity();
		entity.setCourierId(courierId);
		entity.setAttendanceDate(attendanceDate);
		entity.setStartTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(startTime).toDate());
		entity.setEndTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(endTime).toDate());
		entity.setOndutyAddress(ondutyAddress);
		entity.setOffdutyAddress(offdutyAddress);
		entity.setWorkingHours(workHours);
		entity.setDaySalary(daySalary);
		
		this.save(entity);
	
	}
	
	@Override
	public void calcAndSaveAttendanceSalary(Integer courierId, String year, String month){
		
		DateTime firstDayLastMonth = new DateTime().withYear(Integer.parseInt(year)).withMonthOfYear(Integer.parseInt(month)).withDayOfMonth(1);
		DateTime firstDayThisMonth = new DateTime().withYear(Integer.parseInt(year)).withMonthOfYear(Integer.parseInt(month)).plusMonths(1).withDayOfMonth(1);
		
		
		String calStartDate = firstDayLastMonth.toString("yyyy-MM-dd");
		String calEndDate = firstDayThisMonth.toString("yyyy-MM-dd");
		//获取每个快递员的考勤记录
		List<Map<String, Object>> ondutyRecords 
			= attendanceService.getAttendceRecords(courierId, calStartDate, calEndDate, Constants.ONDUTY);
		
		List<Map<String, Object>> offdutyRecords
			= attendanceService.getAttendceRecords(courierId, calStartDate, calEndDate, Constants.OFFDUTY);
		
		double lowestWorkHours = courierSalaryService.getLowestWorkHoursPerDay(); 
		double salary = courierSalaryService.getSalary(courierId, year, month);
		int requiredAttendanceDays = courierSalaryService.getRequiredAttendanceDays(courierId, year, month);
		
		List<AttendanceSalaryEntity> list = new ArrayList<AttendanceSalaryEntity>();
		int realAttendanceDays = 0;
		//对于该月的每一天，计算快递员的考勤记录
		DateTime iteration = firstDayLastMonth;
		while(iteration.isBefore(firstDayThisMonth)){
			String calDate = iteration.toString("yyyy-MM-dd");
			
			//首次打卡上班的记录
			Map<String, Object> firstPunchOndutyRecord = attendanceService.getFirstPunchOndutyRecord(courierId, calDate, ondutyRecords);
			//首次打卡下班的记录
			Map<String, Object> firstPunchOffdutyRecord = attendanceService.getFirstPunchOffdutyRecord(courierId, calDate, offdutyRecords);
			
			//有打卡上班的记录
			if(firstPunchOndutyRecord != null){
				//没有打卡下班的记录
				if(firstPunchOffdutyRecord == null){
					firstPunchOffdutyRecord = new HashMap<String, Object>();
					firstPunchOffdutyRecord.put("courierId", courierId);
					firstPunchOffdutyRecord.put("date", calDate);
					firstPunchOffdutyRecord.put("punchTime", calDate + " 23:59:59.0");
					firstPunchOffdutyRecord.put("type", 1);
					firstPunchOffdutyRecord.put("address", firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString());
				}
				
				double attendanceSalary = 0.0;
				double workhours = attendanceService.calPerDayWorkhours(courierId, calDate, firstPunchOndutyRecord, firstPunchOffdutyRecord);
				if(workhours >= lowestWorkHours){
					attendanceSalary = salary/requiredAttendanceDays;
					realAttendanceDays++;
				}
				
				AttendanceSalaryEntity entity = new AttendanceSalaryEntity();
				entity.setCourierId(courierId);
				entity.setAttendanceDate(calDate);
				entity.setStartTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(firstPunchOndutyRecord.get("punchTime").toString()).toDate());
				entity.setEndTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(firstPunchOffdutyRecord.get("punchTime").toString()).toDate());
				entity.setOndutyAddress(firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString());
				entity.setOffdutyAddress(firstPunchOffdutyRecord.get("address") == null?"": firstPunchOffdutyRecord.get("address").toString());
				entity.setWorkingHours(workhours);
				entity.setDaySalary(attendanceSalary);

				list.add(entity);
			}
			else {
				AttendanceSalaryEntity entity = new AttendanceSalaryEntity();
				entity.setCourierId(courierId);
				entity.setAttendanceDate(calDate);
				entity.setStartTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(calDate + " 00:00:00.0").toDate());
				entity.setEndTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(calDate + " 00:00:00.0").toDate());
				entity.setOndutyAddress("");
				entity.setOffdutyAddress("");
				entity.setWorkingHours(0.0);
				entity.setDaySalary(0.0);

				list.add(entity);
			}
			
			iteration = iteration.plusDays(1);
		}
		
		batchSave(list);
		
		courierSalaryService.saveRealAttendanceDays(courierId, realAttendanceDays, year, month);
		String key = CacheKeyUtil.getSalaryConfKey(courierId, year, month);
		AliOcs.remove(key);
		
	}
	
	/**
	 * 获取快递员考勤工资详情
	 * @param courierId
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAttendanceSalary(Integer courierId,
			String date, int page, int rows) {
		
		String key = CacheKeyUtil.getAttendanceSalayRecordKey(courierId, date, page);
		Object object = AliOcs.getObject(key);
		if(object != null){
			return (List<Map<String, Object>>) object;
		}
		else {
			StringBuilder query = new StringBuilder();
			query.append(" select date_format(attendance_date, '%m月%d日 ') attendanceDate, working_hours workingHours, day_salary daySalary" );
			query.append(" from 0085_courier_attendance_salary" );
			query.append(" where courier_id = ? and date_format(attendance_date,'%Y-%m') = ?");
			List<Map<String, Object>> attendanceSalaryList =  findForJdbcParam(query.toString(), page, rows, courierId, date);
			AliOcs.set(key, attendanceSalaryList, 60*60*24);
			return attendanceSalaryList;
		}
		
	}
	
	@Override
	public int getRealAttendanceDaysFromKQ(Integer courierId, String year, String month){;
		double lowestWorkHours = courierSalaryService.getLowestWorkHoursPerDay();
				
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(1) from 0085_courier_attendance_salary ");
		sql.append(" where courier_id = ? and working_hours >= ? and date_format(attendance_date, '%Y-%m')=?");
		
		return findOneForJdbc(sql.toString(), Integer.class, 
				new Object[]{courierId, lowestWorkHours, year+"-"+month});
	}
	
	@Override
	public void statAttendancePerDay(Integer courierId, String date){
		Map<String, Object> firstPunchOndutyRecord = attendanceService.getFirstPunchOndutyRecord(courierId, date);
		Map<String, Object> firstPunchOffdutyRecord = attendanceService.getFirstPunchOffdutyRecord(courierId, date);
		
		//有打卡上班的记录
		if(firstPunchOndutyRecord != null){
			//没有打卡下班的记录
			if(firstPunchOffdutyRecord == null){
				firstPunchOffdutyRecord = new HashMap<String, Object>();
				firstPunchOffdutyRecord.put("courierId", courierId);
				firstPunchOffdutyRecord.put("date", date);
				firstPunchOffdutyRecord.put("punchTime", date + " 23:59:59.0");
				firstPunchOffdutyRecord.put("type", 1);
				firstPunchOffdutyRecord.put("address", firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString());
			}
			
			double attendanceSalary = 0.0;
			double workhours = attendanceService.calPerDayWorkhours(courierId, date, firstPunchOndutyRecord, firstPunchOffdutyRecord);
			save(courierId, date, 
					firstPunchOndutyRecord.get("punchTime").toString(), 
					firstPunchOffdutyRecord.get("punchTime").toString(),
					firstPunchOndutyRecord.get("address") == null?"": firstPunchOndutyRecord.get("address").toString(),
					firstPunchOffdutyRecord.get("address") == null?"": firstPunchOffdutyRecord.get("address").toString(),
					workhours, attendanceSalary);
		}
		else {
			save(courierId, date, 
					date + " 00:00:00.0", 
					date + " 00:00:00.0",
					"",
					"",
					0.0, 0.0);
		}
		
	}
	
	@Override
	public void updateAttendanceSalaryPerday(Integer courierId, String year, String month){
		StringBuilder sql = new StringBuilder(" update 0085_courier_attendance_salary set day_salary = ?");
		sql.append(" where courier_id = ? and date_format(attendance_date, '%Y-%m')=? ");
		sql.append(" and working_hours >= ?");
		double lowestWorkhour = courierSalaryService.getLowestWorkHoursPerDay();
		double salary = courierSalaryService.getSalary(courierId, year, month);
		double salaryPerDay = salary / lowestWorkhour;
		this.executeSql(sql.toString(), new Object[]{salaryPerDay, courierId, year+"-"+month, salaryPerDay});
		
		
		StringBuilder sql2 = new StringBuilder(" update 0085_courier_attendance_salary set day_salary = ?");
		sql2.append(" where courier_id = ? and date_format(attendance_date, '%Y-%m')=? ");
		sql2.append(" and working_hours < ?");
		this.executeSql(sql2.toString(), new Object[]{salaryPerDay, courierId, year+"-"+month, 0.0});
		
	}

}
