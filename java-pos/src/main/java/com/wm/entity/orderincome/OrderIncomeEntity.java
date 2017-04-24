package com.wm.entity.orderincome;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 订单预收入
 * @author wuyong
 * @date 2015-02-12 16:43:25
 * @version V1.0   
 *
 */
@Entity
@Table(name = "order_income", schema = "")
@SuppressWarnings("serial")
public class OrderIncomeEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**orderId*/
	private java.lang.Integer orderId;
	/**payId*/
	private java.lang.String payId;
	/**merchantId*/
	private java.lang.Integer merchantId;
	/**createTime*/
	private java.lang.Integer createTime;
	/**payTime*/
	private java.lang.Integer payTime;
	/**state*/
	private java.lang.String state;
	/**money*/
	private java.lang.Double money;
	
	private java.lang.Double origin;
	/**快递员配送费**/
	private java.lang.Double deliveryMoney;
	/** 预收入类型，0 普通预收入，1 供应链预收入  */
	private Integer type;
	

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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  payId
	 */
	@Column(name ="PAY_ID",nullable=false,length=32)
	public java.lang.String getPayId(){
		return this.payId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  payId
	 */
	public void setPayId(java.lang.String payId){
		this.payId = payId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  merchantId
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  merchantId
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  payTime
	 */
	@Column(name ="PAY_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPayTime(){
		return this.payTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  payTime
	 */
	public void setPayTime(java.lang.Integer payTime){
		this.payTime = payTime;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  state
	 */
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
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name = "MONEY", precision = 12, scale = 4)
	public java.lang.Double getMoney() {
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  money
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
	}
	
	@Column(name = "ORIGIN", precision = 12, scale = 2)
	public java.lang.Double getOrigin() {
		return this.origin;
	}

	public void setOrigin(java.lang.Double origin){
		this.origin = origin;
	}
	
	@Column(name ="DELIVERY_MONEY",nullable=true,precision=12,scale=2)
	public java.lang.Double getDeliveryMoney(){
		return this.deliveryMoney;
	}

	public void setDeliveryMoney(java.lang.Double deliveryMoney){
		this.deliveryMoney = deliveryMoney;
	}
	
	@Column(name = "type",nullable=false,precision=1,scale=0)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
