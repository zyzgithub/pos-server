package com.wm.dto.bank;

public class BankCardDto {
	private Integer userId;
	private Integer bankId; //银行
	private String cardNo; //卡号
	private String sourceBank; //开户行
	private String name; //开户人
	private String phone; //开户手机号
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getSourceBank() {
		return sourceBank;
	}
	public void setSourceBank(String sourceBank) {
		this.sourceBank = sourceBank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
