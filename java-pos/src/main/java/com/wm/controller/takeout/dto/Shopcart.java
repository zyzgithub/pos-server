package com.wm.controller.takeout.dto;

public class Shopcart {
	private Integer merchantId;
	private Integer menuId;
	private String menuName;
	private Double price;
	private Integer count;
	private Integer repertory;
	
	private boolean promoting;
	private Integer promoteId;
	private Integer limitCount;
	
	public Shopcart() {
		
	}

	public Integer getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Double getPromotionPrice() {
		//当是促销的时候，price == promotionPrice
		if(promoting())
			return price;
		return 0.0;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRepertory() {
		return repertory;
	}

	public void setRepertory(Integer repertory) {
		this.repertory = repertory;
	}
	
	public double getTotalPrice() {
		return Math.rint(getPrice()*100)*getCount()/100;
	}
	
	public String getPromote() {
		if(promoting())
			return "Y";
		return "N";
	}

	public boolean isPromoting() {
		return promoting;
	}
	
	public void setPromoting(boolean promoting) {
		this.promoting = promoting;
	}
	
	public void promoting(boolean promoting) {
		this.promoting = promoting;
	}
	
	public boolean promoting() {
		return promoting;
	}
	
	public Integer getPromoteId() {
		return promoteId;
	}
	
	public void setPromoteId(Integer promoteId) {
		this.promoteId = promoteId;
	}
	
	public Integer getLimitCount() {
		return limitCount;
	}
	
	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
}
