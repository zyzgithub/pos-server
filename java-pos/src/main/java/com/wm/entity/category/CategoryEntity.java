package com.wm.entity.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: category
 * @author wuyong
 * @date 2015-01-07 09:55:55
 * @version V1.0   
 *
 */
@Entity
@Table(name = "category", schema = "")
@SuppressWarnings("serial")
public class CategoryEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**zone*/
	private java.lang.String zone;
	/**czone*/
	private java.lang.String czone;
	/**name*/
	private java.lang.String name;
	/**ename*/
	private java.lang.String ename;
	/**letter*/
	private java.lang.String letter;
	/**sortOrder*/
	private java.lang.Integer sortOrder;
	/**display*/
	private java.lang.String display;
	/**relateData*/
	private java.lang.String relateData;
	/**父id*/
	private java.lang.Integer fid;
	/**是否为热门城市*/
	private java.lang.String hot;
	
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
	 *@return: java.lang.String  zone
	 */
	@Column(name ="ZONE",nullable=true,length=16)
	public java.lang.String getZone(){
		return this.zone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  zone
	 */
	public void setZone(java.lang.String zone){
		this.zone = zone;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  czone
	 */
	@Column(name ="CZONE",nullable=true,length=32)
	public java.lang.String getCzone(){
		return this.czone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  czone
	 */
	public void setCzone(java.lang.String czone){
		this.czone = czone;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  name
	 */
	@Column(name ="NAME",nullable=true,length=32)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ename
	 */
	@Column(name ="ENAME",nullable=true,length=16)
	public java.lang.String getEname(){
		return this.ename;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ename
	 */
	public void setEname(java.lang.String ename){
		this.ename = ename;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  letter
	 */
	@Column(name ="LETTER",nullable=true,length=1)
	public java.lang.String getLetter(){
		return this.letter;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  letter
	 */
	public void setLetter(java.lang.String letter){
		this.letter = letter;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  sortOrder
	 */
	@Column(name ="SORT_ORDER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSortOrder(){
		return this.sortOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  sortOrder
	 */
	public void setSortOrder(java.lang.Integer sortOrder){
		this.sortOrder = sortOrder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  display
	 */
	@Column(name ="DISPLAY",nullable=false,length=1)
	public java.lang.String getDisplay(){
		return this.display;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  display
	 */
	public void setDisplay(java.lang.String display){
		this.display = display;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  relateData
	 */
	@Column(name ="RELATE_DATA",nullable=true,length=65535)
	public java.lang.String getRelateData(){
		return this.relateData;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  relateData
	 */
	public void setRelateData(java.lang.String relateData){
		this.relateData = relateData;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  父id
	 */
	@Column(name ="FID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getFid(){
		return this.fid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  父id
	 */
	public void setFid(java.lang.Integer fid){
		this.fid = fid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否为热门城市
	 */
	@Column(name ="HOT",nullable=false,length=1)
	public java.lang.String getHot(){
		return this.hot;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否为热门城市
	 */
	public void setHot(java.lang.String hot){
		this.hot = hot;
	}

	public CategoryEntity() {
		super();
	}

	public CategoryEntity(Integer id) {
		super();
		this.id = id;
	}
}
