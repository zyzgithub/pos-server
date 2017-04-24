package com.wm.controller.order.dto;

import java.io.Serializable;

public class SuperMarketOrderMenuVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer menuId;  //商品id
	private String name; //商品名称
	private Integer count;//数量
	private double price;//单价
	private double promotionPrice;//促销价格
	private double discountMoney;//优惠金额
	private double total;//总价
	private String salesPromotion;//是否促销
	private String unit = "";//单位
	private String errMsg = "";
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getSalesPromotion() {
		return salesPromotion;
	}
	public void setSalesPromotion(String salesPromotion) {
		this.salesPromotion = salesPromotion;
	}
	public double getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(double discountMoney) {
		this.discountMoney = discountMoney;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
