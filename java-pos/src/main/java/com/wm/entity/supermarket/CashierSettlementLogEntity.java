package com.wm.entity.supermarket;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 收银员结算日志
 * @author wuyong
 * @date 2016-06-07 09:44:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_cashier_settlement_log", schema = "")
@SuppressWarnings("serial")
public class CashierSettlementLogEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**商家ID*/
	private java.lang.Integer merchantId;
	/**收银员ID*/
	private java.lang.Integer cashierId;
	/**结算订单ID*/
	private java.lang.Integer settlementOrderId;
	/**金额*/
	private java.lang.Double money;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**收银员清点金额*/
	private java.lang.Double cash;
	/**结算支付状态*/
	private java.lang.String isPaid;
	/**更新时间*/
	private java.lang.Integer paidTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家ID
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家ID
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
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
	 *@return: java.lang.Integer  结算订单ID
	 */
	@Column(name ="SETTLEMENT_ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getSettlementOrderId(){
		return this.settlementOrderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  结算订单ID
	 */
	public void setSettlementOrderId(java.lang.Integer settlementOrderId){
		this.settlementOrderId = settlementOrderId;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  金额
	 */
	@Column(name ="MONEY",nullable=false,precision=10,scale=2)
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  金额
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true,precision=10,scale=0)
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

	@Column(name ="CASH",nullable=false,precision=10,scale=2)
	public java.lang.Double getCash() {
		return cash;
	}

	public void setCash(java.lang.Double cash) {
		this.cash = cash;
	}

	@Column(name ="IS_PAID",nullable=false,length=2)
	public java.lang.String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(java.lang.String isPaid) {
		this.isPaid = isPaid;
	}

	@Column(name ="PAID_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(java.lang.Integer paidTime) {
		this.paidTime = paidTime;
	}
	
	
}
