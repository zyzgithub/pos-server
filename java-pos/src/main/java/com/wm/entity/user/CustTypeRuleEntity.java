package com.wm.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 客户类型规则定义表
 * @author wuyong
 * @date 2015-08-27 22:18:32
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_custtype_rule", schema = "")
@SuppressWarnings("serial")
public class CustTypeRuleEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**客户类型，如：A*/
	private java.lang.String typeName;
	/**客户类型，如：黄钻*/
	private java.lang.String typeDesc;
	/**今年送单总额*/
	private java.lang.Integer amount;
	/**invalid*/
	private java.lang.Integer invalid;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户类型，如：A
	 */
	@Column(name ="TYPE_NAME",nullable=false,length=32)
	public java.lang.String getTypeName(){
		return this.typeName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户类型，如：A
	 */
	public void setTypeName(java.lang.String typeName){
		this.typeName = typeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户类型，如：黄钻
	 */
	@Column(name ="TYPE_DESC",nullable=false,length=64)
	public java.lang.String getTypeDesc(){
		return this.typeDesc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户类型，如：黄钻
	 */
	public void setTypeDesc(java.lang.String typeDesc){
		this.typeDesc = typeDesc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  今年送单总额
	 */
	@Column(name ="AMOUNT",nullable=false,precision=10,scale=0)
	public java.lang.Integer getAmount(){
		return this.amount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  今年送单总额
	 */
	public void setAmount(java.lang.Integer amount){
		this.amount = amount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  invalid
	 */
	@Column(name ="INVALID",nullable=true,precision=3,scale=0)
	public java.lang.Integer getInvalid(){
		return this.invalid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  invalid
	 */
	public void setInvalid(java.lang.Integer invalid){
		this.invalid = invalid;
	}
}
