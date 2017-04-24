package com.wm.entity.flash;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 闪购订单表
 * @author db
 * @date 2016-05-12
 */

@Entity
@Table(name="flash_order", schema="")
@SuppressWarnings("serial")
public class FlashOrderEntity implements Serializable {

	/** 主键ID */
	private Long id;
	/** 订单总金额 */
	private Double orderAmount;
	/** 支付ID */
	private String payId;
	/** 用户ID */
	private Integer userId;
	/** 创建时间 */
	private Timestamp createTime;
	/** 支付状态 unpay:为支付 ,pay:支付成功,pay_fail */
	private String status;
	/** 抵用积分 */
	private Integer score;
	/** 打折价格 */
	private Integer discountedPrice;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",nullable=false,precision=20,scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="order_amount")
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	@Column(name="pay_id")
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="score")
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Column(name="discounted_price")
	public Integer getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Integer discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	
}
