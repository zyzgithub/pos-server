package com.wm.entity.dineorder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wm.entity.merchant.MerchantEntity;

/**   
 * @Title: Entity
 * @Description: 堂食订单
 * @author wuyong
 * @date 2015-04-01 16:11:12
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dine_order", schema = "")
@SuppressWarnings("serial")
public class DineOrderEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**总价*/
	private java.lang.Double origin;
	/**创建时间*/
	private java.lang.Integer createTime;
	/**完成时间*/
	private java.lang.Integer completeTime;
	/**送达时间*/
	private java.lang.String timeRemark;
	/**商家id*/
	private MerchantEntity merchant;
	/**订单排号*/
	private java.lang.String orderNum;
	/**订单号*/
	private java.lang.String payId;
	
	private java.lang.Integer cityId;
	/**订单状态 1.下单成功 2. 制作完成 3. 配送成功*/
	private java.lang.Integer dineType;
	/**下单完成时间*/
	private java.lang.Integer successTime;
	/**制作完成时间*/
	private java.lang.Integer acceptTime;
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
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  总价
	 */
	@Column(name ="ORIGIN",nullable=true,precision=10,scale=2)
	public java.lang.Double getOrigin(){
		return this.origin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  总价
	 */
	public void setOrigin(java.lang.Double origin){
		this.origin = origin;
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  完成时间
	 */
	@Column(name ="COMPLETE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCompleteTime(){
		return this.completeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  完成时间
	 */
	public void setCompleteTime(java.lang.Integer completeTime){
		this.completeTime = completeTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  送达时间
	 */
	@Column(name ="TIME_REMARK",nullable=true,length=50)
	public java.lang.String getTimeRemark(){
		return this.timeRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  送达时间
	 */
	public void setTimeRemark(java.lang.String timeRemark){
		this.timeRemark = timeRemark;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MERCHANT_ID", nullable = true)
	public MerchantEntity getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantEntity merchant) {
		this.merchant = merchant;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单排号
	 */
	@Column(name ="ORDER_NUM",nullable=true,length=10)
	public java.lang.String getOrderNum(){
		return this.orderNum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单排号
	 */
	public void setOrderNum(java.lang.String orderNum){
		this.orderNum = orderNum;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单号
	 */
	@Column(name ="PAY_ID",nullable=true,length=20)
	public java.lang.String getPayId(){
		return this.payId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单号
	 */
	public void setPayId(java.lang.String payId){
		this.payId = payId;
	}
	
	@Column(name ="CITY_ID",nullable=true,length=11)
	public java.lang.Integer getCityId() {
		return cityId;
	}

	public void setCityId(java.lang.Integer cityId) {
		this.cityId = cityId;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  订单类型
	 */
	@Column(name ="dine_type",nullable=true)
	public java.lang.Integer getDineType() {
		return dineType;
	}

	public void setDineType(java.lang.Integer dineType) {
		this.dineType = dineType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  下单成功时间
	 */
	@Column(name ="success_time",nullable=true)
	public java.lang.Integer getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(java.lang.Integer successTime) {
		this.successTime = successTime;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  制作完成时间
	 */
	@Column(name ="accept_time",nullable=true)
	public java.lang.Integer getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(java.lang.Integer acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	
}
