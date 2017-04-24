package com.wm.entity.courierinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 快递员用户信息
 * @author wuyong
 * @date 2015-09-01 17:09:34
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_info", schema = "")
@SuppressWarnings("serial")
public class CourierInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**快递员id*/
	private java.lang.Integer courierId;
	/**工号,格式为：区号(4位或5位)+五位编号，编号从10000开始，如02010000,075512345*/
	private java.lang.String jobId;
	/**身份证正面*/
	private java.lang.String frontIdCardImg;
	/**身份证背面*/
	private java.lang.String backIdCardImg;
	/**手持身份证正面*/
	private java.lang.String holdFrontIdCardImg;
	/**快递员月基本工资*/
	private java.lang.Integer salary;
	
	private String idCard;
	
	private int courierType;
	
	private Integer bindUserId;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
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
	 *@return: java.lang.Integer  快递员id
	 */
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工号,格式为：区号(4位或5位)+五位编号，编号从10000开始，如02010000,075512345
	 */
	@Column(name ="JOB_ID",nullable=false,length=11)
	public java.lang.String getJobId(){
		return this.jobId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工号,格式为：区号(4位或5位)+五位编号，编号从10000开始，如02010000,075512345
	 */
	public void setJobId(java.lang.String jobId){
		this.jobId = jobId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证正面
	 */
	@Column(name ="FRONT_ID_CARD_IMG",nullable=false,length=255)
	public java.lang.String getFrontIdCardImg(){
		return this.frontIdCardImg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证正面
	 */
	public void setFrontIdCardImg(java.lang.String frontIdCardImg){
		this.frontIdCardImg = frontIdCardImg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证背面
	 */
	@Column(name ="BACK_ID_CARD_IMG",nullable=false,length=255)
	public java.lang.String getBackIdCardImg(){
		return this.backIdCardImg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证背面
	 */
	public void setBackIdCardImg(java.lang.String backIdCardImg){
		this.backIdCardImg = backIdCardImg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手持身份证正面
	 */
	@Column(name ="HOLD_FRONT_ID_CARD_IMG",nullable=false,length=255)
	public java.lang.String getHoldFrontIdCardImg(){
		return this.holdFrontIdCardImg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手持身份证正面
	 */
	public void setHoldFrontIdCardImg(java.lang.String holdFrontIdCardImg){
		this.holdFrontIdCardImg = holdFrontIdCardImg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员月基本工资
	 */
	@Column(name ="SALARY",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSalary(){
		return this.salary;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递员月基本工资
	 */
	public void setSalary(java.lang.Integer salary){
		this.salary = salary;
	}

	@Column(name ="id_card",nullable=false,length=18)
	public String getIdCard() {
		return idCard;
	}

	
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name ="courier_type", nullable=false,precision=10,scale=0)
	public int getCourierType() {
		return courierType;
	}

	public void setCourierType(int courierType) {
		this.courierType = courierType;
	}

	@Column(name ="BIND_USER_ID", nullable=false,precision=11,scale=0)
	public Integer getBindUserId() {
		return bindUserId;
	}

	public void setBindUserId(Integer bindUserId) {
		this.bindUserId = bindUserId;
	}
	
	
}
