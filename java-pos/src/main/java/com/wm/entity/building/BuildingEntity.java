package com.wm.entity.building;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Title: Entity
 * @Description: 外卖楼层表
 * @author wuyong
 * @date 2015-08-13 10:23:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "0085_building", schema = "")
@SuppressWarnings("serial")
public class BuildingEntity implements java.io.Serializable{
	
	/** id */
	private java.lang.Integer id;
	/** 大厦所属区域id */
	private java.lang.Integer regionId = 0;
	/** 大厦名称 */
	private java.lang.String name;
	/** 负责起始楼层 */
	private java.lang.Integer firstFloor;
	/** 负责终止楼层 */
	private java.lang.Integer lastFloor;
	/** 经度 */
	private java.lang.Double lng;
	/** 纬度 */
	private java.lang.Double lat;
	/** 大厦地址 */
	private java.lang.String address;
	
	//是否删除，默认为0=不删除
	private java.lang.Integer isDelete = 0;
	
	//0085_org.id(最小的网点的id), 默认没有关联的网点
	private java.lang.Integer orgId = 0;
	
	/**
	 * 与某坐标点之间的距离
	 */
	private java.lang.Double distance; 
	

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 负责起始楼层
	 */
	@Column(name = "FIRST_FLOOR", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getFirstFloor() {
		return this.firstFloor;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer 负责起始楼层
	 */
	public void setFirstFloor(java.lang.Integer firstFloor) {
		this.firstFloor = firstFloor;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 负责终止楼层
	 */
	@Column(name = "LAST_FLOOR", nullable = true, precision = 10, scale = 0)
	public java.lang.Integer getLastFloor() {
		return this.lastFloor;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer 负责终止楼层
	 */
	public void setLastFloor(java.lang.Integer lastFloor) {
		this.lastFloor = lastFloor;
	}

	@Column(name = "longitude" , nullable = false)
	public java.lang.Double getLng() {
		return lng;
	}

	public void setLng(java.lang.Double lng) {
		this.lng = lng;
	}

	@Column(name = "latitude" , nullable = false)
	public java.lang.Double getLat() {
		return lat;
	}

	public void setLat(java.lang.Double lat) {
		this.lat = lat;
	}

	@Column(name = "REGION_ID", nullable = false)
	public java.lang.Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(java.lang.Integer regionId) {
		this.regionId = regionId;
	}

	@Column(name = "NAME")
	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Column(name = "ADDRESS")
	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	@Column(name = "IS_DELETE")
	public java.lang.Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(java.lang.Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "ORG_ID" , nullable= false)
	public java.lang.Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}

	@Transient
	public java.lang.Double getDistance() {
		return distance;
	}

	public void setDistance(java.lang.Double distance) {
		this.distance = distance;
	}
	
}
