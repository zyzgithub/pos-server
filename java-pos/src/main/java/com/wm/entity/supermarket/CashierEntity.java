package com.wm.entity.supermarket;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 超市收银员关联表
 * @author wuyong
 * @date 2016-05-23 15:38:21
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_cashier", schema = "")
@SuppressWarnings("serial")
public class CashierEntity implements java.io.Serializable {
	
	public final static String valid = "1";
	public final static String unvalid = "0";
	
	
	/**主键*/
	private java.lang.Integer id;
	/**姓名*/
	private java.lang.String name;
	/**身份证*/
	private java.lang.String idCard;
	/**手机号码*/
	private java.lang.String mobile;
	/**登录密码*/
	private java.lang.String password;
	/**商家ID*/
	private java.lang.Integer merchantId;
	/**创建时间*/
	private java.lang.Integer createTime;
	
	private java.lang.String headImageUrl;
	
	/**快递员状态 0 表示有效 1 表示无效*/
	private java.lang.String status;
	
	/**收银员类型   1.营业员   2.店长*/
	private Integer cashierType;
	

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  姓名
	 */
	@Column(name ="NAME",nullable=false,length=30)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证
	 */
	@Column(name ="ID_CARD",nullable=false,length=20)
	public java.lang.String getIdCard(){
		return this.idCard;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证
	 */
	public void setIdCard(java.lang.String idCard){
		this.idCard = idCard;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手机号码
	 */
	@Column(name ="MOBILE",nullable=false,length=11)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手机号码
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  登录密码
	 */
	@Column(name ="PASSWORD",nullable=false,length=50)
	public java.lang.String getPassword(){
		return this.password;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  登录密码
	 */
	public void setPassword(java.lang.String password){
		this.password = password;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家ID
	 */
	@Column(name ="MERCHANT_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getMerchantId(){
		return this.merchantId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家ID
	 */
	public void setMerchantId(java.lang.Integer merchantId){
		this.merchantId = merchantId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建时间
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}

	@Column(name ="head_image_url",nullable=false,length=200)
	public java.lang.String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(java.lang.String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	@Column(name ="status",nullable=false,length=2)
	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	
	@Column(name ="CASHIER_TYPE",nullable=false,precision=2,scale=0)
	public Integer getCashierType() {
		return cashierType;
	}

	public void setCashierType(Integer cashierType) {
		this.cashierType = cashierType;
	}
	
}
