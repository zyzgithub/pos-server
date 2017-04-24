package com.wm.entity.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: function
 * @author wuyong
 * @date 2015-01-07 11:32:09
 * @version V1.0   
 *
 */
@Entity
@Table(name = "function", schema = "")
@SuppressWarnings("serial")
public class WfunctionEntity implements java.io.Serializable {
	/**主键ID*/
	private java.lang.Integer id;
	/**父权限ID*/
	private WfunctionEntity parentFunction;
	//private java.lang.Integer parentFunctionId;
	/**菜单名称*/
	private java.lang.String functionName;
	/**菜单等级*/
	private java.lang.Integer functionLevel;
	/**菜单地址*/
	private java.lang.String functionUrl;
	/**functionOrder*/
	private java.lang.Integer functionOrder;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键ID
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  父权限ID
	 */
	/*@Column(name ="PARENT_FUNCTION_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getParentFunctionId(){
		return this.parentFunctionId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  父权限ID
	 */
	/*public void setParentFunctionId(java.lang.Integer parentFunctionId){
		this.parentFunctionId = parentFunctionId;
	}*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_FUNCTION_ID", nullable = true)
	public WfunctionEntity getParentFunction() {
		return parentFunction;
	}

	public void setParentFunction(WfunctionEntity parentFunction) {
		this.parentFunction = parentFunction;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  菜单名称
	 */
	@Column(name ="FUNCTION_NAME",nullable=false,length=50)
	public java.lang.String getFunctionName(){
		return this.functionName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  菜单名称
	 */
	public void setFunctionName(java.lang.String functionName){
		this.functionName = functionName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  菜单等级
	 */
	@Column(name ="FUNCTION_LEVEL",nullable=true,precision=5,scale=0)
	public java.lang.Integer getFunctionLevel(){
		return this.functionLevel;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  菜单等级
	 */
	public void setFunctionLevel(java.lang.Integer functionLevel){
		this.functionLevel = functionLevel;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  菜单地址
	 */
	@Column(name ="FUNCTION_URL",nullable=true,length=100)
	public java.lang.String getFunctionUrl(){
		return this.functionUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  菜单地址
	 */
	public void setFunctionUrl(java.lang.String functionUrl){
		this.functionUrl = functionUrl;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  functionOrder
	 */
	@Column(name ="FUNCTION_ORDER",nullable=true,precision=5,scale=0)
	public java.lang.Integer getFunctionOrder(){
		return this.functionOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  functionOrder
	 */
	public void setFunctionOrder(java.lang.Integer functionOrder){
		this.functionOrder = functionOrder;
	}
}
