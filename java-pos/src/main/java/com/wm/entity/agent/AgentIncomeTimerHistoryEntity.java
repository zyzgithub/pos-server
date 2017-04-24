package com.wm.entity.agent;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: agent_income_timer_history
 * @author jiangpingmei
 * @date 2016-03-26 15:41:11
 * @version V1.0   
 *
 */
@Entity
@Table(name = "agent_income_timer_history", schema = "")
@SuppressWarnings("serial")
public class AgentIncomeTimerHistoryEntity implements java.io.Serializable {
	/** id */
	private Integer id;
	/** 订单id */
	private Integer orderId;
	/** 订单金额(单位：元) */
	private Double orderMoney;
	/** 结算金额(单位：元) */
	private Double income;
	/** 创建时间 */
	private Integer createTime;
	/** 结算时间 */
	private Integer payTime;
	/** unpay为未到账，pay为到账，cancel为取消 */
	private String payState;
	/** 结算对象 */
	private Integer userId;
	/** 结算类型   1 返点   2 扣点 */
	private Integer type;
	/** user_id的下一级代理商的user_id */
	private Integer subUserId;
	/** 当前分润率，1.00相当于1.00% */
	private BigDecimal rate;
	/** 分润率类型，对应当前商家扣点类型 A、B */
	private String rateType;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column( name="order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column( name="order_money")
	public Double getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(Double orderMoney) {
		this.orderMoney = orderMoney;
	}
	
	@Column( name="income")
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	
	@Column( name="create_time")
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
	@Column( name="pay_time")
	public Integer getPayTime() {
		return payTime;
	}
	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}
	
	@Column( name="pay_state")
	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	
	@Column( name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column( name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column( name = "sub_user_id")
	public Integer getSubUserId() {
		return subUserId;
	}
	public void setSubUserId(Integer subUserId) {
		this.subUserId = subUserId;
	}
	
	@Column( name = "rate")
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	@Column( name = "rate_type")
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
}
