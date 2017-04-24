package com.wm.entity.coupons;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author huangzhibin 
 * 用户领取优惠券记录表
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "0085_coupons_user")
public class CouponsUserEntity implements Serializable {

	//用户领取优惠券id
	private Integer id;
	//用户领取优惠券填写的手机号码
	//（所有领取优惠都需要跟手机号绑定，在我的页面的优惠券列表根据用户表中手机号字段关联
	private String userMobile;
	//用户领取该优惠券的数量
	private Integer couponsNum;
	//领取优惠券的可使用金额，单位：分
	private Integer couponsMoney;
	//使用该优惠券应满足最低订单金额，单位：分
	private Integer couponsLimitMoney;
	//优惠券序列号
	private String couponsSerial;
	//优惠券类型
	private String couponsType;
	//优惠券使用期限，单位：秒,0代表不限
	private Integer endTime;
	//领取优惠券的时间，单位：秒
	private Integer createTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="user_mobile")
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	@Column(name="coupons_num")
	public Integer getCouponsNum() {
		return couponsNum;
	}

	public void setCouponsNum(Integer couponsNum) {
		this.couponsNum = couponsNum;
	}

	@Column(name="coupons_money")
	public Integer getCouponsMoney() {
		return couponsMoney;
	}

	public void setCouponsMoney(Integer couponsMoney) {
		this.couponsMoney = couponsMoney;
	}

	@Column(name="coupons_limit_money")
	public Integer getCouponsLimitMoney() {
		return couponsLimitMoney;
	}

	public void setCouponsLimitMoney(Integer couponsLimitMoney) {
		this.couponsLimitMoney = couponsLimitMoney;
	}

	@Column(name="coupons_serial")
	public String getCouponsSerial() {
		return couponsSerial;
	}

	public void setCouponsSerial(String couponsSerial) {
		this.couponsSerial = couponsSerial;
	}

	@Column(name="coupons_type")
	public String getCouponsType() {
		return couponsType;
	}

	public void setCouponsType(String couponsType) {
		this.couponsType = couponsType;
	}

	@Column(name="end_time")
	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	@Column(name="create_time")
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
	@Transient
	public Date getEndDate() {
		return new Date(getEndTime()*1000L);
	}

}
