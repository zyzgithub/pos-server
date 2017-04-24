package com.wm.controller.takeout.vo;

public class BankCardVo {
	
	private int userId;
	private int bankId;
	private String name;
	private String cardNo;
	private String defaultStr;
	private String phone;
	private String sourceBank;
	private String bankImgUrl;

	private String bankName;
	private String bankCode;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDefaultStr() {
		return defaultStr;
	}

	public void setDefaultStr(String defaultStr) {
		this.defaultStr = defaultStr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSourceBank() {
		return sourceBank;
	}

	public void setSourceBank(String sourceBank) {
		this.sourceBank = sourceBank;
	}

	public String getBankImgUrl() {
		return bankImgUrl;
	}

	public void setBankImgUrl(String bankImgUrl) {
		this.bankImgUrl = bankImgUrl;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
