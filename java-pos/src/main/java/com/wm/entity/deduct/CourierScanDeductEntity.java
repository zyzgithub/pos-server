package com.wm.entity.deduct;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员扫码推广提成表
 * @author wuyong
 * @date 2016-03-18 15:39:12
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_scan_deduct", schema = "")
@SuppressWarnings("serial")
public class CourierScanDeductEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**快递员类型，1=平台快递员，2=代理商快递员，3=众包快递员*/
	private java.lang.Integer courierType;
	/**关联0085_courier_scan_rule的id*/
	private java.lang.Integer scanRuleId;
	/**快递员提成，单位元*/
	private java.lang.Double deduct;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员类型，1=平台快递员，2=代理商快递员，3=众包快递员
	 */
	@Column(name ="COURIER_TYPE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getCourierType(){
		return this.courierType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员类型，1=平台快递员，2=代理商快递员，3=众包快递员
	 */
	public void setCourierType(java.lang.Integer courierType){
		this.courierType = courierType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  关联0085_courier_scan_rule的id
	 */
	@Column(name ="SCAN_RULE_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getScanRuleId(){
		return this.scanRuleId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  关联0085_courier_scan_rule的id
	 */
	public void setScanRuleId(java.lang.Integer scanRuleId){
		this.scanRuleId = scanRuleId;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  快递员提成，单位元
	 */
	@Column(name ="DEDUCT",nullable=false,precision=7,scale=2)
	public java.lang.Double getDeduct(){
		return this.deduct;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  快递员提成，单位元
	 */
	public void setDeduct(java.lang.Double deduct){
		this.deduct = deduct;
	}
}
