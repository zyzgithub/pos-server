package com.wm.entity.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "dine_in_discount_log", schema = "")
@SuppressWarnings("serial")
public class DineInDiscountLogEntity implements java.io.Serializable {
	
	/***/
	private Integer id;
	/***/
	private Integer orderId;
	/**原价，单位为分，1元=100*/
	private Integer originMoney;
	/**折后价，单位为分，1元=100*/
	private Integer onlineMoney;
	/**折扣百分比，1=1%*/
	private Integer discount;
	/**折扣金额，减了多少钱，单位为分，1元=100*/
	private Integer discountMoney;
	/***/
	private Date createTime;
	/**商家id,关联merchant.id*/
	private Integer merchantId;
	/**用户id,关联user.id*/
	private Integer userId;
	/**商家平摊，1=1%*/
	private Integer merchantSharePercent;
	/**平台分摊比例，如1=1%*/
	private Integer platformSharePercent;
	/**uppay表示该订单未支付,pay表示该订单已支付*/
	private String payState;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name="origin_money")
	public Integer getOriginMoney() {
		return originMoney;
	}

	public void setOriginMoney(Integer originMoney) {
		this.originMoney = originMoney;
	}

	@Column(name="online_money")
	public Integer getOnlineMoney() {
		return onlineMoney;
	}

	public void setOnlineMoney(Integer onlineMoney) {
		this.onlineMoney = onlineMoney;
	}

	@Column(name="discount")
	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	@Column(name="discount_money")
	public Integer getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(Integer discountMoney) {
		this.discountMoney = discountMoney;
	}

	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Column(name="merchant_share_percent")
	public Integer getMerchantSharePercent() {
		return merchantSharePercent;
	}

	public void setMerchantSharePercent(Integer merchantSharePercent) {
		this.merchantSharePercent = merchantSharePercent;
	}

	@Column(name="platform_share_percent")
	public Integer getPlatformSharePercent() {
		return platformSharePercent;
	}

	public void setPlatformSharePercent(Integer platformSharePercent) {
		this.platformSharePercent = platformSharePercent;
	}

	@Column(name="pay_state")
	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}
	
	
}
