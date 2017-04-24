package com.wm.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单的催单
 */
@Entity
@Table(name = "0085_order_reminder", schema = "")
@SuppressWarnings("serial")
public class OrderReminderEntity implements java.io.Serializable {
	/** id */
	private java.lang.Integer id;
	// 订单ID
	private java.lang.Integer orderId;
	/** createTime */
	private java.lang.Integer createTime;//催单time
	private String remindDesc;//催单备注
	private String resolver;//ENUM('merchant', 'courier') 处理人， 商家或快递员
	private Integer resolverTime;//处理time
	private String resolverDesc;//处理desc
	

	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 20, scale = 0)
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

	
	@Column(name = "ORDER_ID", nullable = false )
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


	@Column(name ="REMIND_DESC")
	public String getRemindDesc() {
		return remindDesc;
	}

	public void setRemindDesc(String remindDesc) {
		this.remindDesc = remindDesc;
	}

	@Column(name ="RESOLVER")
	public String getResolver() {
		return resolver;
	}

	public void setResolver(String resolver) {
		this.resolver = resolver;
	}

	@Column(name ="RESOLVER_TIME",nullable=true,precision=11,scale=0)
	public Integer getResolverTime() {
		return resolverTime;
	}

	public void setResolverTime(Integer resolverTime) {
		this.resolverTime = resolverTime;
	}


	@Column(name ="RESOLVER_DESC")
	public String getResolverDesc() {
		return resolverDesc;
	}

	public void setResolverDesc(String resolverDesc) {
		this.resolverDesc = resolverDesc;
	}

	

}
