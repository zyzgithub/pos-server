package com.wm.entity.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

/**   
 * @Title: Entity
 * @Description: 统计网点每天订单量、人均订单量
 * @author wuyong
 * @date 2015-09-29 20:14:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_org_statistics_dayly", schema = "")
@SuppressWarnings("serial")
public class OrgStatisticsDaylyEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**网点ID*/
	private java.lang.Integer orgId;
	/**日订单总量*/
	private java.lang.Integer daylyTotalOrder;
	/**日快递员数*/
	private java.lang.Integer daylyTotalCourier;
	/**日人均订单量*/
	private java.lang.Double daylyOrder;
	
	private Integer updateDate;
	
	OrgStatisticsDaylyEntity(){
		
	}
	
	public OrgStatisticsDaylyEntity(Integer orgId, Integer daylyTotalOrder, Integer daylyTotalCourier, Double daylyOrder){
		this.orgId = orgId;
		this.daylyTotalOrder = daylyTotalOrder;
		this.daylyTotalCourier = daylyTotalCourier;
		this.daylyOrder = daylyOrder;
		this.updateDate = Integer.parseInt(DateTime.now().minusDays(1).getMillis()/1000+"");
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  网点ID
	 */
	@Column(name ="ORG_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getOrgId(){
		return this.orgId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  网点ID
	 */
	public void setOrgId(java.lang.Integer orgId){
		this.orgId = orgId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日订单总量
	 */
	@Column(name ="DAYLY_TOTAL_ORDER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalOrder(){
		return this.daylyTotalOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日订单总量
	 */
	public void setDaylyTotalOrder(java.lang.Integer daylyTotalOrder){
		this.daylyTotalOrder = daylyTotalOrder;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  日快递员数
	 */
	@Column(name ="DAYLY_TOTAL_COURIER",nullable=false,precision=10,scale=0)
	public java.lang.Integer getDaylyTotalCourier(){
		return this.daylyTotalCourier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  日快递员数
	 */
	public void setDaylyTotalCourier(java.lang.Integer daylyTotalCourier){
		this.daylyTotalCourier = daylyTotalCourier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  日人均订单量
	 */
	@Column(name ="DAYLY_ORDER",nullable=false,precision=11,scale=2)
	public java.lang.Double getDaylyOrder(){
		return this.daylyOrder;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  日人均订单量
	 */
	public void setDaylyOrder(java.lang.Double daylyOrder){
		this.daylyOrder = daylyOrder;
	}

	@Column(name ="UPDATE_DATE",nullable=false,precision=10,scale=0)
	public Integer getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Integer updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
