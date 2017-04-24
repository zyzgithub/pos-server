package com.testuser.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 真机测试结果
 * @author leichanglin
 * @date 2014-07-22 17:41:44
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_test_user_result", schema = "")
public class TestUserResult implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**id*/
	private java.lang.String id;
	/**接口id*/
	private java.lang.String exeid;
	/**输入参数*/
	private java.lang.String params;
	/**结果*/
	private java.lang.String result;
	/**用户标识*/
	private java.lang.String sessionKey;
	/**操作时间*/
	private java.util.Date dated;
	

	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator") 
	@Column(name ="ID",nullable=false,length=32)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口id
	 */
	@Column(name ="EXEID",nullable=true,length=32)
	public java.lang.String getExeid(){
		return this.exeid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口id
	 */
	public void setExeid(java.lang.String exeid){
		this.exeid = exeid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  输入参数
	 */
	@Column(name ="PARAMS",nullable=true,length=200)
	public java.lang.String getParams(){
		return this.params;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  输入参数
	 */
	public void setParams(java.lang.String params){
		this.params = params;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  结果
	 */
	@Column(name ="RESULT",nullable=true,length=65535)
	public java.lang.String getResult(){
		return this.result;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  结果
	 */
	public void setResult(java.lang.String result){
		this.result = result;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用户标识
	 */
	@Column(name ="sessionkey",nullable=true,length=32)
	public java.lang.String getSessionKey(){
		return this.sessionKey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用户标识
	 */
	public void setSessionKey(java.lang.String sessionKey){
		this.sessionKey = sessionKey;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  操作时间
	 */
	@Column(name ="DATED",nullable=true)
	public java.util.Date getDated(){
		return this.dated;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  操作时间
	 */
	public void setDated(java.util.Date dated){
		this.dated = dated;
	}
}
