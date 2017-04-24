package com.wm.controller.courier.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

public class CrowdsourcingRegisterDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	/** id */
	private java.lang.Integer id;
	/** gender */
	private java.lang.String gender = "M";// 默认为男m , female f
	/** username 用户真实姓名 */
	@NotEmpty
	private java.lang.String username;
	/** userType */
	private java.lang.String userType = "courier"; // 默认快递员，'courier''快递员,''merchant''商家,''user''普通用户
	/** ip */
//	@NotEmpty
	private java.lang.String ip;
	/** 银行卡ID */
	@NotNull
	private java.lang.Integer bankId;
	/** 银行卡号 */
	@NotEmpty
	private java.lang.String cardNo;
	/** 身份证正面照url */
	@NotEmpty
	private java.lang.String idCardFrontImgUrl;
	/** 银行卡正面照url */
	@NotEmpty
	private java.lang.String bankCardFrontImgUrl;
	/** 身份证反面照 */
	@NotEmpty
	private java.lang.String idCardBackImgUrl;
	/** 审核状态，0=审核中，1=审核通过，2=审核失败 */
	private java.lang.Integer checkStatus = 0;
	/** 身份证号码 */
	@NotEmpty
	private java.lang.String idCard;
	/** 所在省份 */
	
	private java.lang.String province;
	
	/** 所在城市 */
	@NotEmpty
	private java.lang.String city;
	@NotEmpty
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

	public java.lang.String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(java.lang.String accountHolder) {
		this.accountHolder = accountHolder;
	}
}
