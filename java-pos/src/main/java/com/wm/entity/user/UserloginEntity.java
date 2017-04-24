package com.wm.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: user_login
 * @author wuyong
 * @date 2015-01-07 10:06:14
 * @version V1.0   
 *
 */
@Entity
@Table(name = "user_login", schema = "")
@SuppressWarnings("serial")
public class UserloginEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**ip*/
	private java.lang.String ip;
	/**loginTime*/
	private java.lang.Integer loginTime;
	/**channel*/
	private java.lang.String channel;
	
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
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ip
	 */
	@Column(name ="IP",nullable=false,length=100)
	public java.lang.String getIp(){
		return this.ip;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ip
	 */
	public void setIp(java.lang.String ip){
		this.ip = ip;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  loginTime
	 */
	@Column(name ="LOGIN_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getLoginTime(){
		return this.loginTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  loginTime
	 */
	public void setLoginTime(java.lang.Integer loginTime){
		this.loginTime = loginTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  channel
	 */
	@Column(name ="CHANNEL",nullable=true,length=7)
	public java.lang.String getChannel(){
		return this.channel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  channel
	 */
	public void setChannel(java.lang.String channel){
		this.channel = channel;
	}
}
