package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wm.entity.menu.MenuEntity;

/**   
 * @Title: Entity
 * @Description: order_menu
 * @author wuyong
 * @date 2015-01-07 10:01:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "order_menu", schema = "")
@SuppressWarnings("serial")
public class OrdermenuEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**orderId*/
	private OrderEntity order;
	/**menuId*/
	private MenuEntity menu;
	/**quantity*/
	private java.lang.Integer quantity;
	/**price*/
	private java.lang.Double price;
	/**totalPrice*/
	private java.lang.Double totalPrice;
	
	private java.lang.String state = "uncomplete";
	/**
	 * 促销单价
	 */
	private java.lang.Double promotionMoney;
	/**
	 * 默认为N，N为不促销，Y为促销
	 */
	private java.lang.String salesPromotion="N";
	/**
	 * 促销表ID
	 */
	private java.lang.Integer menuPromotionId;
	
	
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
	 *@return: java.lang.Integer  orderId
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = true)
	public OrderEntity getOrder(){
		return this.order;
	}
	

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  orderId
	 */
	public void setOrder(OrderEntity order){
		this.order = order;
	}	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = true)
	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}
*/

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  menuId
	
	@Column(name ="MENU_ID",nullable=false)
	public MenuEntity getMenu(){
		return this.menu;
	} */

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  menuId
	 
	public void setMenu(MenuEntity menu){
		this.menu = menu;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = true)
	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  quantity
	 */
	@Column(name ="QUANTITY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getQuantity(){
		return this.quantity;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  quantity
	 */
	public void setQuantity(java.lang.Integer quantity){
		this.quantity = quantity;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  price
	 */
	@Column(name ="PRICE",nullable=false,precision=11,scale=2)
	public java.lang.Double getPrice(){
		return this.price;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  price
	 */
	public void setPrice(java.lang.Double price){
		this.price = price;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  totalPrice
	 */
	@Column(name ="TOTAL_PRICE",nullable=false,precision=11,scale=2)
	public java.lang.Double getTotalPrice(){
		return this.totalPrice;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  totalPrice
	 */
	public void setTotalPrice(java.lang.Double totalPrice){
		this.totalPrice = totalPrice;
	}
	
	@Column(name ="STATE",nullable=true,length=6)
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  state
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}
	
	@Column(name="promotion_money",nullable=true)
	public java.lang.Double getPromotionMoney() {
		return promotionMoney;
	}

	public void setPromotionMoney(java.lang.Double promotionMoney) {
		this.promotionMoney = promotionMoney;
	}

	@Column(name="sales_promotion",nullable=true)
	public java.lang.String getSalesPromotion() {
		return salesPromotion;
	}

	public void setSalesPromotion(java.lang.String salesPromotion) {
		this.salesPromotion = salesPromotion;
	}

	@Column(name="menu_promotion_id",nullable=true)
	public java.lang.Integer getMenuPromotionId() {
		return menuPromotionId;
	}

	public void setMenuPromotionId(java.lang.Integer menuPromotionId) {
		this.menuPromotionId = menuPromotionId;
	}
	
	
	
}
