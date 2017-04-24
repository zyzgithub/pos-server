package com.wm.entity.comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 评论/评价
 * @author wuyong
 * @date 2015-08-13 20:20:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_order_comment", schema = "")
@SuppressWarnings("serial")
public class CommentEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**orderId*/
	private java.lang.Integer orderId;
	/**评价内容*/
	private java.lang.String commentContent;
	/**是否显示*/
	private java.lang.String commentDisplay;
	/**评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务*/
	private java.lang.Integer commentTarget;
	/**评分：0~5分*/
	private java.lang.Integer grade;
	/**是否删除，0-否，1-是*/
	private java.lang.Integer invalid;
	/**评价对象主键*/
	private java.lang.Integer commentTargetId;
	/**评论者*/
	private java.lang.Integer userId;
	/**commentTime*/
	private java.lang.Integer commentTime;
	
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
	 *@return: java.lang.Integer  orderId
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  orderId
	 */
	public void setOrderId(java.lang.Integer orderId){
		this.orderId = orderId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  评价内容
	 */
	@Column(name ="COMMENT_CONTENT",nullable=false,length=500)
	public java.lang.String getCommentContent(){
		return this.commentContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  评价内容
	 */
	public void setCommentContent(java.lang.String commentContent){
		this.commentContent = commentContent;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  是否显示
	 */
	@Column(name ="COMMENT_DISPLAY",nullable=false,length=1)
	public java.lang.String getCommentDisplay(){
		if(this.commentDisplay == null){
			this.commentDisplay = "Y";
		}
		return this.commentDisplay;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  是否显示
	 */
	public void setCommentDisplay(java.lang.String commentDisplay){
		this.commentDisplay = commentDisplay;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 */
	@Column(name ="COMMENT_TARGET",nullable=false,precision=3,scale=0)
	public java.lang.Integer getCommentTarget(){
		return this.commentTarget;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 */
	public void setCommentTarget(java.lang.Integer commentTarget){
		this.commentTarget = commentTarget;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评分：0~5分
	 */
	@Column(name ="GRADE",nullable=true,precision=3,scale=0)
	public java.lang.Integer getGrade(){
		return this.grade;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评分：0~5分
	 */
	public void setGrade(java.lang.Integer grade){
		this.grade = grade;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否删除，0-否，1-是
	 */
	@Column(name ="INVALID",nullable=true,precision=3,scale=0)
	public java.lang.Integer getInvalid(){
		return this.invalid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否删除，0-否，1-是
	 */
	public void setInvalid(java.lang.Integer invalid){
		this.invalid = invalid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评价对象主键
	 */
	@Column(name ="COMMENT_TARGET_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCommentTargetId(){
		return this.commentTargetId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评价对象主键
	 */
	public void setCommentTargetId(java.lang.Integer commentTargetId){
		this.commentTargetId = commentTargetId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评论者
	 */
	@Column(name ="USER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评论者
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  commentTime
	 */
	@Column(name ="COMMENT_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCommentTime(){
		if(commentTime == null){
			commentTime = (int) (System.currentTimeMillis()/1000);
		}
		return this.commentTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  commentTime
	 */
	public void setCommentTime(java.lang.Integer commentTime){
		this.commentTime = commentTime;
	}
}
