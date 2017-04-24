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
 * 优惠券
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "0085_coupons")
public class CouponsEntity implements Serializable {

	//优惠券id
	private Integer id;
	//优惠券类型（share 订单分享, push 公众号推送, new 新用户）
	private String type;
	//优惠券优惠金额 单位：分
	private Integer money;
	//使用该优惠券最低金额 单位：分
	private Integer limitMoney;
	//创建时间
	private Integer createTime;
	//优惠券状态
	private String status;
	//优惠券图片
	private String url;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="coupons_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="coupons_money")
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	@Column(name="coupons_limit_money")
	public Integer getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Integer limitMoney) {
		this.limitMoney = limitMoney;
	}

	@Column(name="create_time")
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	@Column(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="coupons_url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
