package com.wm.entity.couriersalary;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员工资表
 * @author wuyong
 * @date 2016-02-22 11:54:29
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_salary", schema = "")
@SuppressWarnings("serial")
public class CourierSalaryEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**年*/
	private java.lang.String year;
	/**月*/
	private java.lang.String month;
	/**用户id*/
	private java.lang.Integer userId;
	/**身份证号码*/
	private java.lang.String idCard;
	/**name*/
	private java.lang.String name;
	/**center*/
	private java.lang.String center;
	/**department*/
	private java.lang.String department;
	/**position*/
	private java.lang.String position;
	/**入职时间*/
	private java.lang.String jobStartTime;
	/**离职时间*/
	private java.lang.String quitTime;
	/**工资级别*/
	private java.lang.Double salary;
	/**基本工资*/
	private java.lang.Double basicSalary;
	/**绩效工资*/
	private java.lang.Double perfermanceSalary;
	/**应出勤天数*/
	private java.lang.Integer requiredAttendanceDays;
	/**实际出勤天数*/
	private java.lang.Integer realAttendanceDays;
	/**出勤工资*/
	private java.lang.Double attendanceSalary;
	/**业绩提成*/
	private java.lang.Double deduct;
	/**住房补贴*/
	private java.lang.Double lodgingSubsidy;
	/**餐补*/
	private java.lang.Double mealSubsidy;
	/**全勤*/
	private java.lang.Double attendenceReward;
	/**其他补贴*/
	private java.lang.Double otherSupply;
	/**应发工资*/
	private java.lang.Double requiredPaidSarary;
	/**personalSocialInsurance*/
	private java.lang.Double personalSocialInsurance;
	/**个人所得税*/
	private java.lang.Double personalIncomeTax;
	/**其他扣款*/
	private java.lang.Double otherDebit;
	/**实发合计*/
	private java.lang.Double realSalarySum;
	/**平台提成业绩金额合计*/
	private java.lang.Double platformDeductSum;
	/**平台提成业绩金额合计*/
	private java.lang.Double platformDeductUnpay;
	/**realPaySalary*/
	private java.lang.Double realPaySalary;
	/**单位社保*/
	private java.lang.Double companySocialInsurance;
	/**收款人账号*/
	private java.lang.String bankNo;
	/**bankName*/
	private java.lang.String bankName;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  年
	 */
	@Column(name ="YEAR",nullable=false,length=4)
	public java.lang.String getYear(){
		return this.year;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  年
	 */
	public void setYear(java.lang.String year){
		this.year = year;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  月
	 */
	@Column(name ="MONTH",nullable=false,length=2)
	public java.lang.String getMonth(){
		return this.month;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  月
	 */
	public void setMonth(java.lang.String month){
		this.month = month;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  用户id
	 */
	@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证号码
	 */
	@Column(name ="ID_CARD",nullable=false,length=18)
	public java.lang.String getIdCard(){
		return this.idCard;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证号码
	 */
	public void setIdCard(java.lang.String idCard){
		this.idCard = idCard;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  name
	 */
	@Column(name ="NAME",nullable=false,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  name
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  center
	 */
	@Column(name ="CENTER",nullable=false,length=50)
	public java.lang.String getCenter(){
		return this.center;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  center
	 */
	public void setCenter(java.lang.String center){
		this.center = center;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  department
	 */
	@Column(name ="DEPARTMENT",nullable=false,length=50)
	public java.lang.String getDepartment(){
		return this.department;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  department
	 */
	public void setDepartment(java.lang.String department){
		this.department = department;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  position
	 */
	@Column(name ="POSITION",nullable=false,length=50)
	public java.lang.String getPosition(){
		return this.position;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  position
	 */
	public void setPosition(java.lang.String position){
		this.position = position;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入职时间
	 */
	@Column(name ="JOB_START_TIME",nullable=false,length=20)
	public java.lang.String getJobStartTime(){
		return this.jobStartTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入职时间
	 */
	public void setJobStartTime(java.lang.String jobStartTime){
		this.jobStartTime = jobStartTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  离职时间
	 */
	@Column(name ="QUIT_TIME",nullable=false,length=20)
	public java.lang.String getQuitTime(){
		return this.quitTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  离职时间
	 */
	public void setQuitTime(java.lang.String quitTime){
		this.quitTime = quitTime;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工资级别
	 */
	@Column(name ="SALARY",nullable=false,precision=10,scale=2)
	public java.lang.Double getSalary(){
		return this.salary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工资级别
	 */
	public void setSalary(java.lang.Double salary){
		this.salary = salary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  基本工资
	 */
	@Column(name ="BASIC_SALARY",nullable=false,precision=22)
	public java.lang.Double getBasicSalary(){
		return this.basicSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  基本工资
	 */
	public void setBasicSalary(java.lang.Double basicSalary){
		this.basicSalary = basicSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  绩效工资
	 */
	@Column(name ="PERFERMANCE_SALARY",nullable=false,precision=22)
	public java.lang.Double getPerfermanceSalary(){
		return this.perfermanceSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  绩效工资
	 */
	public void setPerfermanceSalary(java.lang.Double perfermanceSalary){
		this.perfermanceSalary = perfermanceSalary;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  应出勤天数
	 */
	@Column(name ="REQUIRED_ATTENDANCE_DAYS",nullable=false,precision=10,scale=0)
	public java.lang.Integer getRequiredAttendanceDays(){
		return this.requiredAttendanceDays;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  应出勤天数
	 */
	public void setRequiredAttendanceDays(java.lang.Integer requiredAttendanceDays){
		this.requiredAttendanceDays = requiredAttendanceDays;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  实际出勤天数
	 */
	@Column(name ="REAL_ATTENDANCE_DAYS",nullable=false,precision=10,scale=0)
	public java.lang.Integer getRealAttendanceDays(){
		return this.realAttendanceDays;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  实际出勤天数
	 */
	public void setRealAttendanceDays(java.lang.Integer realAttendanceDays){
		this.realAttendanceDays = realAttendanceDays;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  出勤工资
	 */
	@Column(name ="ATTENDANCE_SALARY",nullable=false,precision=10,scale=2)
	public java.lang.Double getAttendanceSalary(){
		return this.attendanceSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  出勤工资
	 */
	public void setAttendanceSalary(java.lang.Double attendanceSalary){
		this.attendanceSalary = attendanceSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  业绩提成
	 */
	@Column(name ="DEDUCT",nullable=false,precision=10,scale=2)
	public java.lang.Double getDeduct(){
		return this.deduct;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  业绩提成
	 */
	public void setDeduct(java.lang.Double deduct){
		this.deduct = deduct;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  住房补贴
	 */
	@Column(name ="LODGING_SUBSIDY",nullable=false,precision=10,scale=2)
	public java.lang.Double getLodgingSubsidy(){
		return this.lodgingSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  住房补贴
	 */
	public void setLodgingSubsidy(java.lang.Double lodgingSubsidy){
		this.lodgingSubsidy = lodgingSubsidy;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  餐补
	 */
	@Column(name ="MEAL_SUBSIDY",nullable=false,precision=10,scale=2)
	public java.lang.Double getMealSubsidy(){
		return this.mealSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  餐补
	 */
	public void setMealSubsidy(java.lang.Double mealSubsidy){
		this.mealSubsidy = mealSubsidy;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  全勤
	 */
	@Column(name ="ATTENDENCE_REWARD",nullable=false,precision=10,scale=2)
	public java.lang.Double getAttendenceReward(){
		return this.attendenceReward;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  全勤
	 */
	public void setAttendenceReward(java.lang.Double attendenceReward){
		this.attendenceReward = attendenceReward;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  其他补贴
	 */
	@Column(name ="OTHER_SUPPLY",nullable=false,precision=10,scale=2)
	public java.lang.Double getOtherSupply(){
		return this.otherSupply;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  其他补贴
	 */
	public void setOtherSupply(java.lang.Double otherSupply){
		this.otherSupply = otherSupply;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  应发工资
	 */
	@Column(name ="REQUIRED_PAID_SARARY",nullable=false,precision=10,scale=2)
	public java.lang.Double getRequiredPaidSarary(){
		return this.requiredPaidSarary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  应发工资
	 */
	public void setRequiredPaidSarary(java.lang.Double requiredPaidSarary){
		this.requiredPaidSarary = requiredPaidSarary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  personalSocialInsurance
	 */
	@Column(name ="PERSONAL_SOCIAL_INSURANCE",nullable=false,precision=10,scale=2)
	public java.lang.Double getPersonalSocialInsurance(){
		return this.personalSocialInsurance;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  personalSocialInsurance
	 */
	public void setPersonalSocialInsurance(java.lang.Double personalSocialInsurance){
		this.personalSocialInsurance = personalSocialInsurance;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个人所得税
	 */
	@Column(name ="PERSONAL_INCOME_TAX",nullable=false,precision=10,scale=2)
	public java.lang.Double getPersonalIncomeTax(){
		return this.personalIncomeTax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  个人所得税
	 */
	public void setPersonalIncomeTax(java.lang.Double personalIncomeTax){
		this.personalIncomeTax = personalIncomeTax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  其他扣款
	 */
	@Column(name ="OTHER_DEBIT",nullable=false,precision=10,scale=2)
	public java.lang.Double getOtherDebit(){
		return this.otherDebit;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  其他扣款
	 */
	public void setOtherDebit(java.lang.Double otherDebit){
		this.otherDebit = otherDebit;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  实发合计
	 */
	@Column(name ="REAL_SALARY_SUM",nullable=false,precision=10,scale=2)
	public java.lang.Double getRealSalarySum(){
		return this.realSalarySum;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  实发合计
	 */
	public void setRealSalarySum(java.lang.Double realSalarySum){
		this.realSalarySum = realSalarySum;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  平台提成业绩金额合计
	 */
	@Column(name ="PLATFORM_DEDUCT_SUM",nullable=false,precision=10,scale=2)
	public java.lang.Double getPlatformDeductSum(){
		return this.platformDeductSum;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  平台提成业绩金额合计
	 */
	public void setPlatformDeductSum(java.lang.Double platformDeductSum){
		this.platformDeductSum = platformDeductSum;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  平台提成业绩金额合计
	 */
	@Column(name ="PLATFORM_DEDUCT_UNPAY",nullable=false,precision=10,scale=2)
	public java.lang.Double getPlatformDeductUnpay(){
		return this.platformDeductUnpay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  平台提成业绩金额合计
	 */
	public void setPlatformDeductUnpay(java.lang.Double platformDeductUnpay){
		this.platformDeductUnpay = platformDeductUnpay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  realPaySalary
	 */
	@Column(name ="REAL_PAY_SALARY",nullable=false,precision=10,scale=2)
	public java.lang.Double getRealPaySalary(){
		return this.realPaySalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  realPaySalary
	 */
	public void setRealPaySalary(java.lang.Double realPaySalary){
		this.realPaySalary = realPaySalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  单位社保
	 */
	@Column(name ="COMPANY_SOCIAL_INSURANCE",nullable=false,precision=10,scale=2)
	public java.lang.Double getCompanySocialInsurance(){
		return this.companySocialInsurance;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  单位社保
	 */
	public void setCompanySocialInsurance(java.lang.Double companySocialInsurance){
		this.companySocialInsurance = companySocialInsurance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人账号
	 */
	@Column(name ="BANK_NO",nullable=false,length=20)
	public java.lang.String getBankNo(){
		return this.bankNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人账号
	 */
	public void setBankNo(java.lang.String bankNo){
		this.bankNo = bankNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankName
	 */
	@Column(name ="BANK_NAME",nullable=false,length=255)
	public java.lang.String getBankName(){
		return this.bankName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankName
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
}
