
package com.wm.entity.merchantapply;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.star.lib.uno.environments.java.java_environment;


/**   
 * @Title: Entity
 * @Description: 商家注册审核表
 * @author wuyong
 * @date 2016-02-17 15:30:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_merchant_apply", schema = "")
@SuppressWarnings("serial")
public class MerchantApplyEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**merchantName*/
	private java.lang.String merchantName;
	/** '商家来源'*/
	private java.lang.Integer merchantSource;
	/**商家类别*/
	private java.lang.Integer groupId;
	/**支持的销售类型*/
	private java.lang.Integer supportSaleType;
	/**合同编号*/
	private java.lang.String contractNo;
	/**铺店地址*/
	private java.lang.String address;
	/**经度*/
	private BigDecimal longitude;
	/**纬度*/
	private BigDecimal latitude;
	/**店主姓名*/
	private java.lang.String shopkeeper;
	/**身份证*/
	private java.lang.String idCardNo;
	/**身份证正面照*/
	private java.lang.String idCardFrontImgUrl;
	/**身份证背面照*/
	private java.lang.String idCardBackImgUrl;
	/**营业执照*/
	private java.lang.String businessLicense;
	/**营业执照url*/
	private java.lang.String businessLicenseImgUrl;
	/**饮餐服务*/
	private java.lang.String foodServiceLicense;
	/**饮餐服务url*/
	private java.lang.String foodServiceLicenseImgUrl;
	/**快递员id*/
	private java.lang.Integer courierId;
	/**创建时间*/
	private java.lang.String createTime;
	/**审核状态  1.审核中  2. 审核通过  3.审核不通过*/
	private java.lang.Integer state;
	/**拒绝原因*/
	private java.lang.String refuseReason;
	/**手机号码*/
	private java.lang.String mobile;
	/**店铺照片*/
	private java.lang.String merchantImgUrl;
	/**城市id*/
	private java.lang.Integer cityId;
	/**更新时间*/
	private java.lang.Integer updateTime;
	/**银行id*/
	private java.lang.Integer bankId;
	/**开户人*/
	private java.lang.String accountHolder;
	/**银行支行*/
	private java.lang.String sourceBank;
	/**银行卡号*/
	private java.lang.String bankCardNo;
	/**银行卡正面照*/
	private java.lang.String bankCardFrontImgUrl;
	/**合同照片*/
	private java.lang.String contractImgUrls;
	/**二维码id*/
	private java.lang.Integer qrcodeLibraryId;
	/**备注*/
	private java.lang.String remark;
	///////////////////////////12-30
	/** 商家 账期 */
	private Integer income_date;
	/** 商家 扫码扣点 类型 */
	private String deduction_type;
	
	

	public Integer getIncome_date() {
		return income_date;
	}

	public void setIncome_date(Integer income_date) {
		this.income_date = income_date;
	}

	public String getDeduction_type() {
		return deduction_type;
	}

	public void setDeduction_type(String deduction_type) {
		this.deduction_type = deduction_type;
	}

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
	 *@return: java.lang.String  merchantName
	 */
	@Column(name ="MERCHANT_NAME",nullable=false,length=50)
	public java.lang.String getMerchantName(){
		return this.merchantName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  merchantName
	 */
	public void setMerchantName(java.lang.String merchantName){
		this.merchantName = merchantName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer   '商家来源'
	 */
	@Column(name ="MERCHANT_SOURCE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getMerchantSource(){
		return this.merchantSource;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer   '商家来源'
	 */
	public void setMerchantSource(java.lang.Integer merchantSource){
		this.merchantSource = merchantSource;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  商家类别
	 */
	@Column(name ="GROUP_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getGroupId(){
		return this.groupId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  商家类别
	 */
	public void setGroupId(java.lang.Integer groupId){
		this.groupId = groupId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  支持的销售类型
	 */
	@Column(name ="SUPPORT_SALE_TYPE",nullable=false)
	public java.lang.Integer getSupportSaleType(){
		return this.supportSaleType;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  支持的销售类型
	 */
	public void setSupportSaleType(java.lang.Integer supportSaleType){
		this.supportSaleType = supportSaleType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同编号
	 */
	@Column(name ="CONTRACT_NO",nullable=true,length=30)
	public java.lang.String getContractNo(){
		return this.contractNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同编号
	 */
	public void setContractNo(java.lang.String contractNo){
		this.contractNo = contractNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  铺店地址
	 */
	@Column(name ="ADDRESS",nullable=false,length=200)
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  铺店地址
	 */
	public void setAddress(java.lang.String address){
		this.address = address;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  经度
	 */
	@Column(name ="LONGITUDE",nullable=false,precision=10,scale=6)
	public BigDecimal getLongitude(){
		return this.longitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  经度
	 */
	public void setLongitude(BigDecimal longitude){
		this.longitude = longitude;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  纬度
	 */
	@Column(name ="LATITUDE",nullable=false,precision=10,scale=6)
	public BigDecimal getLatitude(){
		return this.latitude;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  纬度
	 */
	public void setLatitude(BigDecimal latitude){
		this.latitude = latitude;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店主姓名
	 */
	@Column(name ="SHOPKEEPER",nullable=true,length=50)
	public java.lang.String getShopkeeper(){
		return this.shopkeeper;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店主姓名
	 */
	public void setShopkeeper(java.lang.String shopkeeper){
		this.shopkeeper = shopkeeper;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证
	 */
	@Column(name ="ID_CARD_NO",nullable=false,length=18)
	public java.lang.String getIdCardNo(){
		return this.idCardNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证
	 */
	public void setIdCardNo(java.lang.String idCardNo){
		this.idCardNo = idCardNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证正面照
	 */
	@Column(name ="ID_CARD_FRONT_IMG_URL",nullable=true,length=255)
	public java.lang.String getIdCardFrontImgUrl(){
		return this.idCardFrontImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证正面照
	 */
	public void setIdCardFrontImgUrl(java.lang.String idCardFrontImgUrl){
		this.idCardFrontImgUrl = idCardFrontImgUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证背面照
	 */
	@Column(name ="ID_CARD_BACK_IMG_URL",nullable=true,length=255)
	public java.lang.String getIdCardBackImgUrl(){
		return this.idCardBackImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证背面照
	 */
	public void setIdCardBackImgUrl(java.lang.String idCardBackImgUrl){
		this.idCardBackImgUrl = idCardBackImgUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  营业执照
	 */
	@Column(name ="BUSINESS_LICENSE",nullable=true,length=30)
	public java.lang.String getBusinessLicense(){
		return this.businessLicense;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  营业执照
	 */
	public void setBusinessLicense(java.lang.String businessLicense){
		this.businessLicense = businessLicense;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  营业执照url
	 */
	@Column(name ="BUSINESS_LICENSE_IMG_URL",nullable=true,length=255)
	public java.lang.String getBusinessLicenseImgUrl(){
		return this.businessLicenseImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  营业执照url
	 */
	public void setBusinessLicenseImgUrl(java.lang.String businessLicenseImgUrl){
		this.businessLicenseImgUrl = businessLicenseImgUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  饮餐服务
	 */
	@Column(name ="FOOD_SERVICE_LICENSE",nullable=true,length=30)
	public java.lang.String getFoodServiceLicense(){
		return this.foodServiceLicense;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  饮餐服务
	 */
	public void setFoodServiceLicense(java.lang.String foodServiceLicense){
		this.foodServiceLicense = foodServiceLicense;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  饮餐服务url
	 */
	@Column(name ="FOOD_SERVICE_LICENSE_IMG_URL",nullable=true,length=255)
	public java.lang.String getFoodServiceLicenseImgUrl(){
		return this.foodServiceLicenseImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  饮餐服务url
	 */
	public void setFoodServiceLicenseImgUrl(java.lang.String foodServiceLicenseImgUrl){
		this.foodServiceLicenseImgUrl = foodServiceLicenseImgUrl;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递员id
	 */
	@Column(name ="COURIER_ID",nullable=false,precision=19,scale=0)
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
	 *@return: java.lang.String  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=false)
	public java.lang.String getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建时间
	 */
	public void setCreateTime(java.lang.String createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态  1.审核中  2. 审核通过  3.审核不通过
	 */
	@Column(name ="STATE",nullable=false,precision=3,scale=0)
	public java.lang.Integer getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态  1.审核中  2. 审核通过  3.审核不通过
	 */
	public void setState(java.lang.Integer state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拒绝原因
	 */
	@Column(name ="REFUSE_REASON",nullable=true,length=500)
	public java.lang.String getRefuseReason(){
		return this.refuseReason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拒绝原因
	 */
	public void setRefuseReason(java.lang.String refuseReason){
		this.refuseReason = refuseReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商家手机号码
	 */
	@Column(name ="MOBILE",nullable=false,length=16)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商家手机号码
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店铺照片
	 */
	@Column(name ="MERCHANT_IMG_URL",nullable=false,length=255)
	public java.lang.String getMerchantImgUrl(){
		return this.merchantImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店铺照片
	 */
	public void setMerchantImgUrl(java.lang.String merchantImgUrl){
		this.merchantImgUrl = merchantImgUrl;
	}
	
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  城市id
	 */
	@Column(name ="CITY_ID",nullable=false,precision=11,scale=0)
	public java.lang.Integer getCityId() {
		return cityId;
	}

	public void setCityId(java.lang.Integer cityId) {
		this.cityId = cityId;
	}
	
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  城市id
	 */
	@Column(name ="UPDATE_TIME",nullable=false,precision=11,scale=0)
	public java.lang.Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.lang.Integer updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  银行id
	 */
	@Column(name ="BANK_ID",nullable=false,precision=11,scale=0)
	public java.lang.Integer getBankId() {
		return bankId;
	}

	public void setBankId(java.lang.Integer bankId) {
		this.bankId = bankId;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户人
	 */
	@Column(name ="ACCOUNT_HOLDER",nullable=false,length=255)
	public java.lang.String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(java.lang.String accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行支行
	 */
	@Column(name ="SOURCE_BANK",nullable=false,length=255)
	public java.lang.String getSourceBank() {
		return sourceBank;
	}

	public void setSourceBank(java.lang.String sourceBank) {
		this.sourceBank = sourceBank;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String 银行卡号
	 */
	@Column(name ="BANK_CARD_NO",nullable=false,length=255)
	public java.lang.String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(java.lang.String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行卡正面照
	 */
	@Column(name ="BANK_CARD_FRONT_IMG_URL",nullable=false,length=255)
	public java.lang.String getBankCardFrontImgUrl() {
		return bankCardFrontImgUrl;
	}

	public void setBankCardFrontImgUrl(java.lang.String bankCardFrontImgUrl) {
		this.bankCardFrontImgUrl = bankCardFrontImgUrl;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同照片
	 */
	@Column(name ="CONTRACT_IMG_URLS",nullable=false,length=255)
	public java.lang.String getContractImgUrls() {
		return contractImgUrls;
	}

	public void setContractImgUrls(java.lang.String contractImgUrls) {
		this.contractImgUrls = contractImgUrls;
	}

	@Column(name ="QRCODE_LIBRARY_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getQrcodeLibraryId() {
		return qrcodeLibraryId;
	}

	public void setQrcodeLibraryId(java.lang.Integer qrcodeLibraryId) {
		this.qrcodeLibraryId = qrcodeLibraryId;
	}
	
	@Column(name ="REMARK",nullable=true,length=500)
	public java.lang.String getRemark() {
		return remark;
	}
	
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

}