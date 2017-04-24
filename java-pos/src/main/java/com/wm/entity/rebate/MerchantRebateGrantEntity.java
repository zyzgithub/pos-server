package com.wm.entity.rebate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "0085_merchant_rebate_grant", schema = "")
public class MerchantRebateGrantEntity implements Serializable{
	private static final long serialVersionUID = 1833485487566784976L;
	
	private Integer id;
	private Double rebateMoney; 
	private Double beforeMoney;
	private Double afterMoney;
	private Double actualMoney;  
	private Timestamp grantTime; 
	private Integer grantState; 
	private Integer merchantId;
	private Date statDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name ="GRANT_TIME")
	public Timestamp getGrantTime() {
		return grantTime;
	}
	public void setGrantTime(Timestamp grantTime) {
		this.grantTime = grantTime;
	}
	
	@Column(name ="GRANT_STATE")
	public Integer getGrantState() {
		return grantState;
	}
	public void setGrantState(Integer grantState) {
		this.grantState = grantState;
	}
	
	@Column(name ="MERCHANT_ID")
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	@Column(name ="actual_money")
	public Double getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(Double actualMoney) {
		this.actualMoney = actualMoney;
	}
	
	@Column(name ="rebate_money")
	public Double getRebateMoney() {
		return rebateMoney;
	}
	public void setRebateMoney(Double rebateMoney) {
		this.rebateMoney = rebateMoney;
	}
	
	@Column(name ="before_money")
	public Double getBeforeMoney() {
		return beforeMoney;
	}
	public void setBeforeMoney(Double beforeMoney) {
		this.beforeMoney = beforeMoney;
	}
	
	@Column(name ="after_money")
	public Double getAfterMoney() {
		return afterMoney;
	}
	public void setAfterMoney(Double afterMoney) {
		this.afterMoney = afterMoney;
	}
	
	@Column(name ="STAT_DATE")
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	
}
