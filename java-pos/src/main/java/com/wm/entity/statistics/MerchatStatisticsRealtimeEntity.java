package com.wm.entity.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家统计表(实时)
 * @author wuyong
 * @date 2015-08-29 15:15:49
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_statistics_realtime", schema = "")
@SuppressWarnings("serial")
public class MerchatStatisticsRealtimeEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**商家ID*/
	private java.lang.Integer merchantId;
	/**累计订单数*/
	private java.lang.Integer totalOrder = 0;
	/**累计评论次数*/
	private java.lang.Integer totalComment = 0;
	/**累计评分*/
	private java.lang.Integer totalCommentScore = 0;
	/**累计销售量*/
	private java.lang.Integer totalSaledQuantity = 0;
	/**累计销售额*/
	private java.lang.Double totalSaledMoney = 0.0;
	/**最后更新时间*/
	private java.lang.Integer updateTime;
	
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
	 *@return: java.lang.Integer  累计订单数
	 */
	@Column(name ="TOTAL_ORDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalOrder(){
		return this.totalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计订单数
	 */
	public void setTotalOrder(java.lang.Integer totalOrder){
		this.totalOrder = totalOrder;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  累计评论次数
	 */
	@Column(name ="TOTAL_COMMENT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalComment(){
		return this.totalComment;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计评论次数
	 */
	public void setTotalComment(java.lang.Integer totalComment){
		this.totalComment = totalComment;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  累计评分
	 */
	@Column(name ="TOTAL_COMMENT_SCORE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalCommentScore(){
		return this.totalCommentScore;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计评分
	 */
	public void setTotalCommentScore(java.lang.Integer totalCommentScore){
		this.totalCommentScore = totalCommentScore;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  累计销售量
	 */
	@Column(name ="TOTAL_SALED_QUANTITY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalSaledQuantity(){
		return this.totalSaledQuantity;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计销售量
	 */
	public void setTotalSaledQuantity(java.lang.Integer totalSaledQuantity){
		this.totalSaledQuantity = totalSaledQuantity;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  累计销售额
	 */
	@Column(name ="TOTAL_SALED_MONEY",nullable=true,precision=6,scale=2)
	public java.lang.Double getTotalSaledMoney(){
		return this.totalSaledMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  累计销售额
	 */
	public void setTotalSaledMoney(java.lang.Double totalSaledMoney){
		this.totalSaledMoney = totalSaledMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  最后更新时间
	 */
	@Column(name ="UPDATE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  最后更新时间
	 */
	public void setUpdateTime(java.lang.Integer updateTime){
		this.updateTime = updateTime;
	}
}
