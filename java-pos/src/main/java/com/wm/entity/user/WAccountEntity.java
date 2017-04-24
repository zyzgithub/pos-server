package com.wm.entity.user;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account", schema = "")
@SuppressWarnings("serial")
public class WAccountEntity implements java.io.Serializable {

	/**id*/
	private java.lang.Integer id;
	private java.lang.Integer userId;
	/** 账户类型 1:商家用户 2:个人用户 */
	private java.lang.Integer accountType;
	/**money*/
	private BigDecimal balance = new BigDecimal(0);
	/**账户状态0:账户冻结 / 1:正常*/
	private java.lang.Integer status;	
	/**createTime*/
	private java.util.Date createTime = new Date();
	/**last_time*/
	private java.util.Date lastTime = new Date();	

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
	
	@Column(name ="USER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	@Column(name ="ACCOUNT_TYPE",nullable=false,precision=4,scale=0)
	public java.lang.Integer getAccountType() {
		return accountType;
	}
	
	public void setAccountType(java.lang.Integer accountType) {
		this.accountType = accountType;
	}

	@Column(name ="BALANCE",nullable=false,precision=10,scale=2)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Column(name ="STATUS",nullable=false,precision=4,scale=0)
	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	
	@Column(name ="LAST_TIME")
	public java.util.Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(java.util.Date lastTime) {
		this.lastTime = lastTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime() {
		return createTime;
	}

	
	public WAccountEntity(Integer id) {
		super();
		this.id = id;
	}

	public WAccountEntity() {
		super();
	}
	

}
