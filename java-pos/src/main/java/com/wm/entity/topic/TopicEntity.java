package com.wm.entity.topic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: topic
 * @author wuyong
 * @date 2015-01-26 14:09:18
 * @version V1.0   
 *
 */
@Entity
@Table(name = "topic", schema = "")
@SuppressWarnings("serial")
public class TopicEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private java.lang.Integer userId;
	/**createTime*/
	private java.lang.Integer createTime;
	/**title*/
	private java.lang.String title;
	/**text*/
	private java.lang.String text;
	/**赞同数*/
	private java.lang.Integer numApproval;
	/**浏览数*/
	private java.lang.Integer numScan;
	/**评论数*/
	private java.lang.Integer numComment;
	/**图片*/
	private java.lang.String image;
	/**哪一小组话题*/
	private java.lang.Integer teamId;
	/**'Y':'是';'N':'否'*/
	private java.lang.String isApprovalbyself;
	
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
	 *@return: java.lang.Integer  userId
	 */
	@Column(name ="USER_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  title
	 */
	@Column(name ="TITLE",nullable=true,length=255)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  title
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  text
	 */
	@Column(name ="TEXT",nullable=false,length=255)
	public java.lang.String getText(){
		return this.text;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  text
	 */
	public void setText(java.lang.String text){
		this.text = text;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  赞同数
	 */
	@Column(name ="NUM_APPROVAL",nullable=true,precision=10,scale=0)
	public java.lang.Integer getNumApproval(){
		return this.numApproval;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  赞同数
	 */
	public void setNumApproval(java.lang.Integer numApproval){
		this.numApproval = numApproval;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  浏览数
	 */
	@Column(name ="NUM_SCAN",nullable=true,precision=10,scale=0)
	public java.lang.Integer getNumScan(){
		return this.numScan;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  浏览数
	 */
	public void setNumScan(java.lang.Integer numScan){
		this.numScan = numScan;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  评论数
	 */
	@Column(name ="NUM_COMMENT",nullable=false,precision=10,scale=0)
	public java.lang.Integer getNumComment(){
		return this.numComment;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  评论数
	 */
	public void setNumComment(java.lang.Integer numComment){
		this.numComment = numComment;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  图片
	 */
	@Column(name ="IMAGE",nullable=false,length=255)
	public java.lang.String getImage(){
		return this.image;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  图片
	 */
	public void setImage(java.lang.String image){
		this.image = image;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  哪一小组话题
	 */
	@Column(name ="TEAM_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTeamId(){
		return this.teamId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  哪一小组话题
	 */
	public void setTeamId(java.lang.Integer teamId){
		this.teamId = teamId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  'Y':'是';'N':'否'
	 */
	@Column(name ="IS_APPROVALBYSELF",nullable=true,length=2)
	public java.lang.String getIsApprovalbyself(){
		return this.isApprovalbyself;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  'Y':'是';'N':'否'
	 */
	public void setIsApprovalbyself(java.lang.String isApprovalbyself){
		this.isApprovalbyself = isApprovalbyself;
	}
}
