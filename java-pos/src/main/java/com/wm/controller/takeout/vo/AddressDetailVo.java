package com.wm.controller.takeout.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AddressDetailVo {

	private Integer id;
	private Integer userId;
	private String name;
	private String mobile;
	private Integer buildId;
	private Integer buildFloor;
	private String buildName;
	private String addressDetail;
	private Double lng;
	private Double lat;

	public Integer getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id.intValue();
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public Integer getBuildFloor() {
		return buildFloor;
	}

	public void setBuildFloor(Integer buildFloor) {
		this.buildFloor = buildFloor;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		if(null != lng)
			this.lng = lng.doubleValue();
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		if(null != lat)
			this.lat = lat.doubleValue();
	}

}
