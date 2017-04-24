package com.wm.entity.card;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wm.entity.merchant.MerchantEntity;

/**   
 * @Title: Entity
 * @Description: card
 * @author wuyong
 * @date 2015-01-07 11:03:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "card", schema = "")
@SuppressWarnings("serial")
public class CardEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**code*/
	private java.lang.String code;
	/**merchantId*/
	private MerchantEntity merchant;
	//private java.lang.Integer merchantId;
	/**orderId*/
	//private OrderEntity order;
	private java.lang.Integer orderId;
	/**credit*/
	private java.lang.Integer credit;
	/**consume*/
	private java.lang.String consume;
	/**endTime*/
	private java.lang.Integer endTime;
	/**标题*/
	private java.lang.String title;
	/**userId*/
	private java.lang.Integer userId;
	
	/**过期时间,yyyy-MM-dd,数据库未保存由该值,get方法通过endTime算出*/
	private java.lang.String endDate;
	/**是否已过期,数据库未保存由该值,get方法通过endTime算出*/
	private java.lang.Boolean overTime;
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(generator = "_assigned")
	@GenericGenerator(name="_assigned",strategy="assigned")
	@Column(name ="ID",nullable=false,precision=19,scale=0)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  code
	 */
	@Column(name ="CODE",nullable=false,length=30)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  code
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  merchantId
	 */
	/*@Column(name ="MERCHANT_ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  merchantId
	 */
	/*public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MERCHANT_ID", nullable = true)
	public MerchantEntity getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantEntity merchant) {
		this.merchant = merchant;
	}
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  orderId
	 */
	@Column(name ="ORDER_ID",nullable=true,precision=20,scale=0)
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
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = true)
	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}*/

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  credit
	 */
	@Column(name ="CREDIT",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCredit(){
		return this.credit;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  credit
	 */
	public void setCredit(java.lang.Integer credit){
		this.credit = credit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  consume
	 */
	@Column(name ="CONSUME",nullable=false,length=1)
	public java.lang.String getConsume(){
		return this.consume;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  consume
	 */
	public void setConsume(java.lang.String consume){
		this.consume = consume;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  endTime
	 */
	@Column(name ="END_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  endTime
	 */
	public void setEndTime(java.lang.Integer endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标题
	 */
	@Column(name ="TITLE",nullable=false,length=100)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
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
	 * 获取代金券过期日期yyy-MM-dd格式
	 * @author lfq
	 * @email  545987886@qq.com
	 * @return
	 */
	@Transient
	public java.lang.String getEndDate() {
		if (this.endDate!=null) {
			return this.endDate;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long times=this.getEndTime().longValue()*1000;
			String temp=sdf.format(new Date(times));
			return temp;
		}
	}
	
	/**
	 * 获取代金卷是否已经过期
	 * @author lfq
	 * @email  545987886@qq.com
	 * @return  过期时返回true，否则返回false
	 */
	@Transient
	public java.lang.Boolean getOverTime() {
		if(this.overTime!=null){
			return this.overTime;
		}else{
			Long now=(new Date()).getTime()/1000;
			return now>this.getEndTime().longValue();
		}
		
	}
	
	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}

	public void setOverTime(java.lang.Boolean overTime) {
		this.overTime = overTime;
	}
	
	
}
