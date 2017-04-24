package com.wm.service.attendance;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.takeout.vo.AttendanceVo;
import com.wm.util.PageList;

public interface AttendanceServiceI extends CommonService{
	
	List<AttendanceVo> queryByUserId(int userId,String startTime,String endTime, int page, int rows);
	
	/**
	 * 判断用户是否是上班状态
	 * @param userId
	 * @return
	 */
	public boolean isOnDuty(Integer userId);
	/**
	 * 根据考勤记录表字段查询考勤记录分页列表
	 * 
	 * @param attendance
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageList findByEntityList(AttendanceVo attendance,Integer page,Integer rows);
	
	/**
	 * 获取快递员某一段时间的考勤记录
	 * @param courierId 快递员ID
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param type 考勤类型 0 打卡上班  1 打卡下班 null 查询所有
	 * @return
	 */
	public List<Map<String, Object>> getAttendceRecords(Integer courierId, String startDate, String endDate, Integer type);
	
	/**
	 * 获取快递员某一天的考勤记录
	 * @param courierId 快递员ID
	 * @param calDate 计算日期
	 * @param type 考勤类型 0 打卡上班  1 打卡下班 null 查询所有
	 * @return
	 */
	public List<Map<String, Object>> getAttendceRecords(Integer courierId, String calDate, Integer type);
	
	
	/**
	 * 获取快递员首次打卡上班记录
	 * @param courierId
	 * @param calDate
	 * @param ondutyRecords 
	 * @return
	 */
	public Map<String, Object> getFirstPunchOndutyRecord(Integer courierId, String calDate, 
			List<Map<String, Object>> ondutyRecords);
	
	/**
	 * 获取快递员首次打卡上班记录
	 * @param courierId
	 * @param calDate
	 * @return
	 */
	public Map<String, Object> getFirstPunchOndutyRecord(Integer courierId, String calDate);
	
	/**
	 * 获取快递员首次打卡下班记录
	 * @param courierId
	 * @param calDate
	 * @return
	 */
	public Map<String, Object> getFirstPunchOffdutyRecord(Integer courierId, String calDate);
	/**
	 * 获取快递员首次打卡下班记录
	 * @param courierId
	 * @param calDate
	 * @param offdutyRecords 
	 * @return
	 */
	public Map<String, Object> getFirstPunchOffdutyRecord(Integer courierId, String calDate, 
			List<Map<String, Object>> offdutyRecords);
	
	
	/**
	 * 计算快递员的工作时间
	 * @param courierId 快递员ID
	 * @param calDate 计算日期
	 * @return
	 */
	public double calPerDayWorkhours(Integer courierId, String calDate);
	
	
	/**
	 * 计算快递员的工作时间
	 * @param courierId
	 * @param calDate
	 * @param firstPunchOnduty
	 * @param firstPunchOffduty
	 * @return
	 */
	public double calPerDayWorkhours(Integer courierId, String calDate, Map<String, Object> firstPunchOnduty, Map<String, Object> firstPunchOffduty);
	
	/**
	 * 计算快递员出勤的天数
	 * @param courierId
	 * @param startDate
	 * @param endDate 
	 * @return
	 */
	public int calAttendanceDays(Integer courierId, String startDate, String endDate);
	
	/**
	 * 获取快递员第一次打卡时间
	 * @param courierId
	 * @param time
	 * @return
	 */
	public Map<String, Object> getFirstAttendanceTime(Integer courierId, String time );

	/**
	 * 获取指定日期打卡的快递员ID列表
	 * @return
	 */
	public List<Integer> getPunchCourierIds(String date);
	
	/**
	 * 判断是否在打卡范围内  true在发卡范围， false不在
	 * @param courierId
	 * @param lon1
	 * @param lat1
	 * @return
	 */
	public boolean isInTheRange(int courierId, double lon1, double lat1);
}
