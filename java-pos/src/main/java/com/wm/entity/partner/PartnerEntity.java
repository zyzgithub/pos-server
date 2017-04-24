package com.wm.entity.partner;

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
 * @Description: 供应商表
 * @author wuyong
 * @date 2015-09-30 10:41:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_partner", schema = "")
@SuppressWarnings("serial")
public class PartnerEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**第三方标识ID*/
	private java.lang.String openid;
	/**密码*/
	private java.lang.String password;
	/**合作方名称*/
	private java.lang.String name;
	/**注册时间*/
	private java.lang.Integer registerTime;
	/**状态：’valid‘有效,‘invalid’无效*/
	private java.lang.String status;
	/**商家ID*/
	private java.lang.Integer merchantId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  第三方标识ID
	 */
	@Column(name ="OPENID",nullable=false,length=50)
	public java.lang.String getOpenid(){
		return this.openid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  第三方标识ID
	 */
	public void setOpenid(java.lang.String openid){
		this.openid = openid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  密码
	 */
	@Column(name ="PASSWORD",nullable=false,length=50)
	public java.lang.String getPassword(){
		return this.password;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  密码
	 */
	public void setPassword(java.lang.String password){
		this.password = password;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合作方名称
	 */
	@Column(name ="NAME",nullable=false,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合作方名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  注册时间
	 */
	@Column(name ="REGISTER_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getRegisterTime(){
		return this.registerTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  注册时间
	 */
	public void setRegisterTime(java.lang.Integer registerTime){
		this.registerTime = registerTime;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  状态：’valid‘有效,‘invalid’无效
	 */
	@Column(name ="STATUS",nullable=true,length=7)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  状态：’valid‘有效,‘invalid’无效
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家ID
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家ID
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
}
