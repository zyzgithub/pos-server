package com.wm.entity.favorites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: favorites
 * @author wuyong
 * @date 2015-01-07 09:57:03
 * @version V1.0   
 *
 */
@Entity
@Table(name = "favorites", schema = "")
@SuppressWarnings("serial")
public class FavoritesEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**1-菜品，2-商家*/
	private java.lang.String type;
	/**userid*/
	//private WUserEntity wuser;
	private java.lang.Integer userid;
	/**菜品*/
	//private MenuEntity menu;
	private java.lang.Integer itemId;
	/**fTime*/
	private java.lang.Integer fTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
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
	 *@return: java.lang.String  1-菜品，2-商家
	 */
	@Column(name ="TYPE",nullable=true,length=2)
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  1-菜品，2-商家
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  userid
	 */
	@Column(name ="USERID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUserid(){
		return this.userid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userid
	 */
	public void setUserid(java.lang.Integer userid){
		this.userid = userid;
	}
/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}*/
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  菜品
	 */
	@Column(name ="ITEM_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getItemId(){
		return this.itemId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  菜品
	 */
	public void setItemId(java.lang.Integer itemId){
		this.itemId = itemId;
	}
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_ID", nullable = true)
	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}*/

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  fTime
	 */
	@Column(name ="F_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getFTime(){
		return this.fTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  fTime
	 */
	public void setFTime(java.lang.Integer fTime){
		this.fTime = fTime;
	}
}
