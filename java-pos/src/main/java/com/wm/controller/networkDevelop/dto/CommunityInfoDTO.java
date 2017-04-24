package com.wm.controller.networkDevelop.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class CommunityInfoDTO {

	private int id;
	@NotNull
	private Integer courierId;
	
	/**社区名称*/
	@NotEmpty
	private String communityName;
	/**住户数量*/
	@NotNull
	private int household;
	/**2房1厅租金*/
	@NotEmpty
	private String rent;
	/**房价*/
	@NotEmpty
	private String housePrice;
	/**经度*/
	@NotEmpty
	private String longitude;
	/**纬度*/
	@NotEmpty
	private String latitude;
	/**社区地址*/
	@NotEmpty
	private String communityAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getRent() {
		return rent;
	}
	public void setRent(String rent) {
		this.rent = rent;
	}
	public String getHousePrice() {
		return housePrice;
	}
	public void setHousePrice(String housePrice) {
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
	public String getCommunityAddress() {
		return communityAddress;
	}
	public void setCommunityAddress(String communityAddress) {
		this.communityAddress = communityAddress;
	}
	public Integer getCourierId() {
		return courierId;
	}
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	
}
