package com.wm.entity.transfers;

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
 * @Description: transfers
 * @author wuyong
 * @date 2015-01-07 10:05:35
 * @version V1.0   
 *
 */
@Entity
@Table(name = "transfers", schema = "")
@SuppressWarnings("serial")
public class TransfersEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**payUser*/
	private WUserEntity payUser;
	//private java.lang.Integer payUser;
	/**transferUser*/
	private WUserEntity transferUser;
	//private java.lang.Integer transferUser;
	/**createTime*/
	private java.lang.Integer createTime = DateUtils.getSeconds();
	/**money*/
	private java.lang.Double money;
	
	// 订单排号
	private String orderNum;
	
	@Column(name ="ORDER_NUM",nullable=true,length=20)
	public java.lang.String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(java.lang.String orderNum) {
		this.orderNum = orderNum;
	}
	
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
	 *@return: java.lang.Integer  payUser
	 */
	/*@Column(name ="PAY_USER",nullable=false,precision=19,scale=0)
	public java.lang.Integer getPayUser(){
		return this.payUser;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  payUser
	 */
	/*public void setPayUser(java.lang.Integer payUser){
		this.payUser = payUser;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAY_USER", nullable = true)
	public WUserEntity getPayUser() {
		return payUser;
	}

	public void setPayUser(WUserEntity payUser) {
		this.payUser = payUser;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  transferUser
	 */
	/*@Column(name ="TRANSFER_USER",nullable=false,precision=19,scale=0)
	public java.lang.Integer getTransferUser(){
		return this.transferUser;
	}*/
	
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  transferUser
	 */
	/*public void setTransferUser(java.lang.Integer transferUser){
		this.transferUser = transferUser;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSFER_USER", nullable = true)
	public WUserEntity getTransferUser() {
		return transferUser;
	}

	public void setTransferUser(WUserEntity transferUser) {
		this.transferUser = transferUser;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=19,scale=0)
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
}
