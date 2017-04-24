package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

/**   
 * @Title: Entity
 * @Description: 可抢订单临时表
 * @author wuyong
 * @date 2015-09-16 16:34:59
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_pushed_order", schema = "")
@SuppressWarnings("serial")
public class PushedOrderEntity implements java.io.Serializable {
	
	/**id*/
	private java.lang.Integer id;
	/**orderId*/
	private java.lang.Integer orderId;
	/**最近更新时间*/
	private java.lang.Integer latestUpdateTime;
	/**pushedCourier*/
	private java.lang.Integer pushedCourier;
	/**createTime*/
	private java.lang.Integer createTime;
	
	PushedOrderEntity(){
		
	}
	
	public PushedOrderEntity(Integer orderId, Integer courierId) {
		this.orderId = orderId;
		this.pushedCourier = courierId;
		this.createTime = DateUtils.getSeconds();
		this.latestUpdateTime = DateUtils.getSeconds();
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  orderId
	 */
	@Column(name ="ORDER_ID",nullable=false,precision=10,scale=0)
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  最近更新时间
	 */
	@Column(name ="LATEST_UPDATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getLatestUpdateTime(){
		return this.latestUpdateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  最近更新时间
	 */
	public void setLatestUpdateTime(java.lang.Integer latestUpdateTime){
		this.latestUpdateTime = latestUpdateTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  pushedCourier
	 */
	@Column(name ="PUSHED_COURIER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPushedCourier(){
		return this.pushedCourier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  pushedCourier
	 */
	public void setPushedCourier(java.lang.Integer pushedCourier){
		this.pushedCourier = pushedCourier;
	}
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

	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}
	
}
