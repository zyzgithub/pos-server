package com.wm.service.provider.impl;

import java.util.ArrayList;
import java.util.List;

import com.wm.controller.takeout.vo.OrderMenuVo;

public class OrderDetailVo {
	private Integer id;
	private String merchantName;

	// details
	private List<OrderMenuVo> menuList = new ArrayList<OrderMenuVo>();

	private Double origin;// 总价:

	private Double deliveryFee;// 总价:

	private String orderNum;// 订单号:

	private String realname;// 联系人:
	private String mobile; // 电话:
	private String ifCourier;// 订单类型:
	private String timeTemark; // 配送时间:
	private String address;// 地址:
	private String state;// 订单状态：unpay未支付，pay支付成功，accept制作中，done待评价，confirm
							// 已完成，refund 退款 delivery 配送中，delivery_done配送完成'

	private String merchantPhone;
	private String merchantAddress;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<OrderMenuVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<OrderMenuVo> menuList) {
		this.menuList = menuList;
	}

	public Double getOrigin() {
		return origin;
	}

	public void setOrigin(Double origin) {
		this.origin = origin;
	}

	public Double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIfCourier() {
		return ifCourier;
	}

	public void setIfCourier(String ifCourier) {
		this.ifCourier = ifCourier;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getTimeTemark() {
		return timeTemark;
	}

	public void setTimeTemark(String timeTemark) {
		this.timeTemark = timeTemark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMerchantPhone() {
		return merchantPhone;
	}

	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

}
