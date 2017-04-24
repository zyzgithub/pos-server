package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 收银员与订单的关联表
 * @author wuyong
 * @date 2016-05-26 15:29:05
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_cashier_order", schema = "")
@SuppressWarnings("serial")
public class CashierOrderEntity implements java.io.Serializable {
	/**订单ID  来自订单表*/
	private java.lang.Integer orderId;
	/**收银员ID*/
	private java.lang.Integer cashierId;
	/**创建时间*/
	private java.lang.Integer createTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  订单ID  来自订单表
	 */
	@Id
	@Column(name ="ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单ID  来自订单表
	 */
	public void setOrderId(java.lang.Integer orderId){
		this.orderId = orderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收银员ID
	 */
	@Column(name ="CASHIER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCashierId(){
		return this.cashierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收银员ID
	 */
	public void setCashierId(java.lang.Integer cashierId){
		this.cashierId = cashierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
}
