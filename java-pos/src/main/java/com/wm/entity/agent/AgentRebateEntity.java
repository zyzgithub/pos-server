package com.wm.entity.agent;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "agent_rebate", schema = "")
@SuppressWarnings("serial")
public class AgentRebateEntity implements java.io.Serializable {
	
	private Integer id;
	private Integer userId;
	private BigDecimal points;
	private BigDecimal rebate;
	private String type;
	private Integer incomeDate;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
	public Integer getId() {
		return id;
	}
	
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	
	@Column(name="points")
	public BigDecimal getPoints() {
		return points;
	}
	
	@Column(name="rebate")
	public BigDecimal getRebate() {
		return rebate;
	}
	
	@Column(name="type")
	public String getType() {
		return type;
	}
	
	@Column(name="income_date")
	public Integer getIncomeDate() {
		return incomeDate;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setPoints(BigDecimal points) {
		this.points = points;
	}
	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}
	public void setType(String type) {
		this.type = type;
	}

	public void setIncomeDate(Integer incomeDate) {
		this.incomeDate = incomeDate;
	}
	
	
}
