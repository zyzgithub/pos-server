package com.wm.entity.building;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 城市区域表
 * @author wuyong
 * @date 2015-08-13 11:52:29
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_city_area", schema = "")
@SuppressWarnings("serial")
public class CityAreaEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**name*/
	private java.lang.String name;
	/**父id，即本表id,为0代表顶级节点*/
	private java.lang.Integer parentId;
	/**1=省，2=市，3=区，4=街道，5=乡*/
	private java.lang.Integer type;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
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
	@Column(name ="NAME",nullable=false,length=255)
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
	 *@return: java.lang.Integer  父id，即本表id,为0代表顶级节点
	 */
	@Column(name ="PARENT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getParentId(){
		return this.parentId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  父id，即本表id,为0代表顶级节点
	 */
	public void setParentId(java.lang.Integer parentId){
		this.parentId = parentId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  1=省，2=市，3=区，4=街道，5=乡
	 */
	@Column(name ="TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  1=省，2=市，3=区，4=街道，5=乡
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
}
