package com.wm.entity.role;

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
 * @Description: role_function
 * @author wuyong
 * @date 2015-01-07 10:03:49
 * @version V1.0   
 *
 */
@Entity
@Table(name = "role_function", schema = "")
@SuppressWarnings("serial")
public class RolefunctionEntity implements java.io.Serializable {
	/**主键ID*/
	private java.lang.Integer id;
	/**角色ID*/
	private WRoleEntity role;
	//private java.lang.Integer roleId;
	/**权限ID*/
	private java.lang.String functionId;
	/**操作权限代码*/
	private java.lang.String operation;
	
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
	 *@return: java.lang.Integer  角色ID
	 */
	/*@Column(name ="ROLE_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getRoleId(){
		return this.roleId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  角色ID
	 */
	/*public void setRoleId(java.lang.Integer roleId){
		this.roleId = roleId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = true)
	public WRoleEntity getRole() {
		return role;
	}

	public void setRole(WRoleEntity role) {
		this.role = role;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  权限ID
	 */
	@Column(name ="FUNCTION_ID",nullable=true,length=32)
	public java.lang.String getFunctionId(){
		return this.functionId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  权限ID
	 */
	public void setFunctionId(java.lang.String functionId){
		this.functionId = functionId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作权限代码
	 */
	@Column(name ="OPERATION",nullable=true,length=200)
	public java.lang.String getOperation(){
		return this.operation;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作权限代码
	 */
	public void setOperation(java.lang.String operation){
		this.operation = operation;
	}
}
