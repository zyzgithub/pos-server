package com.wm.entity.position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 职称表
 * @author wuyong
 * @date 2015-08-28 09:25:19
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_position", schema = "")
@SuppressWarnings("serial")
public class PositionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**name*/
	private java.lang.String name;
	/**等级排序,用于升降级*/
	private java.lang.Integer sortOrder;
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  等级排序,用于升降级
	 */
	@Column(name ="SORT_ORDER",nullable=false,precision=3,scale=0)
	public java.lang.Integer getSortOrder(){
		return this.sortOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  等级排序,用于升降级
	 */
	public void setSortOrder(java.lang.Integer sortOrder){
		this.sortOrder = sortOrder;
	}
}
