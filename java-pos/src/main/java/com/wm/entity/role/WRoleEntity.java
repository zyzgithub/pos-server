package com.wm.entity.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: role
 * @author wuyong
 * @date 2015-01-07 11:35:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "role", schema = "")
@SuppressWarnings("serial")
public class WRoleEntity implements java.io.Serializable {
	/**角色ID*/
	private java.lang.Integer id;
	/**角色名称*/
	private java.lang.String rolename;
	/**角色编码*/
	private java.lang.String rolecode;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  角色ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  角色ID
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  角色名称
	 */
	@Column(name ="ROLENAME",nullable=true,length=20)
	public java.lang.String getRolename(){
		return this.rolename;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  角色名称
	 */
	public void setRolename(java.lang.String rolename){
		this.rolename = rolename;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  角色编码
	 */
	@Column(name ="ROLECODE",nullable=true,length=15)
	public java.lang.String getRolecode(){
		return this.rolecode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  角色编码
	 */
	public void setRolecode(java.lang.String rolecode){
		this.rolecode = rolecode;
	}
}
