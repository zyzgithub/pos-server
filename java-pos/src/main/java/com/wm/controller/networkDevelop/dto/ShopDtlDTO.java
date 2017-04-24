package com.wm.controller.networkDevelop.dto;

import javax.validation.constraints.NotNull;


public class ShopDtlDTO {
	
	@NotNull
	private Integer courierId;
	
	private int id;
	
	private int type;
	
	private String url;

	/**门店数量*/
	private int shopAmount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getShopAmount() {
		return shopAmount;
	}

	public void setShopAmount(int shopAmount) {
		this.shopAmount = shopAmount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
}
