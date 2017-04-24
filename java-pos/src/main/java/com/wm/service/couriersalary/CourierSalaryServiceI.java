package com.wm.service.couriersalary;

import java.util.List;
import java.util.Map;

import com.wm.service.impl.couriersalary.ex.CalcSalaryException;

public interface CourierSalaryServiceI {
	
	/**
	 * 获取待计算工资的快递员id列表
	 * @param year
	 * @param month
	 * @return
	 */
	List<Integer> getCourierIdsOfRequriedCalcSalary(String year, String month);
	
	/**
	 * 获取每天最低工作小时数
	 * @return
	 */
	double getLowestWorkHoursPerDay();
	
	/**
	 * 获取快递员工资记录
	 * @param courierId
	 * @return
	 */
	Map<String, Object> getCourierSalaryRecord(Integer courierId, String year, String month);
	
	/**
	 * 获取快递员应出勤的天数
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	Integer getRequiredAttendanceDays(Integer courierId, String year, String month);
	
	/**
	 * 获取快递员实际出勤的天数
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	int getRealAttendanceDays(Integer courierId, String year, String month);
	
	/**
	 * 获取快递员的工资
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	Double getSalary(Integer courierId, String year, String month);
	
	/**
	 * 保存快递员实际有效的考勤天数
	 * @param courierId
	 * @param year
	 * @param month
	 * @param realAttendanceDays
	 */
	void saveRealAttendanceDays(Integer courierId, int realAttendanceDays, String year, String month);
	
	/**
	 * 计算并保存快递员实际工资
	 * @param courierId
	 * @param year
	 * @param month
	 * @throws 
	 */
	void calcAndSaveCourierRealSalary(Integer courierId, String year, String month) 
			throws CalcSalaryException;
	
	/**
	 * 计算所有快递员的工资
	 * @param year
	 * @param month
	 */
	void calcAllCourierSalary(String year, String month);
	
	/**
	 * 获取快递员上月收入
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> getTotalIncome(Integer courierId, String year, String month);
	
	/**
	 * 获取快递员补贴和奖金详情
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> getSubsidyAndReward(Integer courierId, String year, String month);
	
	/**
	 * 获取其他详情
	 * @param courierId
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> getOther(Integer courierId, String year, String month);
	
	/**
	 * 清空缓存
	 * @param courierId
	 * @param year
	 * @param month
	 */
	public void cleanSalaryCache(Integer courierId, String year, String month);
}
