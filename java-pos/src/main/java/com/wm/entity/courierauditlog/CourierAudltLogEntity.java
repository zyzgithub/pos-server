package com.wm.entity.courierauditlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: courier_audit_log
 * @author wuyong
 * @date 2015-09-17 09:40:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_audit_log", schema = "")
@SuppressWarnings("serial")
public class CourierAudltLogEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**申请表id*/
	private java.lang.Integer appyId;
	/**审核者，user表id*/
	private java.lang.Integer auditor;
	/**审核时间*/
	private java.lang.Integer updateTime;
	/**审核结果，默认为0=未审核，1=审核通过，2=审核不通过*/
	private java.lang.Integer auditResult;
	/**记录创建时间*/
	private java.lang.Integer createTime;
	/**申请类型，0=管理员后台添加，1=app申请*/
	private java.lang.Integer applyType;
	/**apply_type=0即快递员为后台添加时，该值等于快递员id*/
	private java.lang.Integer courierId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
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
	 *@return: java.lang.Integer  申请表id
	 */
	@Column(name ="APPY_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getAppyId(){
		return this.appyId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请表id
	 */
	public void setAppyId(java.lang.Integer appyId){
		this.appyId = appyId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核者，user表id
	 */
	@Column(name ="AUDITOR",nullable=false,precision=10,scale=0)
	public java.lang.Integer getAuditor(){
		return this.auditor;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核者，user表id
	 */
	public void setAuditor(java.lang.Integer auditor){
		this.auditor = auditor;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核时间
	 */
	@Column(name ="UPDATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核时间
	 */
	public void setUpdateTime(java.lang.Integer updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核结果，默认为0=未审核，1=审核通过，2=审核不通过
	 */
	@Column(name ="AUDIT_RESULT",nullable=false,precision=3,scale=0)
	public java.lang.Integer getAuditResult(){
		return this.auditResult;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核结果，默认为0=未审核，1=审核通过，2=审核不通过
	 */
	public void setAuditResult(java.lang.Integer auditResult){
		this.auditResult = auditResult;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  记录创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  记录创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申请类型，0=管理员后台添加，1=app申请
	 */
	@Column(name ="APPLY_TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getApplyType(){
		return this.applyType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请类型，0=管理员后台添加，1=app申请
	 */
	public void setApplyType(java.lang.Integer applyType){
		this.applyType = applyType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  apply_type=0即快递员为后台添加时，该值等于快递员id
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  apply_type=0即快递员为后台添加时，该值等于快递员id
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
}
