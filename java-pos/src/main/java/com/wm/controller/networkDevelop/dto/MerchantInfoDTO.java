package com.wm.controller.networkDevelop.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class MerchantInfoDTO {

	@NotNull
	private Integer courierId;
	
	private int id;
	/**门店数量*/
	@NotEmpty
	private String shopTotalAmount;
	/**店铺租金*/
	@NotEmpty
	private String shopRent;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getShopTotalAmount() {
		return shopTotalAmount;
	}
	public void setShopTotalAmount(String shopTotalAmount) {
		this.shopTotalAmount = shopTotalAmount;
	}
	public String getShopRent() {
		return shopRent;
	}
	public void setShopRent(String shopRent) {
		this.shopRent = shopRent;
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
}
