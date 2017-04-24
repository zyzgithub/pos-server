package com.wm.controller.networkDevelop.dto;

import java.util.List;
import java.util.Map;

public class NetworkDevelopDTO {
	private Integer id;
	/**社区名称*/
	private String communityName;
	/**住户数量*/
	private int household;
	/**2房1厅租金*/
	private double rent;
	/**房价*/
	private double housePrice;
	/**经度*/
	private String longitude;
	/**纬度*/
	private String latitude;
	/**门店数量*/
	private int shopTotalAmount;
	/**店铺租金*/
	private double shopRent;
	/**饿了么进驻数量*/
	private int elmAmount;
	/**美团外卖进驻数量*/
	private int meituanAmount;
	/**百度外卖进驻数量*/
	private int baiduAmount;
	/**口碑外卖进驻数量*/
	private int koubeiAmount;
	/**其他外卖进驻数量*/
	private int otherAmount;
	/**出单量/单天*/
	private int orderAmount;
	/**业务员id*/
	private int courierId;
	/**社区地址*/
	private String communityAddress;
	
	private List<Map<String, Object>> shopList;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public int getHousehold() {
		return household;
	}

	public void setHousehold(int household) {
		this.household = household;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public double getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(double housePrice) {
		this.housePrice = housePrice;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getShopTotalAmount() {
		return shopTotalAmount;
	}

	public void setShopTotalAmount(int shopTotalAmount) {
		this.shopTotalAmount = shopTotalAmount;
	}

	public double getShopRent() {
		return shopRent;
	}

	public void setShopRent(double shopRent) {
		this.shopRent = shopRent;
	}

	public int getElmAmount() {
		return elmAmount;
	}

	public void setElmAmount(int elmAmount) {
		this.elmAmount = elmAmount;
	}

	public int getMeituanAmount() {
		return meituanAmount;
	}

	public void setMeituanAmount(int meituanAmount) {
		this.meituanAmount = meituanAmount;
	}

	public int getBaiduAmount() {
		return baiduAmount;
	}

	public void setBaiduAmount(int baiduAmount) {
		this.baiduAmount = baiduAmount;
	}

	public int getKoubeiAmount() {
		return koubeiAmount;
	}

	public void setKoubeiAmount(int koubeiAmount) {
		this.koubeiAmount = koubeiAmount;
	}

	public int getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(int otherAmount) {
		this.otherAmount = otherAmount;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public List<Map<String, Object>> getShopList() {
		return shopList;
	}

	public void setShopList(List<Map<String, Object>> shopList) {
		this.shopList = shopList;
	}

	public String getCommunityAddress() {
		return communityAddress;
	}

	public void setCommunityAddress(String communityAddress) {
		this.communityAddress = communityAddress;
	}
}
