package com.wm.entity.couriermerchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: courier_merchant
 * @author wuyong
 * @date 2015-09-23 17:51:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_merchant", schema = "")
@SuppressWarnings("serial")
public class CourierMerchantEntity implements java.io.Serializable {
	/**快递员id*/
	private java.lang.Integer courierId;
	/**商家id*/
	private java.lang.Integer merchantId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员id
	 */
	@Id
	@Column(name ="COURIER_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCourierId(){
		return this.courierId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员id
	 */
	public void setCourierId(java.lang.Integer courierId){
		this.courierId = courierId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家id
	 */
	@Id
	@Column(name ="MERCHANT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家id
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
}
