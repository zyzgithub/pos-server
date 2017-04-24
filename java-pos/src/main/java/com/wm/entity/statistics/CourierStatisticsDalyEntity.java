package com.wm.entity.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员统计表(按天)
 * @author wuyong
 * @date 2015-08-29 15:16:45
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_statistics_dayly", schema = "")
@SuppressWarnings("serial")
public class CourierStatisticsDalyEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**快递员ID*/
	private java.lang.Integer courierId;
	/**日送单量*/
	private java.lang.Integer daylyTotalOrder = 0;
	/**日累计评论次数*/
	private java.lang.Integer daylyTotalComment = 0;
	/**日累计评分*/
	private java.lang.Integer daylyTotalCommentScore = 0;
	/**日累计提成*/
	private java.lang.Double daylyTotalDeduct = 0.0;
	/**日期*/
	private java.lang.Integer updateDate;
	
	/**日累计送餐份数*/
	private Integer dalyTotalQuantity = 0;
	
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
	 *@return: java.lang.Integer  快递员ID
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员ID
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日送单量
	 */
	@Column(name ="DAYLY_TOTAL_ORDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalOrder(){
		return this.daylyTotalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日送单量
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
	 *@return: java.lang.Integer  日累计提成
	 */
	@Column(name ="DAYLY_TOTAL_DEDUCT",nullable=true,precision=10,scale=0)
	public java.lang.Double getDaylyTotalDeduct(){
		return this.daylyTotalDeduct;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日累计提成
	 */
	public void setDaylyTotalDeduct(java.lang.Double daylyTotalDeduct){
		this.daylyTotalDeduct = daylyTotalDeduct;
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

	@Column(name ="DAYLY_TOTAL_QUANTITY",nullable=true,precision=10,scale=0)
	public Integer getDalyTotalQuantity() {
		return dalyTotalQuantity;
	}

	public void setDalyTotalQuantity(Integer dalyTotalQuantity) {
		this.dalyTotalQuantity = dalyTotalQuantity;
	}
	
	
}
