package com.ci.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 自定义接口
 * @author zhenjunzhuo
 * @date 2014-08-01 17:15:35
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_customer_interface", schema = "")
@SuppressWarnings("serial")
public class CustomerInterface implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**名称*/
	private java.lang.String name;
	/**方法名*/
	private java.lang.String methodName;
	/**描述*/
	private java.lang.String descrition;
	/**方法级别，1为一级，2为二级*/
	private java.lang.Integer methodLevel;
	/**父接口*/
	private CustomerInterface customerInterface;
	/**子接口*/
	private List<CustomerInterface> CustomerInterfaces = new ArrayList<CustomerInterface>();
	/**控制器名称*/
	private String controllerName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_code")
	public CustomerInterface getCustomerInterface() {
		return customerInterface;
	}

	public void setCustomerInterface(CustomerInterface customerInterface) {
		this.customerInterface = customerInterface;
	}

	@OneToMany(mappedBy = "customerInterface")
	public List<CustomerInterface> getCustomerInterfaces() {
		return CustomerInterfaces;
	}

	public void setCustomerInterfaces(List<CustomerInterface> customerInterfaces) {
		CustomerInterfaces = customerInterfaces;
	}

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
	 *@return: java.lang.String  名称
	 */
	@Column(name ="NAME",nullable=true,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  方法名
	 */
	@Column(name ="METHOD_NAME",nullable=true,length=32)
	public java.lang.String getMethodName(){
		return this.methodName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  方法名
	 */
	public void setMethodName(java.lang.String methodName){
		this.methodName = methodName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	@Column(name ="DESCRITION",nullable=true,length=200)
	public java.lang.String getDescrition(){
		return this.descrition;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  描述
	 */
	public void setDescrition(java.lang.String descrition){
		this.descrition = descrition;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  方法级别，1为一级，2为二级
	 */
	@Column(name ="METHOD_LEVEL",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMethodLevel(){
		return this.methodLevel;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  方法级别，1为一级，2为二级
	 */
	public void setMethodLevel(java.lang.Integer methodLevel){
		this.methodLevel = methodLevel;
	}
	
	@Column(name ="controller_name",nullable=true)
	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
}
