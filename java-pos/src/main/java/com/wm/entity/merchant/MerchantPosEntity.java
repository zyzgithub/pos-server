package com.wm.entity.merchant;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 商家信息扩展
 * @author zhanxinming
 * @date 2016-08-11 16:30:05
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_pos", schema = "")
@SuppressWarnings("serial")
public class MerchantPosEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**商家id*/
	private java.lang.Integer merchantId;
	/**pos版本类型，对应sys_code的code_value,1:pos超市版，2：pos基础版，3：pos管家版*/
	private java.lang.Integer editionCodeValue;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**操作员id*/
	private java.lang.Integer operator;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  pos版本类型，对应sys_code的code_value,1:pos超市版，2：pos基础版，3：pos管家版
	 */
	@Column(name ="EDITION_CODE_VALUE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getEditionCodeValue(){
		return this.editionCodeValue;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  pos版本类型，对应sys_code的code_value,1:pos超市版，2：pos基础版，3：pos管家版
	 */
	public void setEditionCodeValue(java.lang.Integer editionCodeValue){
		this.editionCodeValue = editionCodeValue;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  操作员id
	 */
	@Column(name ="OPERATOR",nullable=false,precision=19,scale=0)
	public java.lang.Integer getOperator(){
		return this.operator;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  操作员id
	 */
	public void setOperator(java.lang.Integer operator){
		this.operator = operator;
	}
}
