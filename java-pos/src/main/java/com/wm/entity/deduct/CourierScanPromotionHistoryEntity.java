package com.wm.entity.deduct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员扫描支付推广历史记录表
 * @author wuyong
 * @date 2016-04-01 14:34:59
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_scan_promotion_history", schema = "")
@SuppressWarnings("serial")
public class CourierScanPromotionHistoryEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**用户ID*/
	private java.lang.Integer userId;
	/**快递员ID*/
	private java.lang.Integer courierId;
	/**充值金额*/
	private java.lang.Integer money;
	/**对应的扫码奖励规则ID*/
	private java.lang.Integer scanRuleId;
	/**充值时间*/
	private java.util.Date rehcargeTime;
	/**是否有奖励 0 没有 1 表示有*/
	private java.lang.String reward;
	
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
	 *@return: java.lang.Integer  用户ID
	 */
	@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用户ID
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员ID
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员ID
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  充值金额
	 */
	@Column(name ="MONEY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getMoney(){
		return this.money;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  充值金额
	 */
	public void setMoney(java.lang.Integer money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  对应的扫码奖励规则ID
	 */
	@Column(name ="SCAN_RULE_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getScanRuleId(){
		return this.scanRuleId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  对应的扫码奖励规则ID
	 */
	public void setScanRuleId(java.lang.Integer scanRuleId){
		this.scanRuleId = scanRuleId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  充值时间
	 */
	@Column(name ="REHCARGE_TIME",nullable=false)
	public java.util.Date getRehcargeTime(){
		return this.rehcargeTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  充值时间
	 */
	public void setRehcargeTime(java.util.Date rehcargeTime){
		this.rehcargeTime = rehcargeTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否有奖励 0 没有 1 表示有
	 */
	@Column(name ="REWARD",nullable=false,length=2)
	public java.lang.String getReward(){
		return this.reward;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否有奖励 0 没有 1 表示有
	 */
	public void setReward(java.lang.String reward){
		this.reward = reward;
	}
}
