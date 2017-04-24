package com.wm.controller.courierdevelop.dto;


public class MerchantDevRecDTO {

	private int id;

	private int courierId;
	private String targetMerchant;
	private String merchantHolder;
	private String merchantPhone;
	private String remark;
	private String checkString;
	
	public String getCheckString() {
		return checkString;
	}

	public void setCheckString(String checkString) {
		this.checkString = checkString;
	}


	public String getTargetMerchant() {
		return targetMerchant;
	}

	public void setTargetMerchant(String targetMerchant) {
		this.targetMerchant = targetMerchant;
	}

	public String getMerchantHolder() {
		return merchantHolder;
	}

	public void setMerchantHolder(String merchantHolder) {
		this.merchantHolder = merchantHolder;
	}

	public String getMerchantPhone() {
		return merchantPhone;
	}

	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

}
