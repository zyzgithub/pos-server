package com.wm.entity.version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 客户端版本管理
 * @author wuyong
 * @date 2015-08-11 13:09:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "version_manager", schema = "")
@SuppressWarnings("serial")
public class VersionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Double id;
	/**当前版本号*/
	private java.lang.Double currentVersion;
	/**兼容的最小版本号*/
	private java.lang.Double minVersion;
	/**版本描述*/
	private java.lang.String message;
	/**版本下载地址*/
	private java.lang.String downloadUrl;
	/**版本发布时间*/
	private java.util.Date time;
	/**版本类型*/
	private java.lang.String type;
	/**是否强制升级*/
	private Integer forceUpdate;
	
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=22)
	public java.lang.Double getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  id
	 */
	public void setId(java.lang.Double id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  当前版本号
	 */
	@Column(name ="CURRENT_VERSION",nullable=true,precision=22)
	public java.lang.Double getCurrentVersion(){
		return this.currentVersion;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  当前版本号
	 */
	public void setCurrentVersion(java.lang.Double currentVersion){
		this.currentVersion = currentVersion;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  兼容的最小版本号
	 */
	@Column(name ="MIN_VERSION",nullable=true,precision=22)
	public java.lang.Double getMinVersion(){
		return this.minVersion;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  兼容的最小版本号
	 */
	public void setMinVersion(java.lang.Double minVersion){
		this.minVersion = minVersion;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  版本描述
	 */
	@Column(name ="MESSAGE",nullable=true,length=500)
	public java.lang.String getMessage(){
		return this.message;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  版本描述
	 */
	public void setMessage(java.lang.String message){
		this.message = message;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  版本下载地址
	 */
	@Column(name ="DOWNLOAD_URL",nullable=true,length=300)
	public java.lang.String getDownloadUrl(){
		return this.downloadUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  版本下载地址
	 */
	public void setDownloadUrl(java.lang.String downloadUrl){
		this.downloadUrl = downloadUrl;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  版本发布时间
	 */
	@Column(name ="TIME",nullable=true)
	public java.util.Date getTime(){
		return this.time;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  版本发布时间
	 */
	public void setTime(java.util.Date time){
		this.time = time;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  版本类型
	 */
	@Column(name ="TYPE",nullable=true,length=1)
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  版本类型
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}

	@Column(name ="FORCEUPDATE",length=1)
	public Integer getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(Integer forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	
}
