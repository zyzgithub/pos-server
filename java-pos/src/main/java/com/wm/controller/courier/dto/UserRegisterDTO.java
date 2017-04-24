package com.wm.controller.courier.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jeecgframework.core.util.DateUtils;

public class UserRegisterDTO implements Serializable{

	private static final long serialVersionUID = -1406619257254196885L;
	/** id */
	private java.lang.Integer id;
	/** gender */
	private java.lang.String gender = "M";// 默认为男m , female f
	/** username 用户真实姓名 */
	@NotEmpty
	private java.lang.String username;
	/** password */
	@NotEmpty
	private java.lang.String password;
	/** mobile */
	@NotEmpty
	private java.lang.String mobile;
	/** userType */
	private java.lang.String userType = "courier"; // 默认快递员，'courier''快递员,''merchant''商家,''user''普通用户
	/** ip */
//	@NotEmpty
	private java.lang.String ip;
	/** createTime */
	private java.lang.Integer createTime = DateUtils.getSeconds();
	/** 银行卡ID */
	@NotNull
	private java.lang.Integer bankId;
	/** 银行卡号 */
	@NotEmpty
	private java.lang.String cardNo;
	/** 银行卡支行信息 */
	@NotEmpty
	private java.lang.String sourceBank;
	/** 身份证正面照url */
	@NotEmpty
	private java.lang.String idCardFrontImgUrl;
	/** 银行卡正面照url */
	@NotEmpty
	private java.lang.String bankCardFrontImgUrl;
	/** 身份证反面照 */
	@NotEmpty
	private java.lang.String idCardBackImgUrl;
	/** 手持身份证正面照url */
	@NotEmpty
	private java.lang.String holdIdCardFrontImgUrl;
	/** 手持身份证反面照 */
	
	private java.lang.String holdIdCardBackImgUrl;
	/** 审核状态，0=审核中，1=审核通过，2=审核失败 */
	private java.lang.Integer checkStatus = 0;
	/** 身份证号码 */
	@NotEmpty
	private java.lang.String idCard;
	
	// 期望配送区域
	@NotEmpty
	private String expectedDistArea;
	
	/** 户籍类型 */
	@NotEmpty
	private java.lang.String censusType;
	
	/** 户籍地址 */
	@NotEmpty
	private java.lang.String censusAddress;
	
	/** 所在省份 */
	@NotEmpty
	private java.lang.String province;
	
	/** 所在城市 */
	@NotEmpty
	private java.lang.String city;
	
	/** 居住地址 */
	@NotEmpty
	private java.lang.String address;
	
	
	/** 婚姻状况 0未婚  1已婚 */
	private java.lang.Integer marriage;
	
	/** 民族  */
	@NotEmpty
	private java.lang.String nation;
	
	/** 籍贯  */
	@NotEmpty
	private java.lang.String nativePlace;
	
	/** 紧急联系人姓名 */
	@NotEmpty
	private java.lang.String emergencyName;
	
	/** 紧急联系人电话 */
	@NotEmpty
	private java.lang.String emergencyPhone;
	
	/** 学历 */
	@NotEmpty
	private java.lang.String schooling;	
	
	/** 手持学历证正面照*/
	@NotEmpty
	private java.lang.String holdSchoolingFrontImgUrl;	
	
	/**开户人姓名*/
	private java.lang.String accountHolder;
	
	
	
	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getGender() {
		if(StringUtils.isBlank(gender)){
			gender = "M";
		}
		return gender;
	}

	public void setGender(java.lang.String gender) {
		this.gender = gender;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getUserType() {
		if(StringUtils.isBlank(userType)){
			userType = "courier";
		}
		return userType;
	}

	public void setUserType(java.lang.String userType) {
		this.userType = userType;
	}

	public java.lang.String getIp() {
		return ip;
	}

	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}

