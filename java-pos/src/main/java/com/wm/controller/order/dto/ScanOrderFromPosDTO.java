package com.wm.controller.order.dto;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ScanOrderFromPosDTO {

	@NotNull
	private Integer merchantId;//商家id
	@NotNull
	private Integer cashierId;//收银员id
	@NotNull
	private Double origin;//收款金额
	@NotEmpty
	private String fromType = "pos";//收款来源
	
	private Integer orderId;
	
	private String payId;
	
	private Double totalPrice;
	
	private Integer totalCount;
	
	private List<Map<String, Object>> menuList;
	
	private String merchantName;
 	
	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getFromType() {
		return fromType;
	}
	
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public Integer getCashierId() {
		return cashierId;
	}

	public void setCashierId(Integer cashierId) {
		this.cashierId = cashierId;
	}

	public Double getOrigin() {
		return origin;
	}

	public void setOrigin(Double origin) {
		this.origin = origin;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Map<String, Object>> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Map<String, Object>> menuList) {
		this.menuList = menuList;
	}
	
}
