package com.wm.entity.order;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "0085_expired_pushed_order", schema = "")
@SuppressWarnings("serial")
public class ExpiredPushedOrderEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**订单ID*/
	private java.lang.Integer orderId;
	/**快递员ID*/
	private java.lang.Integer pushedCourier;
	/**创建时间*/
	private java.lang.Long createTime;
	/**这条推送信息在原有表中的创建时间*/
	private java.lang.Long originCreateTime;
	
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  订单ID
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单ID
	 */
	public void setOrderId(java.lang.Integer orderId){
		this.orderId = orderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员ID
	 */
	@Column(name ="PUSHED_COURIER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPushedCourier(){
		return this.pushedCourier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员ID
	 */
	public void setPushedCourier(java.lang.Integer pushedCourier){
		this.pushedCourier = pushedCourier;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Long getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Long createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  这条推送信息在原有表中的创建时间
	 */
	@Column(name ="ORIGIN_CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Long getOriginCreateTime(){
		return this.originCreateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  这条推送信息在原有表中的创建时间
	 */
	public void setOriginCreateTime(java.lang.Long originCreateTime){
		this.originCreateTime = originCreateTime;
	}
}
