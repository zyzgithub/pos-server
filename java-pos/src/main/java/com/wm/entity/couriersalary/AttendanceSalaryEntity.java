package com.wm.entity.couriersalary;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**   
 * @Title: Entity
 * @Description: 快递员考勤工资表
 * @author wuyong
 * @date 2016-02-22 11:50:01
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_attendance_salary", schema = "")
@SuppressWarnings("serial")
public class AttendanceSalaryEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**快递员ID*/
	private java.lang.Integer courierId;
	/**日期*/
	private String attendanceDate;
	/**上班开始时间*/
	private java.util.Date startTime;
	/**上班结束时间*/
	private java.util.Date endTime;
	/**上班地址*/
	private java.lang.String ondutyAddress;
	/**下班地址*/
	private java.lang.String offdutyAddress;
	/**工作时长（单位小时）*/
	private java.lang.Double workingHours;
	/**daySalary*/
	private java.lang.Double daySalary;
	/**备注*/
	private java.lang.String remark;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员ID
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员ID
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	@Column(name ="ATTENDANCE_DATE",nullable=false)
	public String getAttendanceDate(){
		return this.attendanceDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setAttendanceDate(String attendanceDate){
		this.attendanceDate = attendanceDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  上班开始时间
	 */
	@Column(name ="START_TIME",nullable=false)
	public java.util.Date getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  上班开始时间
	 */
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  上班结束时间
	 */
	@Column(name ="END_TIME",nullable=false)
	public java.util.Date getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  上班结束时间
	 */
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工作时长（单位小时）
	 */
	@Column(name ="WORKING_HOURS",nullable=false,precision=22)
	public java.lang.Double getWorkingHours(){
		return this.workingHours;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工作时长（单位小时）
	 */
	public void setWorkingHours(java.lang.Double workingHours){
		this.workingHours = workingHours;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  daySalary
	 */
	@Column(name ="DAY_SALARY",nullable=true,precision=5,scale=2)
	public java.lang.Double getDaySalary(){
		return this.daySalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  daySalary
	 */
	public void setDaySalary(java.lang.Double daySalary){
		this.daySalary = daySalary;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="REMARK",nullable=true,length=200)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}

	@Column(name ="ONDUTY_ADDRESS")
	public java.lang.String getOndutyAddress() {
		return ondutyAddress;
	}

	public void setOndutyAddress(java.lang.String ondutyAddress) {
		this.ondutyAddress = ondutyAddress;
	}

	@Column(name ="OFFDUTY_ADDRESS")
	public java.lang.String getOffdutyAddress() {
		return offdutyAddress;
	}

	public void setOffdutyAddress(java.lang.String offdutyAddress) {
		this.offdutyAddress = offdutyAddress;
	}
}
