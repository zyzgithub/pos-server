package com.wm.entity.dineorder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 堂食订单详情
 * @author wuyong
 * @date 2015-04-01 16:15:01
 * @version V1.0
 *
 */
@Entity
@Table(name = "dine_order_menu", schema = "")
@SuppressWarnings("serial")
public class DineOrderMenuEntity implements java.io.Serializable {
	/** id */
	private java.lang.Integer id;
	/** orderId */
	private java.lang.Integer orderId;
	/** menuId */
	private java.lang.Integer menuId;
	/** quantity */
	private java.lang.Integer quantity;
	/** price */
	private java.lang.Double price;
	/** totalPrice */
	private java.lang.Double totalPrice;
	/** state */
	private java.lang.String state;

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer id
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 20, scale = 0)
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer orderId
	 */
	@Column(name = "ORDER_ID", nullable = false, precision = 19, scale = 0)
	public java.lang.Integer getOrderId() {
		return this.orderId;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer orderId
	 */
	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer menuId
	 */
	@Column(name = "MENU_ID", nullable = false, precision = 19, scale = 0)
	public java.lang.Integer getMenuId() {
		return this.menuId;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer menuId
	 */
	public void setMenuId(java.lang.Integer menuId) {
		this.menuId = menuId;
	}

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer quantity
	 */
	@Column(name = "QUANTITY", nullable = false, precision = 10, scale = 0)
	public java.lang.Integer getQuantity() {
		return this.quantity;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer quantity
	 */
	public void setQuantity(java.lang.Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 方法: 取得java.lang.Double
	 *
	 * @return: java.lang.Double price
	 */
	@Column(name = "PRICE", nullable = false, precision = 11, scale = 2)
	public java.lang.Double getPrice() {
		return this.price;
	}

	/**
	 * 方法: 设置java.lang.Double
	 *
	 * @param: java.lang.Double price
	 */
	public void setPrice(java.lang.Double price) {
		this.price = price;
	}

	/**
	 * 方法: 取得java.lang.Double
	 *
	 * @return: java.lang.Double totalPrice
	 */
	@Column(name = "TOTAL_PRICE", nullable = false, precision = 11, scale = 2)
	public java.lang.Double getTotalPrice() {
		return this.totalPrice;
	}

	/**
	 * 方法: 设置java.lang.Double
	 *
	 * @param: java.lang.Double totalPrice
	 */
	public void setTotalPrice(java.lang.Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * 方法: 取得java.lang.Object
	 *
	 * @return: java.lang.Object state
	 */
	@Column(name = "STATE", nullable = true, length = 10)
	public java.lang.String getState() {
		return this.state;
	}

	/**
	 * 方法: 设置java.lang.Object
	 *
	 * @param: java.lang.Object state
	 */
	public void setState(java.lang.String state) {
		this.state = state;
	}
}
