package com.wm.entity.couriersalarylog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员调薪记录表
 * @author wuyong
 * @date 2015-09-01 17:05:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_salary_log", schema = "")
@SuppressWarnings("serial")
public class CourierSalaryLogEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**courierId*/
	private java.lang.Integer courierId;
	/**oldSalary*/
	private java.lang.Integer oldSalary;
	/**newSalary*/
	private java.lang.Integer newSalary;
	/**changeTime*/
	private java.lang.Integer changeTime;
	/**调薪操作人ID，0代表系统自动调*/
	private java.lang.Integer changeOperator;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  courierId
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  courierId
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  oldSalary
	 */
	@Column(name ="OLD_SALARY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOldSalary(){
		return this.oldSalary;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  oldSalary
	 */
	public void setOldSalary(java.lang.Integer oldSalary){
		this.oldSalary = oldSalary;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  newSalary
	 */
	@Column(name ="NEW_SALARY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getNewSalary(){
		return this.newSalary;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  newSalary
	 */
	public void setNewSalary(java.lang.Integer newSalary){
		this.newSalary = newSalary;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  changeTime
	 */
	@Column(name ="CHANGE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getChangeTime(){
		return this.changeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  changeTime
	 */
	public void setChangeTime(java.lang.Integer changeTime){
		this.changeTime = changeTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  调薪操作人ID，0代表系统自动调
	 */
	@Column(name ="CHANGE_OPERATOR",nullable=true,precision=10,scale=0)
	public java.lang.Integer getChangeOperator(){
		return this.changeOperator;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  调薪操作人ID，0代表系统自动调
	 */
	public void setChangeOperator(java.lang.Integer changeOperator){
		this.changeOperator = changeOperator;
	}
}
