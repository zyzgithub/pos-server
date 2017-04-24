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
@Table(name = "0085_courier_rebate", schema = "")
public class CourierRebateEntity implements Serializable{
	private static final long serialVersionUID = 1833485487566784976L;
	
	private Integer id;
	private Date countDate;
	private Double originSum; 
	private Integer orderCount; 
	private Integer courierId;  
	private Double courierRebate;  
	private Integer battalionId;  
	private Double battalionRebate; 
	private Integer delegationId;  
	private Double delegationRebate; 
	private Timestamp grantTime; 
	private Integer grantState; 
	private Integer rebateSetupId;
	
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
	
	@Column(name ="COURIER_ID")
	public Integer getCourierId() {
		return courierId;
	}
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
	@Column(name ="COURIER_REBATE")
	public Double getCourierRebate() {
		return courierRebate;
	}
	public void setCourierRebate(Double courierRebate) {
		this.courierRebate = courierRebate;
	}
	
	@Column(name ="BATTALION_ID")
	public Integer getBattalionId() {
		return battalionId;
	}
	public void setBattalionId(Integer battalionId) {
		this.battalionId = battalionId;
	}
	
	@Column(name ="BATTALION_REBATE")
	public Double getBattalionRebate() {
		return battalionRebate;
	}
	public void setBattalionRebate(Double battalionRebate) {
		this.battalionRebate = battalionRebate;
	}
	
	@Column(name ="DELEGATION_ID")
	public Integer getDelegationId() {
		return delegationId;
	}
	public void setDelegationId(Integer delegationId) {
		this.delegationId = delegationId;
	}
	
	@Column(name ="DELEGATION_REBATE")
	public Double getDelegationRebate() {
		return delegationRebate;
	}
	public void setDelegationRebate(Double delegationRebate) {
		this.delegationRebate = delegationRebate;
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
	
	
	
}
