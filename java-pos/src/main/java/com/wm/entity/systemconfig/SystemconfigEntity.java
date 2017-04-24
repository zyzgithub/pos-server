package com.wm.entity.systemconfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: system_config
 * @author wuyong
 * @date 2015-01-07 10:05:09
 * @version V1.0   
 *
 */
@Entity
@Table(name = "system_config", schema = "")
@SuppressWarnings("serial")
public class SystemconfigEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**code*/
	private java.lang.String code;
	/**value*/
	private java.lang.String value;
	
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
	 *@return: java.lang.String  code
	 */
	@Column(name ="CODE",nullable=true,length=20)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  code
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  value
	 */
	@Column(name ="VALUE",nullable=true,length=100)
	public java.lang.String getValue(){
		return this.value;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  value
	 */
	public void setValue(java.lang.String value){
		this.value = value;
	}
}
