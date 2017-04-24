package com.wm.entity.deduct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员日提成表
 * @author wuyong
 * @date 2015-09-01 11:43:13
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_deduct_log", schema = "")
public class DeductLogEntity implements java.io.Serializable {

	private static final long serialVersionUID = -6303432075161292055L;
	
	/**id*/
	private java.lang.Integer id;
	/**courierId*/
	private java.lang.Integer courierId;
	/**可拿提成的送餐份数*/
	private java.lang.Integer quantity = 0;
	/**提成金额*/
	private Double deduct = 0.0;
	/**统计日期，deduct_type=1时，代表统计月份,忽略日时分秒*/
	private java.lang.Integer accountDate;
	
	/**可拿提成的订单数，当deduct_type=1时，表示子网点月累计订单*/
	private Double orders = 0.0;
	
	/**
	 * 提成类型：0-快递员日提成(不包括众包订单)，1-管理层提成，2-一级子网点提成, 3-众包订单提成, 4-供应链司机提成, 5-供应链快递员提成
	 **/
	private Integer deductType = 0;
	
	/**奖励**/
	private Double reward = 0.0;
	
	/**餐补**/
	private Double mealSubsidy =  0.0;
	
	/**合计=订单提成+奖励+餐补**/
	private Double totalDeduct =  0.0;
	
	private Double orderDeduct = 0.0;
	
	private Integer agentId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
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
	 *@return: java.lang.Integer  courierId
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  courierId
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  可拿提成的送餐份数
	 */
	@Column(name ="QUANTITY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getQuantity(){
		return this.quantity;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  可拿提成的送餐份数
	 */
	public void setQuantity(java.lang.Integer quantity){
		this.quantity = quantity;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  deduct
	 */
	@Column(name ="DEDUCT",nullable=false,precision=10,scale=2)
	public Double getDeduct(){
		return this.deduct;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  deduct
	 */
	public void setDeduct(Double deduct){
		this.deduct = deduct;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  accountDate
	 */
	@Column(name ="ACCOUNT_DATE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getAccountDate(){
		return this.accountDate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  accountDate
	 */
	public void setAccountDate(java.lang.Integer accountDate){
		this.accountDate = accountDate;
	}

	@Column(name ="ORDERS",nullable=false,precision=10,scale=2)
	public Double getOrders() {
		return orders;
	}

	public void setOrders(Double orders) {
		this.orders = orders;
	}

	@Column(name ="DEDUCT_TYPE",nullable=false,precision=3,scale=0)
	public Integer getDeductType() {
		return deductType;
	}

	public void setDeductType(Integer deductType) {
		this.deductType = deductType;
	}

	@Column(name ="REWARD",nullable=false,precision=10,scale=2)
	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	@Column(name ="MEAL_SUBSIDY",nullable=false,precision=10,scale=2)
	public Double getMealSubsidy() {
		return mealSubsidy;
	}

	public void setMealSubsidy(Double mealSubsidy) {
		this.mealSubsidy = mealSubsidy;
	}

	@Column(name ="TOTAL_DEDUCT",nullable=false,precision=10,scale=2)
	public Double getTotalDeduct() {
		return totalDeduct;
	}

	public void setTotalDeduct(Double totalDeduct) {
		this.totalDeduct = totalDeduct;
	}

	@Column(name ="ORDER_DEDUCT",nullable=false,precision=10,scale=2)
	public Double getOrderDeduct() {
		return orderDeduct;
	}

	public void setOrderDeduct(Double orderDeduct) {
		this.orderDeduct = orderDeduct;
	}

	@Column(name ="AGENT_ID",nullable=true,precision=10,scale=0)
	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

}
