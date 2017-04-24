package com.wm.entity.courierapply;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.star.lib.uno.environments.java.java_environment;


/**   
 * @Title: Entity
 * @Description: 0085_courier_apply
 * @author 0085.com
 * @date 2015-09-17 09:48:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_courier_apply", schema = "")
@SuppressWarnings("serial")
public class CourierApplyEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**gender*/
	private java.lang.String gender;
	/**username*/
	private java.lang.String username;
	/**password*/
	private java.lang.String password;
	/**mobile*/
	private java.lang.String mobile;
	/**userType*/
	private java.lang.String userType;
	/**ip*/
	private java.lang.String ip;
	/**createTime*/
	private java.lang.Integer createTime;
	/**银行卡ID*/
	private java.lang.Integer bankId;
	/**银行卡号*/
	private java.lang.String cardNo;
	/**银行卡支行信息*/
	private java.lang.String sourceBank;
	/**身份证正面照url*/
	private java.lang.String idCardFrontImgUrl;
	/**身份证反面照*/
	private java.lang.String idCardBackImgUrl;
	/**手持身份证正面照url*/
	private java.lang.String holdIdCardFrontImgUrl;
	/**手持身份证反面照*/
	private java.lang.String holdIdCardBackImgUrl;
	/**审核状态，0=审核中，1=审核通过，2=审核失败*/
	private java.lang.Integer checkStatus;
	/**身份证号码*/
	private java.lang.String idCard;
	// 期望配送区域
	private String expectedDistArea;
	/** 银行卡正面照url */
	private java.lang.String bankCardFrontImgUrl;
	/** 户籍类型 */
	private java.lang.String censusType;
	/** 户籍地址 */
	private java.lang.String censusAddress;
	/** 所在省份 */
	private java.lang.String province;
	/** 所在城市 */
	private java.lang.String city;
	/** 居住地址 */
	private java.lang.String address;
	/** 婚姻状况 0未婚  1已婚 */
	private java.lang.Integer marriage;
	/** 民族  */
	private java.lang.String nation;
	/** 籍贯  */
	private java.lang.String nativePlace;
	/** 紧急联系人姓名 */
	private java.lang.String emergencyName;
	/** 紧急联系人电话 */
	private java.lang.String emergencyPhone;
	/** 学历 */
	private java.lang.String schooling;
	/** 手持学历证正面照*/
	private java.lang.String holdSchoolingFrontImgUrl;
	/**用户Id*/
	private java.lang.Integer userId;
	/**开户人姓名*/
	private java.lang.String accountHolder;
	/**快递员类型*/
	private java.lang.Integer courierType;
	/**代理商id*/
	private java.lang.Integer bindUserId;
	/**代理商审核状态*/
	private java.lang.Integer agentCheckStatus;
	/**代理商审核id*/
	private java.lang.Integer agentUserId;
	
	
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=11,scale=0)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  gender
	 */
	@Column(name ="GENDER",nullable=true,length=1)
	public java.lang.String getGender(){
		return this.gender;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  gender
	 */
	public void setGender(java.lang.String gender){
		this.gender = gender;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  username
	 */
	@Column(name ="USERNAME",nullable=true,length=255)
	public java.lang.String getUsername(){
		return this.username;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  username
	 */
	public void setUsername(java.lang.String username){
		this.username = username;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  password
	 */
	@Column(name ="PASSWORD",nullable=true,length=255)
	public java.lang.String getPassword(){
		return this.password;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  password
	 */
	public void setPassword(java.lang.String password){
		this.password = password;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  mobile
	 */
	@Column(name ="MOBILE",nullable=true,length=255)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  mobile
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  userType
	 */
	@Column(name ="USER_TYPE",nullable=true,length=7)
	public java.lang.String getUserType(){
		return this.userType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  userType
	 */
	public void setUserType(java.lang.String userType){
		this.userType = userType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ip
	 */
	@Column(name ="IP",nullable=true,length=255)
	public java.lang.String getIp(){
		return this.ip;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ip
	 */
	public void setIp(java.lang.String ip){
		this.ip = ip;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  createTime
	 */
	public void setCreateTime(java.lang.Integer createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  银行卡ID
	 */
	@Column(name ="BANK_ID",nullable=true,precision=10,scale=0)
	public java.lang.Integer getBankId(){
		return this.bankId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  银行卡ID
	 */
	public void setBankId(java.lang.Integer bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行卡号
	 */
	@Column(name ="CARD_NO",nullable=true,length=255)
	public java.lang.String getCardNo(){
		return this.cardNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行卡号
	 */
	public void setCardNo(java.lang.String cardNo){
		this.cardNo = cardNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行卡支行信息
	 */
	@Column(name ="SOURCE_BANK",nullable=true,length=255)
	public java.lang.String getSourceBank(){
		return this.sourceBank;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行卡支行信息
	 */
	public void setSourceBank(java.lang.String sourceBank){
		this.sourceBank = sourceBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证正面照url
	 */
	@Column(name ="ID_CARD_FRONT_IMG_URL",nullable=true,length=255)
	public java.lang.String getIdCardFrontImgUrl(){
		return this.idCardFrontImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证正面照url
	 */
	public void setIdCardFrontImgUrl(java.lang.String idCardFrontImgUrl){
		this.idCardFrontImgUrl = idCardFrontImgUrl;
	}
	

	@Column(name ="BANK_CARD_FRONT_IMG_URL",nullable=true,length=255)
	public java.lang.String getBankCardFrontImgUrl() {
		return bankCardFrontImgUrl;
	}

	public void setBankCardFrontImgUrl(java.lang.String bankCardFrontImgUrl) {
		this.bankCardFrontImgUrl = bankCardFrontImgUrl;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证反面照
	 */
	@Column(name ="ID_CARD_BACK_IMG_URL",nullable=true,length=255)
	public java.lang.String getIdCardBackImgUrl(){
		return this.idCardBackImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证反面照
	 */
	public void setIdCardBackImgUrl(java.lang.String idCardBackImgUrl){
		this.idCardBackImgUrl = idCardBackImgUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手持身份证正面照url
	 */
	@Column(name ="HOLD_ID_CARD_FRONT_IMG_URL",nullable=true,length=255)
	public java.lang.String getHoldIdCardFrontImgUrl(){
		return this.holdIdCardFrontImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手持身份证正面照url
	 */
	public void setHoldIdCardFrontImgUrl(java.lang.String holdIdCardFrontImgUrl){
		this.holdIdCardFrontImgUrl = holdIdCardFrontImgUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手持身份证反面照
	 */
	@Column(name ="HOLD_ID_CARD_BACK_IMG_URL",nullable=true,length=255)
	public java.lang.String getHoldIdCardBackImgUrl(){
		return this.holdIdCardBackImgUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手持身份证反面照
	 */
	public void setHoldIdCardBackImgUrl(java.lang.String holdIdCardBackImgUrl){
		this.holdIdCardBackImgUrl = holdIdCardBackImgUrl;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态，0=审核中，1=审核通过，2=审核失败
	 */
	@Column(name ="CHECK_STATUS",nullable=true,precision=3,scale=0)
	public java.lang.Integer getCheckStatus(){
		return this.checkStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态，0=审核中，1=审核通过，2=审核失败
	 */
	public void setCheckStatus(java.lang.Integer checkStatus){
		this.checkStatus = checkStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证号码
	 */
	@Column(name ="ID_CARD",nullable=true,length=18)
	public java.lang.String getIdCard(){
		return this.idCard;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证号码
	 */
	public void setIdCard(java.lang.String idCard){
		this.idCard = idCard;
	}

	@Column(name ="EXPECTED_DIST_AREA",nullable=true,length=255)
	public String getExpectedDistArea() {
		return expectedDistArea;
	}

	public void setExpectedDistArea(String expectedDistArea) {
		this.expectedDistArea = expectedDistArea;
	}
	@Column(name = "CENSUS_TYPE", nullable = true, length = 50)
	public java.lang.String getCensusType() {
		return censusType;
	}

	public void setCensusType(java.lang.String censusType) {
		this.censusType = censusType;
	}
	@Column(name = "CENSUS_ADDRESS", nullable = true, length = 200)
	public java.lang.String getCensusAddress() {
		return censusAddress;
	}

	public void setCensusAddress(java.lang.String censusAddress) {
		this.censusAddress = censusAddress;
	}
	@Column(name = "PROVINCE", nullable = true, length = 20)
	public java.lang.String getProvince() {
		return province;
	}

	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	@Column(name = "CITY", nullable = true, length = 20)
	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}
	@Column(name = "ADDRESS", nullable = true, length = 500)
	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	@Column(name = "MARRIAGE", nullable = true, length = 4)
	public java.lang.Integer getMarriage() {
		return marriage;
	}

	public void setMarriage(java.lang.Integer marriage) {
		this.marriage = marriage;
	}
	@Column(name = "NATION", nullable = true, length = 20)
	public java.lang.String getNation() {
		return nation;
	}

	public void setNation(java.lang.String nation) {
		this.nation = nation;
	}
	@Column(name = "NATIVE_PLACE", nullable =true, length = 20)
	public java.lang.String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(java.lang.String nativePlace) {
		this.nativePlace = nativePlace;
	}
	@Column(name = "EMERGENCY_NAME", nullable = true, length = 50)
	public java.lang.String getEmergencyName() {
		return emergencyName;
	}

	public void setEmergencyName(java.lang.String emergencyName) {
		this.emergencyName = emergencyName;
	}
	@Column(name = "EMERGENCY_PHONE", nullable = true, length = 18)
	public java.lang.String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(java.lang.String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}
	@Column(name = "SCHOOLING", nullable = true, length = 20)
	public java.lang.String getSchooling() {
		return schooling;
	}

	public void setSchooling(java.lang.String schooling) {
		this.schooling = schooling;
	}
	
	@Column(name = "HOLD_SCHOOLING_FRONT_IMG_URL", nullable = true, length = 255)
	public java.lang.String getHoldSchoolingFrontImgUrl() {
		return holdSchoolingFrontImgUrl;
	}

	public void setHoldSchoolingFrontImgUrl(
			java.lang.String holdSchoolingFrontImgUrl) {
		this.holdSchoolingFrontImgUrl = holdSchoolingFrontImgUrl;
	}
	
	@Column(name = "USER_ID", nullable = true, precision=11,scale=0)
	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	@Column(name = "ACCOUNT_HOLDER", nullable = true, length = 255)
	public java.lang.String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(java.lang.String accountHolder) {
		this.accountHolder = accountHolder;
	}

	@Column(name = "COURIER_TYPE", nullable = true, precision=10,scale=0)
	public java.lang.Integer getCourierType() {
		return courierType;
	}

	public void setCourierType(java.lang.Integer courierType) {
		this.courierType = courierType;
	}

	@Column(name = "BIND_USER_ID", nullable = true, precision=11,scale=0)
	public java.lang.Integer getBindUserId() {
		return bindUserId;
	}

	public void setBindUserId(java.lang.Integer bindUserId) {
		this.bindUserId = bindUserId;
	}

	@Column(name = "AGENT_CHECK_STATUS", nullable = true, precision=4,scale=0)
	public java.lang.Integer getAgentCheckStatus() {
		return agentCheckStatus;
	}

	public void setAgentCheckStatus(java.lang.Integer agentCheckStatus) {
		this.agentCheckStatus = agentCheckStatus;
	}

	@Column(name = "AGENT_USER_ID", nullable = true, precision=11,scale=0)
	public java.lang.Integer getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(java.lang.Integer agentUserId) {
		this.agentUserId = agentUserId;
	}
	
}
