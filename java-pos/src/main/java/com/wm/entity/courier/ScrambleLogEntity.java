package com.wm.entity.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.core.util.DateUtils;

/**
 * 快递员抢单日志
 * @author Simon
 */
@Entity
@Table(name = "0085_courier_scramble_log", schema = "")
public class ScrambleLogEntity implements java.io.Serializable {

	private static final long serialVersionUID = 9150906621961319099L;
	
	private java.lang.Integer id;
	private java.lang.Integer courierId;
	private java.lang.Integer orderId;
	private java.lang.Integer success;
	private java.lang.String msg;
	private java.lang.Integer createTime;
	
	
	ScrambleLogEntity(){
		
	}
	
	public ScrambleLogEntity(Integer courierId, Object orderId, boolean success, String msg) {
		this.courierId = courierId;
		if(orderId != null){
			this.orderId = Integer.parseInt(orderId.toString());
		} else {
			this.orderId = 0;
		}
		this.success = success ? 1 : 0;
		this.msg = msg;
		this.createTime = DateUtils.getSeconds();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId() {
		return courierId;
	}
	public void setCourierId(java.lang.Integer courierId) {
		this.courierId = courierId;
	}
	
	@Column(name ="ORDER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name ="SUCCESS",nullable=false,length=1)
	public Integer isSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	
	@Column(name ="MSG",nullable=false,length=255)
	public java.lang.String getMsg() {
		return msg;
	}
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}
	
	

}
