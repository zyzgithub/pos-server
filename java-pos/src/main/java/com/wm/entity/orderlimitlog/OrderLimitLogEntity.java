package com.wm.entity.orderlimitlog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_limit_log", schema = "")
@SuppressWarnings("serial")
public class OrderLimitLogEntity implements Serializable{
	private  static final long serialVersionUID = 7548214556300330397L;

	private Integer id;
	private Integer merchantId;//商家编号
	private Integer userId;//用户编号
	private Integer createTime;//创建时间
	private String payType;//weixinpay-微信支付 alipay-支付宝支付
	private Integer amount;//金额
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id",nullable=false,precision=20,scale=0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="merchant_id")
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Column(name="create_time")
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	@Column(name="pay_type")
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name="amount")
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}