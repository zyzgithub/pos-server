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
 * 优惠券发放记录表
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "0085_coupons_publish")
public class CouponsPublishEntity implements Serializable {

	//优惠券发放id
	private Integer id;
	//优惠券发放人id
	private Integer userId;
	//优惠券类型（share 订单分享, push 公众号推送, new 新用户）
	private String couponsType;
	//优惠券序列号（每发放一批，该批次序列号一致）
	private String couponsSerial;
	//优惠券发放时间
	private Integer createTime;
	//该优惠券有效时间（0代表不限）
	private Integer endTime;
	//优惠券发放量（0代表不限）
	private Integer couponsNum;
	//发放的优惠券状态
	private String status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(name="coupons_type")
	public String getCouponsType() {
		return couponsType;
	}

	public void setCouponsType(String couponsType) {
		this.couponsType = couponsType;
	}

	@Column(name="coupons_serial")
	public String getCouponsSerial() {
		return couponsSerial;
	}

	public void setCouponsSerial(String couponsSerial) {
		this.couponsSerial = couponsSerial;
	}

	@Column(name="create_time")
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	@Column(name="end_time")
	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	@Column(name="coupons_num")
	public Integer getCouponsNum() {
		return couponsNum;
	}

	public void setCouponsNum(Integer couponsNum) {
		this.couponsNum = couponsNum;
	}

	@Column(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
