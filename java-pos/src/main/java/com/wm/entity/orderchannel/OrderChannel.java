package com.wm.entity.orderchannel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author 订单渠道
 *
 */
@Entity
@Table(name = "0085_order_channel", schema = "")
@SuppressWarnings("serial")
public class OrderChannel implements java.io.Serializable {
	private java.lang.Integer id;
	private java.lang.Integer orderId;
	private java.lang.String channelId;
	private java.lang.String channelMark;
	private java.lang.String channelFlag;
	private java.lang.String openId;
	private java.lang.Double money;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	@Column(name ="order_id")
	public java.lang.Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}
	@Column(name ="channel_id")
	public java.lang.String getChannelId() {
		return channelId;
	}
	public void setChannelId(java.lang.String channelId) {
		this.channelId = channelId;
	}
	@Column(name ="channel_mark")
	public java.lang.String getChannelMark() {
		return channelMark;
	}
	public void setChannelMark(java.lang.String channelMark) {
		this.channelMark = channelMark;
	}
	@Column(name ="channel_flag")
	public java.lang.String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(java.lang.String channelFlag) {
		this.channelFlag = channelFlag;
	}
	@Column(name ="openId")
	public java.lang.String getOpenId() {
		return openId;
	}
	public void setOpenId(java.lang.String openId) {
		this.openId = openId;
	}
	@Column(name ="money",nullable=false,precision=10,scale=2)
	public java.lang.Double getMoney() {
		return money;
	}
	public void setMoney(java.lang.Double money) {
		this.money = money;
	}
}