	public java.lang.Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Integer createTime) {
		this.createTime = createTime;
	}

	public java.lang.Integer getBankId() {
		return bankId;
	}

	public void setBankId(java.lang.Integer bankId) {
		this.bankId = bankId;
	}

	public java.lang.String getCardNo() {
		return cardNo;
	}

	public void setCardNo(java.lang.String cardNo) {
		this.cardNo = cardNo;
	}

	public java.lang.String getSourceBank() {
		return sourceBank;
	}

	public void setSourceBank(java.lang.String sourceBank) {
		this.sourceBank = sourceBank;
	}

	public java.lang.String getIdCardFrontImgUrl() {
		return idCardFrontImgUrl;
	}

	public void setIdCardFrontImgUrl(java.lang.String idCardFrontImgUrl) {
		this.idCardFrontImgUrl = idCardFrontImgUrl;
	}
	
	public java.lang.String getBankCardFrontImgUrl() {
		return bankCardFrontImgUrl;
	}

	public void setBankCardFrontImgUrl(java.lang.String bankCardFrontImgUrl) {
		this.bankCardFrontImgUrl = bankCardFrontImgUrl;
	}

	public java.lang.String getIdCardBackImgUrl() {
		return idCardBackImgUrl;
	}

	public void setIdCardBackImgUrl(java.lang.String idCardBackImgUrl) {
		this.idCardBackImgUrl = idCardBackImgUrl;
	}

	public java.lang.String getHoldIdCardFrontImgUrl() {
		return holdIdCardFrontImgUrl;
	}

	public void setHoldIdCardFrontImgUrl(java.lang.String holdIdCardFrontImgUrl) {
		this.holdIdCardFrontImgUrl = holdIdCardFrontImgUrl;
	}

	public java.lang.String getHoldIdCardBackImgUrl() {
		return holdIdCardBackImgUrl;
	}

	public void setHoldIdCardBackImgUrl(java.lang.String holdIdCardBackImgUrl) {
		this.holdIdCardBackImgUrl = holdIdCardBackImgUrl;
	}

	public java.lang.Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(java.lang.Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public java.lang.String getIdCard() {
		return idCard;
	}

	public void setIdCard(java.lang.String idCard) {
		this.idCard = idCard;
	}

	public String getExpectedDistArea() {
		return expectedDistArea;
	}

	public void setExpectedDistArea(String expectedDistArea) {
		this.expectedDistArea = expectedDistArea;
	}

	public java.lang.String getCensusType() {
		return censusType;
	}

	public void setCensusType(java.lang.String censusType) {
		this.censusType = censusType;
	}

	public java.lang.String getCensusAddress() {
		return censusAddress;
	}

	public void setCensusAddress(java.lang.String censusAddress) {
		this.censusAddress = censusAddress;
	}

	public java.lang.String getProvince() {
		return province;
	}

	public void setProvince(java.lang.String province) {
		this.province = province;
	}

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.Integer getMarriage() {
		return marriage;
	}

	public void setMarriage(java.lang.Integer marriage) {
		this.marriage = marriage;
	}

	public java.lang.String getNation() {
		return nation;
	}

	public void setNation(java.lang.String nation) {
		this.nation = nation;
	}

	public java.lang.String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(java.lang.String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public java.lang.String getEmergencyName() {
		return emergencyName;
	}

	public void setEmergencyName(java.lang.String emergencyName) {
		this.emergencyName = emergencyName;
	}

	public java.lang.String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(java.lang.String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public java.lang.String getSchooling() {
		return schooling;
	}

	public void setSchooling(java.lang.String schooling) {
		this.schooling = schooling;
	}

	public java.lang.String getHoldSchoolingFrontImgUrl() {
		return holdSchoolingFrontImgUrl;
	}

	public void setHoldSchoolingFrontImgUrl(
			java.lang.String holdSchoolingFrontImgUrl) {
		this.holdSchoolingFrontImgUrl = holdSchoolingFrontImgUrl;
	}

	public java.lang.String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(java.lang.String accountHolder) {
		this.accountHolder = accountHolder;
	}

	
	
	

}
