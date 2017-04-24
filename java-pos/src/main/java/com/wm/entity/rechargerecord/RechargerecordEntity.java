package com.wm.entity.rechargerecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: recharge_record
 * @author wuyong
 * @date 2015-01-07 10:02:47
 * @version V1.0   
 *
 */
@Entity
@Table(name = "recharge_record", schema = "")
@SuppressWarnings("serial")
public class RechargerecordEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	//private java.lang.Integer userId;
	/**money*/
	private java.lang.Double money;
	/**service*/
	private java.lang.String service;
	/**rechargeTime*/
	private java.lang.Integer rechargeTime = DateUtils.getSeconds();
	/**payId*/
//	private PayEntity pay;
	
	private java.lang.String payId;
	
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
	 *@return: java.lang.Integer  userId
	 */
	/*@Column(name ="USER_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	/*public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name ="MONEY",nullable=false,precision=10,scale=2)
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
	 *@return: java.lang.String  service
	 */
	@Column(name ="SERVICE",nullable=false,length=9)
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
	 *@return: java.lang.Integer  rechargeTime
	 */
	@Column(name ="RECHARGE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getRechargeTime(){
		return this.rechargeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  rechargeTime
	 */
	public void setRechargeTime(java.lang.Integer rechargeTime){
		this.rechargeTime = rechargeTime;
	}
	
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  payId
	 */
	@Column(name ="PAY_ID",nullable=false,length=50)
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
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "PAY_ID", nullable = true)
//	public PayEntity getPay() {
//		return pay;
//	}
//
//	public void setPay(PayEntity pay) {
//		this.pay = pay;
//	}
}
