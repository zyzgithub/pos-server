package com.wm.entity.notice;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 公告
 * @author wuyong
 * @date 2016-01-12 14:38:49
 * @version V1.0   
 *
 */
@Entity
@Table(name = "tsm_notice_user", schema = "")
@SuppressWarnings("serial")
public class NoticeUserEntity implements java.io.Serializable {
	public static final int UN_READ = 0;
	public static final int READED = 1;
	
    private int id;
	
	/** 消息id，对应tsm_notice表的id*/
	private java.lang.Integer noticeId;
	/**用户id，关联user表的id*/
	private java.lang.Integer userId;
	/**阅读状态，0=未读，1=已读*/
	private java.lang.Integer readStatus;
	/**阅读时间*/
	private java.util.Date readTime;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer   消息id，对应tsm_notice表的id
	 */
	@Column(name ="NOTICE_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getNoticeId(){
		return this.noticeId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer   消息id，对应tsm_notice表的id
	 */
	public void setNoticeId(java.lang.Integer noticeId){
		this.noticeId = noticeId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  用户id，关联user表的id
	 */
	@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户id，关联user表的id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  阅读状态，0=未读，1=已读
	 */
	@Column(name ="READ_STATUS",nullable=false,precision=3,scale=0)
	public java.lang.Integer getReadStatus(){
		return this.readStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  阅读状态，0=未读，1=已读
	 */
	public void setReadStatus(java.lang.Integer readStatus){
		this.readStatus = readStatus;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  阅读时间
	 */
	@Column(name ="READ_TIME",nullable=true)
	public java.util.Date getReadTime(){
		return this.readTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  阅读时间
	 */
	public void setReadTime(java.util.Date readTime){
		this.readTime = readTime;
	}

}
