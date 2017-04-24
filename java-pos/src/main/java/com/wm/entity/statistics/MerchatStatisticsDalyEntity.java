package com.wm.entity.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家统计表(按天)
 * @author wuyong
 * @date 2015-08-29 15:16:21
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_statistics_dayly", schema = "")
@SuppressWarnings("serial")
public class MerchatStatisticsDalyEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**商家ID*/
	private java.lang.Integer merchantId;
	/**日累计订单数*/
	private java.lang.Integer daylyTotalOrder = 0;
	/**日累计评论次数*/
	private java.lang.Integer daylyTotalComment = 0;
	/**日累计评分*/
	private java.lang.Integer daylyTotalCommentScore = 0;
	/**日累计销售量*/
	private java.lang.Integer daylyTotalSaledQuantity = 0;
	/**日累计销售额*/
	private java.lang.Double daylyTotalSaledMoney = 0.0;
	/**日期*/
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日累计订单数
	 */
	@Column(name ="DAYLY_TOTAL_ORDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalOrder(){
		return this.daylyTotalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日累计订单数
	 */
	public void setDaylyTotalOrder(java.lang.Integer daylyTotalOrder){
		this.daylyTotalOrder = daylyTotalOrder;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日累计评论次数
	 */
	@Column(name ="DAYLY_TOTAL_COMMENT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalComment(){
		return this.daylyTotalComment;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日累计评论次数
	 */
	public void setDaylyTotalComment(java.lang.Integer daylyTotalComment){
		this.daylyTotalComment = daylyTotalComment;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日累计评分
	 */
	@Column(name ="DAYLY_TOTAL_COMMENT_SCORE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalCommentScore(){
		return this.daylyTotalCommentScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日累计评分
	 */
	public void setDaylyTotalCommentScore(java.lang.Integer daylyTotalCommentScore){
		this.daylyTotalCommentScore = daylyTotalCommentScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日累计销售量
	 */
	@Column(name ="DAYLY_TOTAL_SALED_QUANTITY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalSaledQuantity(){
		return this.daylyTotalSaledQuantity;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日累计销售量
	 */
	public void setDaylyTotalSaledQuantity(java.lang.Integer daylyTotalSaledQuantity){
		this.daylyTotalSaledQuantity = daylyTotalSaledQuantity;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  日累计销售额
	 */
	@Column(name ="DAYLY_TOTAL_SALED_MONEY",nullable=true,precision=6,scale=2)
	public java.lang.Double getDaylyTotalSaledMoney(){
		return this.daylyTotalSaledMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  日累计销售额
	 */
	public void setDaylyTotalSaledMoney(java.lang.Double daylyTotalSaledMoney){
		this.daylyTotalSaledMoney = daylyTotalSaledMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日期
	 */
	public void setUpdateDate(java.lang.Integer updateDate){
		this.updateDate = updateDate;
	}
}
