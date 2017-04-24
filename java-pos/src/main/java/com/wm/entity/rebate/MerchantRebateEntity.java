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
@Table(name = "0085_merchant_rebate", schema = "")
public class MerchantRebateEntity implements Serializable{
	private static final long serialVersionUID = 1833485487566784976L;
	
	private Integer id;
	private Date countDate;
	private Double originSum; 
	private Integer orderCount; 
	private Double rebateSum;  
	private Timestamp grantTime; 
	private Integer grantState; 
	private Integer rebateSetupId;
	private Integer merchantId;
	private Double rebateRate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name ="COUNT_DATE")
	public Date getCountDate() {
		return countDate;
	}
	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
	
	@Column(name ="ORIGIN_SUM")
	public Double getOriginSum() {
		return originSum;
	}
	public void setOriginSum(Double originSum) {
		this.originSum = originSum;
	}
	
	@Column(name ="ORDER_COUNT")
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	@Column(name ="REBATE_SUM")
	public Double getRebateSum() {
		return rebateSum;
	}
	public void setRebateSum(Double rebateSum) {
		this.rebateSum = rebateSum;
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
	
	@Column(name ="REBATE_SETUP_ID")
	public Integer getRebateSetupId() {
		return rebateSetupId;
	}
	public void setRebateSetupId(Integer rebateSetupId) {
		this.rebateSetupId = rebateSetupId;
	}
	
	@Column(name ="MERCHANT_ID")
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	@Column(name ="REBATE_RATE")
	public Double getRebateRate() {
		return rebateRate;
	}
	public void setRebateRate(Double rebateRate) {
		this.rebateRate = rebateRate;
	}
	
	
	
}
