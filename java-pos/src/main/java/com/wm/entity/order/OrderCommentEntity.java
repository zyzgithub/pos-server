package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单的评论Entity
 */
@Entity
@Table(name = "0085_order_comment", schema = "")
@SuppressWarnings("serial")
public class OrderCommentEntity implements java.io.Serializable {
	/** id */
	private java.lang.Integer id;
	private java.lang.Integer orderId;
	/**
	 * 评价对象主键
	 */
	private java.lang.Integer commentTargetId;
	// 评论者
	private java.lang.Integer userId;
	/**
	 * 评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 */
	private java.lang.Integer commentTarget;

	/**
	 * 评分：1~5分
	 */
	private java.lang.Integer grade;
	/**
	 * 是否删除，0-否，1-是
	 */
	private java.lang.Integer invalid = 0;

	/** commentContent 评价内容 */
	private java.lang.String commentContent;
	/** commentDisplay 是否显示， S为系统自动确认的时候生成， Y,N为人为，Y为显示，N为不显示 */
	private java.lang.String commentDisplay = "Y";
	/** commentTime */
	private java.lang.Integer commentTime;

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 20, scale = 0)
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer commentTime
	 */
	@Column(name = "COMMENT_TIME", nullable = false, precision = 10, scale = 0)
	public java.lang.Integer getCommentTime() {
		return this.commentTime;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer commentTime
	 */
	public void setCommentTime(java.lang.Integer commentTime) {
		this.commentTime = commentTime;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String commentContent
	 */
	@Column(name = "COMMENT_CONTENT", nullable = false, length = 65535)
	public java.lang.String getCommentContent() {
		return this.commentContent;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String commentContent
	 */
	public void setCommentContent(java.lang.String commentContent) {
		this.commentContent = commentContent;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String commentDisplay
	 */
	@Column(name = "COMMENT_DISPLAY", nullable = false, length = 1)
	public java.lang.String getCommentDisplay() {
		return this.commentDisplay;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String commentDisplay
	 */
	public void setCommentDisplay(java.lang.String commentDisplay) {
		this.commentDisplay = commentDisplay;
	}


	@Column(name = "ORDER_ID", nullable = false )
	public java.lang.Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name = "COMMENT_TARGET_ID", nullable = false)
	public java.lang.Integer getCommentTargetId() {
		return commentTargetId;
	}

	public void setCommentTargetId(java.lang.Integer commentTargetId) {
		this.commentTargetId = commentTargetId;
	}


	@Column(name = "USER_ID", nullable = false)
	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}


	@Column(name = "COMMENT_TARGET", nullable = false)
	public java.lang.Integer getCommentTarget() {
		return commentTarget;
	}

	public void setCommentTarget(java.lang.Integer commentTarget) {
		this.commentTarget = commentTarget;
	}


	@Column(name = "GRADE", nullable = true)
	public java.lang.Integer getGrade() {
		return grade;
	}

	public void setGrade(java.lang.Integer grade) {
		this.grade = grade;
	}

	@Column(name = "INVALID", nullable = true)
	public java.lang.Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(java.lang.Integer invalid) {
		this.invalid = invalid;
	}
}
