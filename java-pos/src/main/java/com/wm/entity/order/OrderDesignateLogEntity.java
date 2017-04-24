package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单指派日志实体
 */
@Entity
@Table(name = "0085_order_designate_log", schema = "")
@SuppressWarnings("serial")
public class OrderDesignateLogEntity implements java.io.Serializable {
	/** id */
	private java.lang.Integer id;
	// 订单ID
	private java.lang.Integer orderId;
	//指派人id
	private java.lang.Integer designateId;
	//被指派的快递员的id
	private java.lang.Integer designeeId;
	/** createTime */
	private java.lang.Integer createTime;

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 11, scale = 0)
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	
	@Column(name ="ORDER_ID",nullable=false,precision=11,scale=0)
	public java.lang.Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}


	@Column(name ="CREATE_TIME",nullable=false,precision=11,scale=0)
	public java.lang.Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}

	@Column(name ="DESIGNATE_ID",nullable=false,precision=11,scale=0)
	public java.lang.Integer getDesignateId() {
		return designateId;
	}

	public void setDesignateId(java.lang.Integer designateId) {
		this.designateId = designateId;
	}

	@Column(name ="DESIGNEE_ID",nullable=false,precision=11,scale=0)
	public java.lang.Integer getDesigneeId() {
		return designeeId;
	}

	public void setDesigneeId(java.lang.Integer designeeId) {
		this.designeeId = designeeId;
	}
	

}
