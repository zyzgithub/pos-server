package com.wm.entity.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 交易信息
 * @author wuyong
 * @date 2015-03-16 09:54:58
 * @version V1.0   
 *
 */
@Entity
@Table(name = "other_order", schema = "")
@SuppressWarnings("serial")
public class OtherEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**交易号*/
	private java.lang.String vid;
	/**订单号*/
	private java.lang.String otherOrderNo;
	/**交易金额*/
	private java.lang.Double otherMoney;
	/**交易状态*/
	private java.lang.String state;
	/**订单ID*/
	private java.lang.String orderId;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易号
	 */
	@Column(name ="VID",nullable=false,length=32)
	public java.lang.String getVid(){
		return this.vid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易号
	 */
	public void setVid(java.lang.String vid){
		this.vid = vid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单号
	 */
	@Column(name ="OTHER_ORDER_NO",nullable=false,length=32)
	public java.lang.String getOtherOrderNo(){
		return this.otherOrderNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单号
	 */
	public void setOtherOrderNo(java.lang.String otherOrderNo){
		this.otherOrderNo = otherOrderNo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  交易金额
	 */
	@Column(name ="OTHER_MONEY",nullable=false,precision=22)
	public java.lang.Double getOtherMoney(){
		return this.otherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  交易金额
	 */
	public void setOtherMoney(java.lang.Double otherMoney){
		this.otherMoney = otherMoney;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  交易状态
	 */
	@Column(name ="STATE",nullable=false,length=5)
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  交易状态
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单ID
	 */
	@Column(name ="ORDER_ID",nullable=true,length=255)
	public java.lang.String getOrderId(){
		return this.orderId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单ID
	 */
	public void setOrderId(java.lang.String orderId){
		this.orderId = orderId;
	}
}
