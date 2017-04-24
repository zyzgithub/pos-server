package com.wm.entity.coupons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author huangzhibin
 * 用户使用优惠券记录
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "0085_coupons_user_log")
public class CouponsUserLogEntity implements Serializable {

	//用户使用优惠券id
	private Integer id;
	//用户id
	private Integer userId;
	//订单id
	private Integer orderId;
	//用户领取优惠券id
	private Integer couponsUserId;
	//使用优惠券时间
	private Integer useTime;
	//使用说明
	private String detail;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="user_id")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name="coupons_user_id")
	public Integer getCouponsUserId() {
		return couponsUserId;
	}

	public void setCouponsUserId(Integer couponsUserId) {
		this.couponsUserId = couponsUserId;
	}

	@Column(name="use_time")
	public Integer getUseTime() {
		return useTime;
	}

	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}

	@Column(name="detail")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
