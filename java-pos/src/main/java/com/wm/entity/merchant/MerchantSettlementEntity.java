package com.wm.entity.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家日结算统计表
 * @author wuyong
 * @date 2015-09-14 20:11:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_settlement_dayly", schema = "")
@SuppressWarnings("serial")
public class MerchantSettlementEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**商家ID*/
	private java.lang.Integer merchantId;
	/**日订单总金额*/
	private java.lang.Double daylyOrderIncome = 0.0;
	/**日预收入金额*/
	private java.lang.Double daylyPreIncome = 0.0;
	/**结算后商家余额*/
	private java.lang.Double daylyBalance = 0.0;
	/**统计日期*/
	private java.lang.Integer updateDate;
	
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
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  日订单总金额
	 */
	@Column(name ="DAYLY_ORDER_INCOME",nullable=false,precision=22)
	public java.lang.Double getDaylyOrderIncome(){
		return this.daylyOrderIncome;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  日订单总金额
	 */
	public void setDaylyOrderIncome(java.lang.Double daylyOrderIncome){
		this.daylyOrderIncome = daylyOrderIncome;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  日预收入金额
	 */
	@Column(name ="DAYLY_PRE_INCOME",nullable=false,precision=22)
	public java.lang.Double getDaylyPreIncome(){
		return this.daylyPreIncome;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  日预收入金额
	 */
	public void setDaylyPreIncome(java.lang.Double daylyPreIncome){
		this.daylyPreIncome = daylyPreIncome;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  结算后商家余额
	 */
	@Column(name ="DAYLY_BALANCE",nullable=false,precision=22)
	public java.lang.Double getDaylyBalance(){
		return this.daylyBalance;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  结算后商家余额
	 */
	public void setDaylyBalance(java.lang.Double daylyBalance){
		this.daylyBalance = daylyBalance;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  统计日期
	 */
	@Column(name ="UPDATE_DATE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  统计日期
	 */
	public void setUpdateDate(java.lang.Integer updateDate){
		this.updateDate = updateDate;
	}
}
