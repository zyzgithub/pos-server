package com.wm.controller.statistics.vo;

import java.math.BigInteger;
import java.util.Date;

public class AddressOrderStatisticListVo {
	private Integer orderId;
	private String orderNum;
	private String payId;
	private Date createTime;
	private double origin;
	private double onlineMoney;
	private double credit;
	private double scoreMoney;
	private double deliveryFee;
	private String orderType;
	private Integer saleType;
	private String payType;
	private String userName;
	private String mobile;
	private String address;
	private Integer buildingId;
	private String buildingName;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(BigInteger orderId) {
		if(null != orderId)
			this.orderId = orderId.intValue();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public double getOrigin() {
		return origin;
	}

	public void setOrigin(double origin) {
		this.origin = origin;
	}

	public double getOnlineMoney() {
		return onlineMoney;
	}

	public void setOnlineMoney(double onlineMoney) {
		this.onlineMoney = onlineMoney;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getScoreMoney() {
		return scoreMoney;
	}

	public void setScoreMoney(double scoreMoney) {
		this.scoreMoney = scoreMoney;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

}
