package com.wm.entity.positiongraderule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 岗位变更规则表
 * @author wuyong
 * @date 2015-09-01 16:59:57
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_position_grade_rule", schema = "")
@SuppressWarnings("serial")
public class PositionGradeRuleEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**月单量*/
	private java.lang.Integer totalOrder;
	/**invalid*/
	private java.lang.Integer invalid;
	/**positionId*/
	private java.lang.Integer positionId;
	/**salary*/
	private java.lang.Integer salary;
	
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
	 *@return: java.lang.Integer  月单量
	 */
	@Column(name ="TOTAL_ORDER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTotalOrder(){
		return this.totalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  月单量
	 */
	public void setTotalOrder(java.lang.Integer totalOrder){
		this.totalOrder = totalOrder;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  invalid
	 */
	@Column(name ="INVALID",nullable=true,precision=3,scale=0)
	public java.lang.Integer getInvalid(){
		return this.invalid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  invalid
	 */
	public void setInvalid(java.lang.Integer invalid){
		this.invalid = invalid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  positionId
	 */
	@Column(name ="POSITION_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPositionId(){
		return this.positionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  positionId
	 */
	public void setPositionId(java.lang.Integer positionId){
		this.positionId = positionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  salary
	 */
	@Column(name ="SALARY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSalary(){
		return this.salary;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  salary
	 */
	public void setSalary(java.lang.Integer salary){
		this.salary = salary;
	}
}
