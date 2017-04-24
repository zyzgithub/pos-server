package com.wm.entity.courierpositionlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员调岗记录表
 * @author wuyong
 * @date 2015-09-01 17:01:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_position_log", schema = "")
@SuppressWarnings("serial")
public class CourierPositionLogEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**courierId*/
	private java.lang.Integer courierId;
	/**oldPositionId*/
	private java.lang.Integer oldPositionId;
	/**newPositionId*/
	private java.lang.Integer newPositionId;
	/**changeTime*/
	private java.lang.Integer changeTime;
	/**调薪操作人ID，0代表系统自动调*/
	private java.lang.Integer changeOperator;
	
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
	 *@return: java.lang.Integer  oldPositionId
	 */
	@Column(name ="OLD_POSITION_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOldPositionId(){
		return this.oldPositionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  oldPositionId
	 */
	public void setOldPositionId(java.lang.Integer oldPositionId){
		this.oldPositionId = oldPositionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  newPositionId
	 */
	@Column(name ="NEW_POSITION_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getNewPositionId(){
		return this.newPositionId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  newPositionId
	 */
	public void setNewPositionId(java.lang.Integer newPositionId){
		this.newPositionId = newPositionId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  changeTime
	 */
	@Column(name ="CHANGE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getChangeTime(){
		return this.changeTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  changeTime
	 */
	public void setChangeTime(java.lang.Integer changeTime){
		this.changeTime = changeTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  调薪操作人ID，0代表系统自动调
	 */
	@Column(name ="CHANGE_OPERATOR",nullable=false,precision=10,scale=0)
	public java.lang.Integer getChangeOperator(){
		return this.changeOperator;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  调薪操作人ID，0代表系统自动调
	 */
	public void setChangeOperator(java.lang.Integer changeOperator){
		this.changeOperator = changeOperator;
	}
}
