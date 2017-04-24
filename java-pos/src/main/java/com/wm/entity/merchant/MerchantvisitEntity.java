package com.wm.entity.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: merchant_visit
 * @author wuyong
 * @date 2015-01-07 10:00:16
 * @version V1.0   
 *
 */
@Entity
@Table(name = "merchant_visit", schema = "")
@SuppressWarnings("serial")
public class MerchantvisitEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**merchantId*/
	private MerchantEntity merchant;
	//private java.lang.Integer merchantId;
	/**visitedTime*/
	private java.lang.Integer visitedTime;
	
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
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  merchantId
	 */
	/*@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  merchantId
	 */
	/*public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MERCHANT_ID", nullable = true)
	public MerchantEntity getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantEntity merchant) {
		this.merchant = merchant;
	}
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  visitedTime
	 */
	@Column(name ="VISITED_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getVisitedTime(){
		return this.visitedTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  visitedTime
	 */
	public void setVisitedTime(java.lang.Integer visitedTime){
		this.visitedTime = visitedTime;
	}
}
