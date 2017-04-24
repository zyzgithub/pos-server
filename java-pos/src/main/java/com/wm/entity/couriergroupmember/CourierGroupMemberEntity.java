package com.wm.entity.couriergroupmember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 关联表：快递员组-成员
 * @author wuyong
 * @date 2015-08-28 09:31:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_group_member", schema = "")
@SuppressWarnings("serial")
public class CourierGroupMemberEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**快递员组*/
	private java.lang.Integer groupId;
	/**快递员*/
	private java.lang.Integer userId;
	/**是否组长，0：否，1：是*/
	private java.lang.Integer isLeader;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员组
	 */
	@Column(name ="GROUP_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getGroupId(){
		return this.groupId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员组
	 */
	public void setGroupId(java.lang.Integer groupId){
		this.groupId = groupId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员
	 */
	@Column(name ="USER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否组长，0：否，1：是
	 */
	@Column(name ="IS_LEADER",nullable=false,precision=3,scale=0)
	public java.lang.Integer getIsLeader(){
		return this.isLeader;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否组长，0：否，1：是
	 */
	public void setIsLeader(java.lang.Integer isLeader){
		this.isLeader = isLeader;
	}
}
