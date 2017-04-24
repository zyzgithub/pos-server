package com.wm.controller.building.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class BuildingDTO implements Serializable {
	private static final long serialVersionUID = 5252714952221047907L;
	/** id */
	private java.lang.Integer id;
	@NotNull
	@Min(value = 1)
	private Integer courierId;
	/** 大厦所属区域id */
	private java.lang.Integer regionId = 0;
	/** 大厦名称 */
	@NotEmpty
	private java.lang.String name;
	/** 负责起始楼层 */
	@NotNull
	private java.lang.Integer firstFloor;
	/** 负责终止楼层 */
	@NotNull
	@Min(value = 999)
	private java.lang.Integer lastFloor;
	/** 经度 */
	@NotNull
	private java.lang.Double lng;
	/** 纬度 */
	@NotNull
	private java.lang.Double lat;
	/** 大厦地址 */
	@NotEmpty
	private java.lang.String address;

	//是否删除，默认为0=不删除
	private java.lang.Integer isDelete = 0;
	
	//0085_org.id(最小的网点的id), 默认没有关联的网点
	private java.lang.Integer orgId = 0;
	
	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getRegionId() {
		if(regionId == null){
			regionId = 0;
		}
		return regionId;
	}

	public void setRegionId(java.lang.Integer regionId) {
		this.regionId = regionId;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Integer getFirstFloor() {
		return firstFloor;
	}

	public void setFirstFloor(java.lang.Integer firstFloor) {
		this.firstFloor = firstFloor;
	}

	public java.lang.Integer getLastFloor() {
		return lastFloor;
	}

	public void setLastFloor(java.lang.Integer lastFloor) {
		this.lastFloor = lastFloor;
	}

	public java.lang.Double getLng() {
		return lng;
	}

	public void setLng(java.lang.Double lng) {
		this.lng = lng;
	}

	public java.lang.Double getLat() {
		return lat;
	}

	public void setLat(java.lang.Double lat) {
		this.lat = lat;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.Integer getIsDelete() {
		if(isDelete == null){
			isDelete = 0;
		}
		return isDelete;
	}

	public void setIsDelete(java.lang.Integer isDelete) {
		this.isDelete = isDelete;
	}

	public java.lang.Integer getOrgId() {
		if(orgId == null){
			orgId = 0;
		}
		return orgId;
	}

	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getCourierId() {
		return courierId;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

}
