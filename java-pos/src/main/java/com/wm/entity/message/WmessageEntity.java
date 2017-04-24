package com.wm.entity.message;

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
 * @Description: message
 * @author wuyong
 * @date 2015-01-07 10:08:22
 * @version V1.0   
 *
 */
@Entity
@Table(name = "message", schema = "")
@SuppressWarnings("serial")
public class WmessageEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**content*/
	private java.lang.String content;
	/**read*/
	private java.lang.String read = "N";
	/**createTime*/
	private java.lang.Integer createTime;
	/**title*/
	private java.lang.String title;
	
	
	
	@Column(name ="TITLE",nullable=true,length=255)
	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  content
	 */
	@Column(name ="CONTENT",nullable=true,length=255)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  content
	 */
	public void setContent(java.lang.String content){
		this.content = content;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  read
	 */
	@Column(name ="READ",nullable=true,length=1)
	public java.lang.String getRead(){
		return this.read;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  read
	 */
	public void setRead(java.lang.String read){
		this.read = read;
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
}
