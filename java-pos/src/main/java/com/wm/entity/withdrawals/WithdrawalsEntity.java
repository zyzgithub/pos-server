package com.wm.entity.withdrawals;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: withdrawals
 * @author wuyong
 * @date 2015-01-07 10:06:50
 * @version V1.0   
 *
 */
@Entity
@Table(name = "withdrawals", schema = "")
@SuppressWarnings("serial")
public class WithdrawalsEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**submitTime*/
	private java.lang.Integer submitTime;
	/**完成时间*/
	private java.lang.Integer completeTime;
	/**money*/
	private java.lang.Double money;
	/**state*/
	private java.lang.String state;
	/**提现银行卡*/
	private java.lang.Integer bankcardId;
	/** 银行卡其他信息 */
	private java.lang.String bankInfo;
	/**申请取消时间*/
	private java.lang.Integer cancelTime;
	/**用户类型*/
	private java.lang.String userType = "user";
	
	private java.lang.Integer auditor;
	
	private java.lang.Double beforeMoney;
	
	private java.lang.Double afterMoney;
	
	private Date expectArrivalTime;
	private java.lang.Integer takeMode;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
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
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  submitTime
	 */
	@Column(name ="SUBMIT_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSubmitTime(){
		return this.submitTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  submitTime
	 */
	public void setSubmitTime(java.lang.Integer submitTime){
		this.submitTime = submitTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  完成时间
	 */
	@Column(name ="COMPLETE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCompleteTime(){
		return this.completeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  完成时间
	 */
	public void setCompleteTime(java.lang.Integer completeTime){
		this.completeTime = completeTime;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name ="MONEY",nullable=true,precision=11,scale=2)
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  money
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  state
	 */
	@Column(name ="STATE",nullable=true,length=6)
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  state
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}

	@Column(name="bankcard_id",nullable=true)
	public java.lang.Integer getBankcardId() {
		return bankcardId;
	}

	public void setBankcardId(java.lang.Integer bankcardId) {
		this.bankcardId = bankcardId;
	}
	
	@Column(name="bank_info",nullable=true)
	public java.lang.String getBankInfo() {
		return bankInfo;
	}
	
	public void setBankInfo(java.lang.String bankInfo) {
		this.bankInfo = bankInfo;
	}
	
	@Column(name="cancel_time",nullable=true)
	public java.lang.Integer getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(java.lang.Integer cancelTime) {
		this.cancelTime = cancelTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用户类型'courier'快递员,'merchant'商家,'user'普通用户,'manage'管理员
	 */
	@Column(name ="USER_TYPE",nullable=true,length=8)
	public java.lang.String getUserType(){
		return this.userType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用户类型
	 */
	public void setUserType(java.lang.String userType){
		this.userType = userType;
	}
	@Column(name ="BEFORE_MONEY",nullable=true,precision=11,scale=2)
	public java.lang.Double getBeforeMoney() {
		return beforeMoney;
	}

	public void setBeforeMoney(java.lang.Double beforeMoney) {
		this.beforeMoney = beforeMoney;
	}
	
	@Column(name ="AFTER_MONEY",nullable=true,precision=11,scale=2)
	public java.lang.Double getAfterMoney() {
		return afterMoney;
	}

	public void setAfterMoney(java.lang.Double afterMoney) {
		this.afterMoney = afterMoney;
	}
	
	@Column(name="auditor",nullable=true)
	public java.lang.Integer getAuditor() {
		return auditor;
	}

	public void setAuditor(java.lang.Integer auditor) {
		this.auditor = auditor;
	}
	
	public void setExpectArrivalTime(Date expectArrivalTime) {
		this.expectArrivalTime = expectArrivalTime;
	}

	public void setTakeMode(java.lang.Integer takeMode) {
		this.takeMode = takeMode;
	}

	@Column(name ="expect_arrival_time",nullable=true)
	public Date getExpectArrivalTime() {
		return expectArrivalTime;
	}

	@Column(name ="take_mode",nullable=false)
	public java.lang.Integer getTakeMode() {
		return takeMode;
	}
}