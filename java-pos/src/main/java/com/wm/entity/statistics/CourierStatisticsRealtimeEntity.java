package com.wm.entity.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员统计表(实时)
 * @author wuyong
 * @date 2015-08-29 15:17:05
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_statistics_realtime", schema = "")
@SuppressWarnings("serial")
public class CourierStatisticsRealtimeEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**快递员ID*/
	private java.lang.Integer courierId;
	/**累计送单量*/
	private java.lang.Integer totalOrder = 0;
	/**累计评论次数*/
	private java.lang.Integer totalComment = 0;
	/**累计评分*/
	private java.lang.Integer totalCommentScore = 0;
	/**累计提成*/
	private java.lang.Double totalDeduct = 0.0;
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
	 *@return: java.lang.Integer  累计送单量
	 */
	@Column(name ="TOTAL_ORDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalOrder(){
		return this.totalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计送单量
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
	 *@return: java.lang.Integer  累计提成
	 */
	@Column(name ="TOTAL_DEDUCT",nullable=true,precision=10,scale=0)
	public java.lang.Double getTotalDeduct(){
		return this.totalDeduct;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  累计提成
	 */
	public void setTotalDeduct(java.lang.Double totalDeduct){
		this.totalDeduct = totalDeduct;
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
