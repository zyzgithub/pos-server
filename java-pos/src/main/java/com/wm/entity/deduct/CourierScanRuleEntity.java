package com.wm.entity.deduct;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员扫码推广提成规则表
 * @author wuyong
 * @date 2016-03-18 15:39:42
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_scan_rule", schema = "")
@SuppressWarnings("serial")
public class CourierScanRuleEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**充值金额*/
	private java.lang.Integer totalCharge;
	/**是否有效，0=无效，1=有效*/
	private java.lang.Integer invalid;
	
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
	 *@return: java.lang.Integer  充值金额
	 */
	@Column(name ="TOTAL_CHARGE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTotalCharge(){
		return this.totalCharge;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  充值金额
	 */
	public void setTotalCharge(java.lang.Integer totalCharge){
		this.totalCharge = totalCharge;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否有效，0=无效，1=有效
	 */
	@Column(name ="INVALID",nullable=false,precision=3,scale=0)
	public java.lang.Integer getInvalid(){
		return this.invalid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否有效，0=无效，1=有效
	 */
	public void setInvalid(java.lang.Integer invalid){
		this.invalid = invalid;
	}
}
