package com.testuser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 测试用户
 * @author zhenjunzhuo
 * @date 2014-07-15 11:06:10
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_test_user", schema = "")
@SuppressWarnings("serial")
public class TestUser implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**createTime*/
	private java.util.Date createTime;
	/**timeOut*/
	private java.util.Date timeOut;
	/**userid*/
	private java.lang.String userId;

	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="sessionkey")
	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=true)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  timeOut
	 */
	@Column(name ="TIME_OUT",nullable=true)
	public java.util.Date getTimeOut(){
		return this.timeOut;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  timeOut
	 */
	public void setTimeOut(java.util.Date timeOut){
		this.timeOut = timeOut;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  userid
	 */
	@Column(name ="userid",nullable=true,length=32)
	public java.lang.String getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  userid
	 */
	public void setUserId(java.lang.String userId){
		this.userId = userId;
	}
}
