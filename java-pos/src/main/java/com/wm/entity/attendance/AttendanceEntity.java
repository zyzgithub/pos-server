package com.wm.entity.attendance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 考勤记录表
 * @author wuyong
 * @date 2015-08-25 15:00:11
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_attendance", schema = "")
@SuppressWarnings("serial")
public class AttendanceEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**用户ID*/
	private java.lang.Integer userId;
	/**经度*/
	private BigDecimal longitude;
	/**纬度*/
	private BigDecimal latitude;
	/**考勤类型：0-上班，1-下班*/
	private java.lang.Integer type;
	/**考勤时间*/
	private java.lang.Integer createTime;
	/**考勤位置*/
	private java.lang.String address;
	/**打卡设备号*/
	private java.lang.String deviceInfo;	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
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
	 *@return: java.lang.Integer  用户ID
	 */
	@Column(name ="USER_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户ID
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  经度
	 */
	@Column(name ="LONGITUDE",nullable=false,precision=15,scale=6)
	public BigDecimal getLongitude(){
		return this.longitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  经度
	 */
	public void setLongitude(BigDecimal longitude){
		this.longitude = longitude;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  纬度
	 */
	@Column(name ="LATITUDE",nullable=false,precision=15,scale=6)
	public BigDecimal getLatitude(){
		return this.latitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  纬度
	 */
	public void setLatitude(BigDecimal latitude){
		this.latitude = latitude;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  考勤类型：0-上班，1-下班
	 */
	@Column(name ="TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  考勤类型：0-上班，1-下班
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  考勤时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  考勤时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  考勤位置
	 */
	@Column(name ="ADDRESS",nullable=true,length=100)
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  考勤位置
	 */
	public void setAddress(java.lang.String address){
		this.address = address;
	}

	@Column(name ="DEVICE_INFO",nullable=true,length=100)
	public java.lang.String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(java.lang.String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	
}
