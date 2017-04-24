package com.wm.controller.order.dto;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jeecgframework.core.util.DateUtils;

import com.base.enums.OrderStateEnum;

public class OrderFromMerchantDTO {

	@NotNull
	private Integer merchantId;
	@NotEmpty
	private String realname;
	@NotEmpty
	private String mobile;
	@NotEmpty
	private String address;
	@NotEmpty
	private String timeRemark;
	@NotEmpty
	private String params;
	private String title;//备注
	
	private String invoice="";
	private String orderType = "mobile";
	private Integer saleType = 1;
	
	private String state = "unpay";
	private String fromType = "db";
	
	private Integer floor;
	private Integer buildingId;
	private String buildingName;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimeRemark() {
		return timeRemark;
	}

	public void setTimeRemark(String timeRemark) {
		this.timeRemark = timeRemark;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getInvoice() {
		return invoice;
	}
	
	public void setInvoice(String invoice) {
		this.invoice = invoice;
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
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getFromType() {
		return fromType;
	}
	
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		if(null != floor && !floor.equals(0))
			this.floor = floor;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		if(null != buildingId && !buildingId.equals(0))
			this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		if(!StringUtils.isEmpty(buildingName))
			this.buildingName = buildingName;
	}

	public String getPayState() {
		if(OrderStateEnum.PAY.getOrderStateEn().equals(state))
			return OrderStateEnum.PAY.getOrderStateEn();
		else
			return OrderStateEnum.UNPAY.getOrderStateEn();
	}
	
	public int getPayTime() {
		if(OrderStateEnum.PAY.getOrderStateEn().equals(state))
			return DateUtils.getSeconds();
		else
			return 0;
	}
	
}
