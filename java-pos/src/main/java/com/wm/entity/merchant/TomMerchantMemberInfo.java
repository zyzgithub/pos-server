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
 * 商家会员信息表
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tom_merchant_member_info")
public class TomMerchantMemberInfo implements Serializable {
	//主键
	private Integer id;
	//商家id
	private Integer merchantId;
	//用户id
	private Integer userId;
	//会员卡余额(单位：分)
	private Integer money;
	//入会时间
	private Date createTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
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
	
	@Column(name ="user_id",nullable=false,length=11)
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name ="money",nullable=false,length=11)
	public Integer getMoney() {
		return money;
	}
	
	public void setMoney(Integer money) {
		this.money = money;
	}

	@Column(name ="create_time",nullable=true)
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
