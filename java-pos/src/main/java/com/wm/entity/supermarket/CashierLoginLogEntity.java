package com.wm.entity.supermarket;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 收银员登录日志
 * @author wuyong
 * @date 2016-05-24 16:12:35
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_cashier_login_log", schema = "")
@SuppressWarnings("serial")
public class CashierLoginLogEntity implements java.io.Serializable {
	
	public final static int LOGIN  = 1;		//登录
	public final static int EXIT = 2;		//退出登录
	
	/**主键*/
	private java.lang.Integer id;
	/**商家id*/
	private java.lang.Integer merchantId;
	/**收银员ID*/
	private java.lang.Integer cashierId;
	/**登录姓名*/
	private java.lang.String name;
	/**登录类型 1 登录 2 退出登录*/
	private java.lang.Integer loginType;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**登录唯一标识*/
	private java.lang.String deviceCode;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收银员ID
	 */
	@Column(name ="CASHIER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCashierId(){
		return this.cashierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收银员ID
	 */
	public void setCashierId(java.lang.Integer cashierId){
		this.cashierId = cashierId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  登录姓名
	 */
	@Column(name ="NAME",nullable=false,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  登录姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  登录类型 1 登录 2 退出登录
	 */
	@Column(name ="LOGIN_TYPE",nullable=true,precision=3,scale=0)
	public java.lang.Integer getLoginType(){
		return this.loginType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  登录类型 1 登录 2 退出登录
	 */
	public void setLoginType(java.lang.Integer loginType){
		this.loginType = loginType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}

	@Column(name ="DEVICE_CODE",nullable=true,length=255)
	public java.lang.String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(java.lang.String deviceCode) {
		this.deviceCode = deviceCode;
	}
	
	
}
