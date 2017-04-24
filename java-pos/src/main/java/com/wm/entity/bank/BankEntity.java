package com.wm.entity.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: bank
 * @author wuyong
 * @date 2015-01-07 09:45:18
 * @version V1.0   
 *
 */
@Entity
@Table(name = "bank", schema = "")
@SuppressWarnings("serial")
public class BankEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**name*/
	private java.lang.String name;
	/**logoUrl*/
	private java.lang.String logoUrl;
	
	private java.lang.String bankCode;
	
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
	 *@return: java.lang.String  name
	 */
	@Column(name ="NAME",nullable=false,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  name
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  logoUrl
	 */
	@Column(name ="LOGO_URL",nullable=true,length=100)
	public java.lang.String getLogoUrl(){
		return this.logoUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  logoUrl
	 */
	public void setLogoUrl(java.lang.String logoUrl){
		this.logoUrl = logoUrl;
	}
	
	@Column(name = "BANK_CODE", nullable = true, length = 20)
	public java.lang.String getBankCode() {
		return bankCode;
	}

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public BankEntity(Integer id) {
		super();
		this.id = id;
	}

	public BankEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
}