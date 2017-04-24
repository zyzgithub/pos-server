package com.wm.entity.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: address
 * @author wuyong
 * @date 2015-01-07 09:41:11
 * @version V1.0   
 *
 */
@Entity
@Table(name = "address", schema = "")
@SuppressWarnings("serial")
public class AddressEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**用户id*/
	//private WUserEntity wuser;
	private java.lang.Integer userId;
	/**城市*/
	private java.lang.String city;
	/**详细地址*/
	private java.lang.String addressDetail;
	/**收货人名称*/
	private java.lang.String name;
	/**收货人手机号码*/
	private java.lang.String mobile;
	/**是否设定为默认地址*/
	private java.lang.String isDefault;
	/**地理位置（经纬度）*/
	private java.lang.String location;
	/**创建时间*/
	private java.util.Date createTime;

	/**大厦id*/
	private java.lang.Integer buildingId;

	/**大厦楼层*/
	private java.lang.Integer buildingFloor;

	/**性别，male-m，female-f*/
	private java.lang.String sex;
	
	/**大厦名*/
	private java.lang.String buildingName;
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  用户id
	 */
	@Column(name ="USER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}*/
	@Column(name ="CITY",nullable=false,length=20)
	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  详细地址
	 */
	@Column(name ="ADDRESS_DETAIL",nullable=false,length=255)
	public java.lang.String getAddressDetail(){
		return this.addressDetail;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  详细地址
	 */
	public void setAddressDetail(java.lang.String addressDetail){
		this.addressDetail = addressDetail;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人名称
	 */
	@Column(name ="NAME",nullable=false,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人手机号码
	 */
	@Column(name ="MOBILE",nullable=false,length=16)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人手机号码
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否设定为默认地址
	 */
	@Column(name ="is_default",nullable=false,length=1)
	public java.lang.String getIsDefault() {
		return isDefault;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否设定为默认地址
	 */
	public void setIsDefault(java.lang.String isDefault) {
		this.isDefault = isDefault;
	}
	

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地理位置（经纬度）
	 */
	@Column(name ="LOCATION",nullable=false,length=50)
	public java.lang.String getLocation(){
		return this.location;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地理位置（经纬度）
	 */
	public void setLocation(java.lang.String location){
		this.location = location;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	@Column(name ="BUILDING_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(java.lang.Integer buildingId) {
		this.buildingId = buildingId;
	}

	@Column(name ="BUILDING_FLOOR",nullable=true,precision=10,scale=0)
	public java.lang.Integer getBuildingFloor() {
		return buildingFloor;
	}
	
	public void setBuildingFloor(java.lang.Integer buildingFloor) {
		this.buildingFloor = buildingFloor;
	}

	@Column(name ="SEX",nullable=true)
	public java.lang.String getSex() {
		return sex;
	}
	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}

	@Column(name ="BUILDING_NAME",nullable=true)
	public java.lang.String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(java.lang.String buildingName) {
		this.buildingName = buildingName;
	}
	
}
