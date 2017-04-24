package com.wm.controller.takeout.vo;

import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class MenuVo {

	private int id;
	private String name;
	private double price;
	private String picUrl;
	private int buyCount;
	private int typeId;
	private int repertory;
	
	private Integer promotion;//(商品促销id)
	private Date overHour;//促销时间
	private Integer limitCount;//每人限购
	private Integer promotionCount;//促销总数
	private Double money;//促销价

	public int getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id.intValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		if(isPromoting())
			return money;
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPicUrl() {
		return picUrl;
	}
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(BigInteger buyCount) {
		this.buyCount = buyCount.intValue();
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(BigInteger typeId) {
		this.typeId = typeId.intValue();
	}
	
	public Date getOverHour() {
		return overHour;
	}
	
	public void setOverHour(Date overHour) {
		this.overHour = overHour;
	}

	public Integer getLimitCount() {
		if(null != limitCount && limitCount > 0)
			return limitCount;
		return 0;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
	
	public void setPromotionCount(Integer promotionCount) {
		this.promotionCount = promotionCount;
	}

	public Integer getPromotionCount() {
		return promotionCount;
	}
	
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getPromotion() {
		return promotion;
	}
	
	public void setPromotion(BigInteger promotion) {
		if(null != promotion)
			this.promotion = promotion.intValue();
	}
	
	public void setRepertory(int repertory) {
		this.repertory = repertory;
	}
	
	public int getRepertory() {
		if(isPromoting())
			return getPromotionCount();
		return repertory;
	}
	
	public String getProDesc() {
		if(getRepertory() > 0 && getRepertory() <20)
			return "剩余"+getRepertory()+"份";
		return StringUtils.EMPTY;
	}
	
	public boolean isPromoting() {
		if(null != getPromotion())
			return true;
		return false;
	}
}
