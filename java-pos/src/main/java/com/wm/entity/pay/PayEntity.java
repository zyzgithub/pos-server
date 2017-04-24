package com.wm.entity.pay;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.core.util.DateUtils;

/**   
 * @Title: Entity
 * @Description: pay
 * @author wuyong
 * @date 2015-01-07 10:01:54
 * @version V1.0   
 *
 */
@Entity
@Table(name = "pay", schema = "")
@SuppressWarnings("serial")
@DynamicUpdate(true)
@DynamicInsert(true)
public class PayEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**vid*/
	private java.lang.String vid;
	/**orderId*/
	//private OrderEntity order;
	private java.lang.Integer orderId;
	/**bank*/
	private java.lang.String bank;
	/**money*/
	private java.lang.Double money = 0.0;
	/**currency*/
	private java.lang.String currency = "CNY";
	/**service*/
	private java.lang.String service;
	/**createTime*/
	private java.lang.Integer createTime = DateUtils.getSeconds();
	
	private java.lang.String state="unpay";
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	@Id
	@GeneratedValue(generator = "_assigned")
	@GenericGenerator(name="_assigned",strategy="assigned")
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  vid
	 */
	@Column(name ="VID",nullable=true,length=32)
	public java.lang.String getVid(){
		return this.vid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  vid
	 */
	public void setVid(java.lang.String vid){
		this.vid = vid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  orderId
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=20,scale=0)
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
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = true)
	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}
	*/
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bank
	 */
	@Column(name ="BANK",nullable=true,length=32)
	public java.lang.String getBank(){
		return this.bank;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bank
	 */
	public void setBank(java.lang.String bank){
		this.bank = bank;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name ="MONEY",nullable=true,precision=10,scale=2)
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  money
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  currency
	 */
	@Column(name ="CURRENCY",nullable=false,length=3)
	public java.lang.String getCurrency(){
		return this.currency;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  currency
	 */
	public void setCurrency(java.lang.String currency){
		this.currency = currency;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  service
	 */
	@Column(name ="SERVICE",nullable=false,length=16)
	public java.lang.String getService(){
		return this.service;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  service
	 */
	public void setService(java.lang.String service){
		this.service = service;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
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
	
	@Column(name ="STATE",nullable=false,length=10)
	public java.lang.String getState(){
		return this.state;
	}

	public void setState(java.lang.String getState){
		this.state = getState;
	}
}
