package com.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 短信
 * @author wuyong
 * @date 2015-01-19 10:09:46
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_sms", schema = "")
@SuppressWarnings("serial")
public class SmsEntity implements java.io.Serializable {
	/**id*/
	private Integer id;
	/**手机号码*/
	private String phone;
	/**内容*/
	private String content;
	/**发送时间*/
	private java.util.Date sendtime;
	
	
	/**
	 *方法: 取得Integer
	 *@return: Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置Integer
	 *@param: Integer  id
	 */
	public void setId(Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得String
	 *@return: String  手机号码
	 */
	@Column(name ="PHONE",nullable=true,length=11)
	public String getPhone(){
		return this.phone;
	}

	/**
	 *方法: 设置String
	 *@param: String  手机号码
	 */
	public void setPhone(String phone){
		this.phone = phone;
	}
	/**
	 *方法: 取得String
	 *@return: String  内容
	 */
	@Column(name ="CONTENT",nullable=true,length=255)
	public String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置String
	 *@param: String  内容
	 */
	public void setContent(String content){
		this.content = content;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  发送时间
	 */
	@Column(name ="SENDTIME",nullable=true)
	public java.util.Date getSendtime(){
		return this.sendtime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  发送时间
	 */
	public void setSendtime(java.util.Date sendtime){
		this.sendtime = sendtime;
	}	
	
}
