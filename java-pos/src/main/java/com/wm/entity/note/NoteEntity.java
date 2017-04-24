package com.wm.entity.note;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家群发短信记录表
 * @author wuyong
 * @date 2015-05-06 17:09:48
 * @version V1.0   
 *
 */
@Entity
@Table(name = "note", schema = "")
@SuppressWarnings("serial")
public class NoteEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**标题*/
	private java.lang.String title;
	/**内容*/
	private java.lang.String conten;
	/**商家ID*/
	private java.lang.Integer merchantid;
	/**创建时间*/
	private java.util.Date ceaterTime;
	/**短信状态*/
	private java.lang.String state;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标题
	 */
	@Column(name ="TITLE",nullable=true,length=255)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  内容
	 */
	@Column(name ="content",nullable=true,length=255)
	public java.lang.String getConten(){
		return this.conten;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  内容
	 */
	public void setConten(java.lang.String conten){
		this.conten = conten;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家ID
	 */
	@Column(name ="MERCHANTID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMerchantid(){
		return this.merchantid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家ID
	 */
	public void setMerchantid(java.lang.Integer merchantid){
		this.merchantid = merchantid;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="creation_time",nullable=true)
	public java.util.Date getCeaterTime(){
		return this.ceaterTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCeaterTime(java.util.Date ceaterTime){
		this
		.ceaterTime = ceaterTime;
	}
	
	@Column(name ="STATE",nullable=true)
	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}
	
}
