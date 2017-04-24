package com.wm.entity.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.wm.entity.order.OrderEntity;

/**   
 * @Title: Entity
 * @Description: 众包订单表
 * @author jiangpingmei
 * @date 2015-12-28 17:20:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "tom_order_crowdsourcing", schema = "")
@SuppressWarnings("serial")
public class TomOrderCrowdsourcingEntity implements java.io.Serializable  {
	/**订单id,关联order表主键id*/
	private java.lang.Integer orderId;
	/**众包类型名称*/
	private java.lang.String crowdsourcingName;
	/**众包配送费*/
	private java.lang.Integer crowdsourcingDeliveryFee;
	/**众包快递员提成*/
	private java.lang.Integer crowdsourcingCourierDeduct;
	/**经度*/
	private java.lang.Double longitude;
	/**纬度*/
	private java.lang.Double latitude;
	
	@Id
	@Column(name="order_id",nullable=false,precision=20,scale=0)
	public java.lang.Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name="crowdsourcing_name",nullable=false,length=128)
	public java.lang.String getCrowdsourcingName() {
		return crowdsourcingName;
	}
	
	public void setCrowdsourcingName(java.lang.String crowdsourcingName) {
		this.crowdsourcingName = crowdsourcingName;
	}
	
	@Column(name="crowdsourcing_delivery_fee",nullable=false,precision=11,scale=0)
	public java.lang.Integer getCrowdsourcingDeliveryFee() {
		return crowdsourcingDeliveryFee;
	}
	
	public void setCrowdsourcingDeliveryFee(
			java.lang.Integer crowdsourcingDeliveryFee) {
		this.crowdsourcingDeliveryFee = crowdsourcingDeliveryFee;
	}
	
	@Column(name="crowdsourcing_courier_deduct",nullable=false,precision=11,scale=0)
	public java.lang.Integer getCrowdsourcingCourierDeduct() {
		return crowdsourcingCourierDeduct;
	}
	
	public void setCrowdsourcingCourierDeduct(
			java.lang.Integer crowdsourcingCourierDeduct) {
		this.crowdsourcingCourierDeduct = crowdsourcingCourierDeduct;
	}

	@Column(name="longitude",nullable=false,precision=10,scale=6)
	public java.lang.Double getLongitude() {
		return longitude;
	}

	public void setLongitude(java.lang.Double longitude) {
		this.longitude = longitude;
	}

	@Column(name="latitude",nullable=false,precision=10,scale=6)
	public java.lang.Double getLatitude() {
		return latitude;
	}

	public void setLatitude(java.lang.Double latitude) {
		this.latitude = latitude;
	}
	
	
}
