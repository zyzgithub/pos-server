package com.ci.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 自定义接口
 * @author leichanglin
 * @date 2014-08-01 17:16:20
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_customer_interface_param", schema = "")
@SuppressWarnings("serial")
public class CIParam implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**paramName*/
	private java.lang.String paramName;
	/**paramType*/
	private java.lang.String paramType;
	/**descrition*/
	private java.lang.String descrition;
	/**自定义接口*/
	private CustomerInterface customerInterface;
	
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
	 *@return: java.lang.String  paramName
	 */
	@Column(name ="PARAM_NAME",nullable=true,length=32)
	public java.lang.String getParamName(){
		return this.paramName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  paramName
	 */
	public void setParamName(java.lang.String paramName){
		this.paramName = paramName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  paramType
	 */
	@Column(name ="PARAM_TYPE",nullable=true,length=32)
	public java.lang.String getParamType(){
		return this.paramType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  paramType
	 */
	public void setParamType(java.lang.String paramType){
		this.paramType = paramType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  descrition
	 */
	@Column(name ="DESCRITION",nullable=true,length=200)
	public java.lang.String getDescrition(){
		return this.descrition;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  descrition
	 */
	public void setDescrition(java.lang.String descrition){
		this.descrition = descrition;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interfaec_id")
	public CustomerInterface getCustomerInterface() {
		return customerInterface;
	}

	public void setCustomerInterface(CustomerInterface customerInterface) {
		this.customerInterface = customerInterface;
	}
}
