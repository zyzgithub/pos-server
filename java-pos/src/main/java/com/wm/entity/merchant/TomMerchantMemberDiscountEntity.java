package com.wm.entity.merchant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商家会员折扣表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tom_merchant_member_discount")
public class TomMerchantMemberDiscountEntity implements Serializable{
	//主键
	private Integer id;
	//商家id
	private Integer merchantId;
	//商家会员折扣
	private Integer merchantDiscount;
	//成为会员最低充值金额(单位:分)
	private Integer minRecharge;
	
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
}
