package com.wm.entity.merchant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商家会员折扣修改日志表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tom_merchant_member_discount_log")
public class TomMerchantMemberDiscountLogEntity implements Serializable {
	//主键
	private Integer id;
	//商家id
	private Integer merchantId;
	//商家会员折扣
	private Integer merchantDiscount;
	//成为会员最低充值金额(单位:分)
	private Integer minRecharge;
	//操作人员ID
	private Integer userId;
	//商家会员总人数
	private Integer members;
	//商家会员总余额
	private Integer sumMoney;
	//更新时间
	private Date updateTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name ="merchant_id",nullable=false,length=11)
	public Integer getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	@Column(name ="merchant_discount",nullable=false,length=3)
	public Integer getMerchantDiscount() {
		return merchantDiscount;
	}
	
	public void setMerchantDiscount(Integer merchantDiscount) {
		this.merchantDiscount = merchantDiscount;
	}
	
	@Column(name ="min_recharge",nullable=false,length=11)
	public Integer getMinRecharge() {
		return minRecharge;
	}
	
	public void setMinRecharge(Integer minRecharge) {
		this.minRecharge = minRecharge;
	}
	
	@Column(name ="user_id",nullable=true)
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name ="members",nullable=true)
	public Integer getMembers() {
		return members;
	}
	
	public void setMembers(Integer members) {
		this.members = members;
	}
	
	@Column(name ="sum_money",nullable=true)
	public Integer getSumMoney() {
		return sumMoney;
	}
	
	public void setSumMoney(Integer sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	@Column(name ="update_time",nullable=true)
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
