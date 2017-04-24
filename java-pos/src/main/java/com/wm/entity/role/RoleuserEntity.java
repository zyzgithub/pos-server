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

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: role_user
 * @author wuyong
 * @date 2015-01-07 10:04:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "role_user", schema = "")
@SuppressWarnings("serial")
public class RoleuserEntity implements java.io.Serializable {
	/**主键ID*/
	private java.lang.Integer id;
	/**用户ID*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**角色ID*/
	private WRoleEntity role;
	//private java.lang.Integer roleId;
	
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
	 *@return: java.lang.Integer  用户ID
	 */
	/*@Column(name ="USER_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户ID
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
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
	
	
}
