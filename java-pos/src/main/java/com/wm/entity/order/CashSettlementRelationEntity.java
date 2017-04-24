package com.wm.entity.order;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 现金订单与结算订单关联表
 * @author wuyong
 * @date 2016-05-20 20:18:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_cash_settlement_relation", schema = "")
@SuppressWarnings("serial")
public class CashSettlementRelationEntity implements java.io.Serializable {
	/**现金订单ID*/
	private java.lang.Integer cashOrderId;
	/**结算订单ID 默认值为0 表示未指定结算订单*/
	private java.lang.Integer settlementOrderId;
	
	private java.lang.Integer merchantId;
	
	private java.lang.Integer cashierId;
	
	/**是否结算 0 表示未结算 1 表示已结算*/
	private java.lang.String isSettlemented;
	/**订单金额*/
	private java.lang.Double money;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**更新时间*/
	private java.lang.Integer updateTime;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  现金订单ID
	 */
	@Id
	@Column(name ="CASH_ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCashOrderId(){
		return this.cashOrderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  现金订单ID
	 */
	public void setCashOrderId(java.lang.Integer cashOrderId){
		this.cashOrderId = cashOrderId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  结算订单ID 默认值为0 表示未指定结算订单
	 */
	@Column(name ="SETTLEMENT_ORDER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getSettlementOrderId(){
		return this.settlementOrderId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  结算订单ID 默认值为0 表示未指定结算订单
	 */
	public void setSettlementOrderId(java.lang.Integer settlementOrderId){
		this.settlementOrderId = settlementOrderId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否结算 0 表示未结算 1 表示已结算
	 */
	@Column(name ="IS_SETTLEMENTED",nullable=false,length=2)
	public java.lang.String getIsSettlemented(){
		return this.isSettlemented;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否结算 0 表示未结算 1 表示已结算
	 */
	public void setIsSettlemented(java.lang.String isSettlemented){
		this.isSettlemented = isSettlemented;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  订单金额
	 */
	@Column(name ="MONEY",nullable=false,precision=10,scale=2)
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  订单金额
	 */
	public void setMoney(java.lang.Double money){
		this.money = money;
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  更新时间
	 */
	@Column(name ="UPDATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  更新时间
	 */
	public void setUpdateTime(java.lang.Integer updateTime){
		this.updateTime = updateTime;
	}

	@Column(name ="merchant_id",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(java.lang.Integer merchantId) {
		this.merchantId = merchantId;
	}

	@Column(name ="cashier_id",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCashierId() {
		return cashierId;
	}

	public void setCashierId(java.lang.Integer cashierId) {
		this.cashierId = cashierId;
	}
}
