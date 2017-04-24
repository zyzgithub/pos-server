package com.wm.entity.courieraccount;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员账号封号表
 * @author wuyong
 * @date 2016-02-24 11:57:12
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_account_suspend", schema = "")
@SuppressWarnings("serial")
public class CourierAccountSuspendEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**courierId*/
	private java.lang.Integer courierId;
	/**封号时间*/
	private java.util.Date suspendTime;
	/**封号原因*/
	private java.lang.String suspendReason;
	/**当前状态 1 封号  0 已解封*/
	private java.lang.Integer currentState;
	/**解封时间*/
	private java.util.Date unlockTime;
	/**解封人*/
	private java.lang.Integer unlocker;
	/**remark*/
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
	 *@return: java.lang.Integer  courierId
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=19,scale=0)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  封号时间
	 */
	@Column(name ="SUSPEND_TIME",nullable=false)
	public java.util.Date getSuspendTime(){
		return this.suspendTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  封号时间
	 */
	public void setSuspendTime(java.util.Date suspendTime){
		this.suspendTime = suspendTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  封号原因
	 */
	@Column(name ="SUSPEND_REASON",nullable=true,length=200)
	public java.lang.String getSuspendReason(){
		return this.suspendReason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  封号原因
	 */
	public void setSuspendReason(java.lang.String suspendReason){
		this.suspendReason = suspendReason;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  当前状态 1 封号  0 已解封
	 */
	@Column(name ="CURRENT_STATE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getCurrentState(){
		return this.currentState;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  当前状态 1 封号  0 已解封
	 */
	public void setCurrentState(java.lang.Integer currentState){
		this.currentState = currentState;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  解封时间
	 */
	@Column(name ="UNLOCK_TIME",nullable=true)
	public java.util.Date getUnlockTime(){
		return this.unlockTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  解封时间
	 */
	public void setUnlockTime(java.util.Date unlockTime){
		this.unlockTime = unlockTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  解封人
	 */
	@Column(name ="UNLOCKER",nullable=true,precision=19,scale=0)
	public java.lang.Integer getUnlocker(){
		return this.unlocker;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  解封人
	 */
	public void setUnlocker(java.lang.Integer unlocker){
		this.unlocker = unlocker;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  remark
	 */
	@Column(name ="REMARK",nullable=true,length=500)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  remark
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}
}
