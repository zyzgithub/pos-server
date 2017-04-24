package com.wm.entity.flow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: flow
 * @author wuyong
 * @date 2015-01-07 09:57:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "flow", schema = "")
@SuppressWarnings("serial")
public class FlowEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private java.lang.Integer userId;
	/**detailId*/
	private java.lang.Integer detailId;
	/**detail*/
	private java.lang.String detail;
	/**money*/
	private java.lang.Double money;
	/**action*/
	private java.lang.String action;
	/**createTime*/
	private java.lang.Integer createTime;
	/**type*/
	private java.lang.String type;
	
	/**操作前余额*/
	private Double preMoney;
	/**操作后余额*/
	private Double postMoney;
	
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
	 *@return: java.lang.Integer  userId
	 */
	@Column(name ="USER_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  userId
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  detailId
	 */
	@Column(name ="DETAIL_ID",nullable=true,precision=19,scale=0)
	public java.lang.Integer getDetailId(){
		return this.detailId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  detailId
	 */
	public void setDetailId(java.lang.Integer detailId){
		this.detailId = detailId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  detail
	 */
	@Column(name ="DETAIL",nullable=true,length=255)
	public java.lang.String getDetail(){
		return this.detail;
	}

	

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  detail
	 */
	public void setDetail(java.lang.String detail){
		this.detail = detail;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  money
	 */
	@Column(name ="MONEY",nullable=false,precision=12,scale=4)
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
	 *@return: java.lang.String  action
	 */
	@Column(name ="ACTION",nullable=false)
	public java.lang.String getAction(){
		return this.action;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  action
	 */
	public void setAction(java.lang.String action){
		this.action = action;
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
	@Column(name ="type",nullable=true,length=20)
	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	@Column(name ="PRE_MONEY",nullable=false,precision=12,scale=4)
	public Double getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(Double preMoney) {
		this.preMoney = preMoney;
	}

	@Column(name ="POST_MONEY",nullable=false,precision=12,scale=4)
	public Double getPostMoney() {
		return postMoney;
	}

	public void setPostMoney(Double postMoney) {
		this.postMoney = postMoney;
	}
	
	/**
	 * type
	 */
	public static final class Type {
		// 收入
		public static final String INCOME = "income";
		// 支出
		public static final String PAY = "pay";
		
	}
	
	/**
	 * action
	 */
	public static final class Action {
		// 购买
		public static final String BUY = "buy";
		// 收入
		public static final String INCOME = "income";
		// 充值
		public static final String RECHARGE = "recharge";
		// 商家订单收入
		public static final String ORDERINCOME = "orderIncome";
		// 供应链商家订单收入
		public static final String SUPPLYORDERINCOME = "supplyOrderIncome";
		// 转账
		public static final String TRANSFER = "transfer";
		// 退款
		public static final String REFUND = "refund";
		// 快递员提成
		public static final String DEDUCT = "deduct";
		// 商家返点收入
		public static final String MERCHANTREBATE = "merchantRebate";
		// 快递员返点
		public static final String COURIERREBATE = "courierRebate";
		// 商家会员充值
		public static final String MERCHANTRECHARGE = "merchantRecharge";
		// 商家会员退款
		public static final String MERCHANTREFUND = "merchantrefund";
		// 代理商扣点收入
		public static final String AGENTDEDUCTION = "agentDeduction";
		// 代理商返点收入
		public static final String AGENTREBATE = "agentRebate";
		// 连锁商家流水
		public static final String MULTIINCOME = "multiIncome";
		// 连锁商家流水
		public static final String POSBUYOUT = "posBuyout";
		
	}

}
