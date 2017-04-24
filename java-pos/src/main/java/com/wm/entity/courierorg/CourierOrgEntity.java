package com.wm.entity.courierorg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 关联表：快递员-组织架构
 * @author wuyong
 * @date 2015-08-28 09:14:44
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_org", schema = "")
@SuppressWarnings("serial")
public class CourierOrgEntity implements java.io.Serializable {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
	/**courierId*/
	private java.lang.Integer courierId;
	/**orgId*/
	private java.lang.Integer orgId;
	
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
	 *@return: java.lang.Integer  orgId
	 */
	@Column(name ="ORG_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOrgId(){
		return this.orgId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  orgId
	 */
	public void setOrgId(java.lang.Integer orgId){
		this.orgId = orgId;
	}
}
