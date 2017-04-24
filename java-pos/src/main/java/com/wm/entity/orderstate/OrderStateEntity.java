package com.wm.entity.orderstate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 订单状态
 * @author wuyong
 * @date 2015-02-02 16:15:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "order_state", schema = "")
@SuppressWarnings("serial")
public class OrderStateEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**orderId*/
	private java.lang.Integer orderId;
	/**处理时间*/
	private java.lang.Integer dealTime;
	/**状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价*/
	private java.lang.String state;
	/**detail*/
	private java.lang.String detail;
	
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
	 *@return: java.lang.Integer  orderId
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  orderId
	 */
	public void setOrderId(java.lang.Integer orderId){
		this.orderId = orderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理时间
	 */
	@Column(name ="DEAL_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDealTime(){
		return this.dealTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  处理时间
	 */
	public void setDealTime(java.lang.Integer dealTime){
		this.dealTime = dealTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价
	 */
	@Column(name ="STATE",nullable=true,length=10)
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态：未支付，已付款，已接单，申请取消，已取消，配送中，已收货，已确认，已评价
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  detail
	 */
	@Column(name ="DETAIL",nullable=true,length=100)
	public java.lang.String getDetail(){
		return this.detail;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  detail
	 */
	public void setDetail(java.lang.String detail){
		this.detail = detail;
	}
}
