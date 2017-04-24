package com.wm.entity.couriergroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员群组
 * @author wuyong
 * @date 2015-08-28 09:30:44
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_group", schema = "")
@SuppressWarnings("serial")
public class CourierGroupEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**快递员分组名*/
	private java.lang.String groupName;
	/**分组描述*/
	private java.lang.String groupDesc;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  快递员分组名
	 */
	@Column(name ="GROUP_NAME",nullable=false,length=32)
	public java.lang.String getGroupName(){
		return this.groupName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  快递员分组名
	 */
	public void setGroupName(java.lang.String groupName){
		this.groupName = groupName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  分组描述
	 */
	@Column(name ="GROUP_DESC",nullable=false,length=64)
	public java.lang.String getGroupDesc(){
		return this.groupDesc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  分组描述
	 */
	public void setGroupDesc(java.lang.String groupDesc){
		this.groupDesc = groupDesc;
	}
}
