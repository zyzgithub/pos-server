package com.wm.entity.networkDevelop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员开拓网点记录表
 * @author wuyong
 * @date 2016-04-13 11:42:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_shop_detail", schema = "")
@SuppressWarnings("serial")
public class ShopDetailEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**网点开拓记录id*/
	private java.lang.Integer networkDevid;
	/**门店门店数量*/
	private java.lang.Integer shopAmount;
	/**超市门店数量*/
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  网点开拓记录id
	 */
	@Column(name ="NETWORK_DEVID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getNetworkDevid(){
		return this.networkDevid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  网点开拓记录id
	 */
	public void setNetworkDevid(java.lang.Integer networkDevid){
		this.networkDevid = networkDevid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  门店数量
	 */
	@Column(name ="SHOP_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getShopAmount(){
		return this.shopAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  门店数量
	 */
	public void setShopAmount(java.lang.Integer shopAmount){
		this.shopAmount = shopAmount;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  门店类型id
	 */
	@Column(name ="TYPE",nullable=true,precision=19,scale=0)
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  门店类型id
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
}
