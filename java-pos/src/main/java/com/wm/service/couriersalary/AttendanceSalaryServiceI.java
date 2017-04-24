package com.wm.service.couriersalary;

import java.util.List;
import java.util.Map;


public interface AttendanceSalaryServiceI {
	void save(Integer courierId, String attendanceDate, String startTime, String endTime, 
			String ondutyAddress, String offdutyAddress, double workHours, double daySalary);
	
	/**
	 * 计算快递员的考勤工资
	 * @param courierId
	 * @param year
	 * @param month
	 */
	void calcAndSaveAttendanceSalary(Integer courierId, String year, String month);
	
	
	/**
	 * 获取快递员考勤工资详情
	 * @param courierId
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> getAttendanceSalary(Integer courierId, String date, int page, int rows);
	
	/**
	 * 从考勤表中获取快递的实际考勤天数
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	int getRealAttendanceDaysFromKQ(Integer courierId, String year, String month);
	
	/**
	 * 统计快递员每天的考勤记录
	 * @param courierId 快递员ID
	 * @param date 计算日期
	 */
	void statAttendancePerDay(Integer courierId, String date);
	
	/**
	 * 更新快递员每天的考勤工资
	 * @param courierId
	 * @param year
	 * @param month
	 */
	void updateAttendanceSalaryPerday(Integer courierId, String year, String month);
}
