package com.log.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 操作日志
 * @author leichanglin
 * @date 2014-07-24 12:48:15
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_operate_log", schema = "")
@SuppressWarnings("serial")
public class OperateLog implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**接口ID*/
	private java.lang.String exeid;
	/**请求参数*/
	private java.lang.String params;
	/**返回结果*/
	private java.lang.String operateResult;
	/**操作用户*/
	private java.lang.String userid;
	/**日期*/
	private java.util.Date operateDate;
	/**操作类型*/
	private java.lang.String operateType;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	
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
	 *@return: java.lang.String  接口ID
	 */
	@Column(name ="EXEID",nullable=true,length=32)
	public java.lang.String getExeid(){
		return this.exeid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口ID
	 */
	public void setExeid(java.lang.String exeid){
		this.exeid = exeid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  请求参数
	 */
	@Column(name ="PARAMS",nullable=true,length=200)
	public java.lang.String getParams(){
		return this.params;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  请求参数
	 */
	public void setParams(java.lang.String params){
		this.params = params;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  返回结果
	 */
	@Column(name ="OPERATE_RESULT",nullable=true)
//	@javax.persistence.Transient
	public java.lang.String getOperateResult(){
		return this.operateResult;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  返回结果
	 */
	public void setOperateResult(java.lang.String operateResult){
		this.operateResult = operateResult;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作用户
	 */
	@Column(name ="USERID",nullable=true,length=32)
	public java.lang.String getUserid(){
		return this.userid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作用户
	 */
	public void setUserid(java.lang.String userid){
		this.userid = userid;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	@Column(name ="OPERATE_DATE",nullable=true)
	public java.util.Date getOperateDate(){
		return this.operateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setOperateDate(java.util.Date operateDate){
		this.operateDate = operateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型
	 */
	@Column(name ="OPERATE_TYPE",nullable=true,length=32)
	public java.lang.String getOperateType(){
		return this.operateType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型
	 */
	public void setOperateType(java.lang.String operateType){
		this.operateType = operateType;
	}


	@Override
	public String toString() {
		return "接口ID:" + exeid + ",操作类型" + operateType + ",操作用户" + userid + ",日期" + operateDate + 
				",请求参数" + params + ",返回结果" + operateResult;
	}
}
