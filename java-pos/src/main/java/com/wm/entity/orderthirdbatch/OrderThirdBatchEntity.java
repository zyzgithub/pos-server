package com.wm.entity.orderthirdbatch;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_third_batch", schema = "")
@SuppressWarnings("serial")
public class OrderThirdBatchEntity implements Serializable{
	private  static final long serialVersionUID = -4698105615729612997L;
	private Long id;
	private String orderId;//交易流水号
	private String bizId;//第三方业务ID
	private Integer payStatus;//0-未支付1-已支付
	private Integer callbackStatus;//0-未回调 1-回调成功
	private Long createTime;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "order_id")
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Column(name = "biz_id")
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	@Column(name = "pay_status")
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	@Column(name = "callback_status")
	public Integer getCallbackStatus() {
		return callbackStatus;
	}
	public void setCallbackStatus(Integer callbackStatus) {
		this.callbackStatus = callbackStatus;
	}
	@Column(name = "create_time")
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
}