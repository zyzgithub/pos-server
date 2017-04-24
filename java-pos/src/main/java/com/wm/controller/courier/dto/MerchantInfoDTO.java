
package com.wm.controller.courier.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class MerchantInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/** 商家id*/
	private Integer id;
	@NotEmpty
	/** name 商家名称*/
	private String merchantName;
	@NotEmpty
	/** mobile 商家电话*/
	private String mobile;
	@NotNull
	/** merchantSource 商家来源*/
	private Integer merchantSource;
	@NotNull
	/** groupId 商家类别*/
	private Integer groupId;
	@NotNull
	/** supportSaleType 支持的销售类型*/
	private Integer supportSaleType;
	@NotEmpty
	/** contractNumber 合同编号*/
	private String contractNo;	
	
	@NotEmpty
	/** address 详细地址*/
	private String address;
	@NotNull
	/** longitude 经度*/
	private Double longitude;
	@NotNull
	/** latitude 纬度*/
	private Double latitude;
	@NotEmpty
	/** shopkeeper 姓名*/
	private String shopkeeper;
	@NotEmpty
	/** idCardNo 身份证号*/
	private String idCardNo;
	@NotEmpty
	/** idCardFrontImgUrl 身份证正面照*/
	private String idCardFrontImgUrl;
	@NotEmpty
	/** idCardBackImgUrl*/
	private String idCardBackImgUrl;
	@NotEmpty
	/** merchantImgUrl 店面照片*/
	private String merchantImgUrl;
	
	/** businessLicenseNumber 营业执照注册号*/
	private String businessLicense;
	
	/** businessLicense 营业执照照片*/
	private String businessLicenseImgUrl;
	
	/** operatingPermitNumber 经营许可证注册号*/
	private String foodServiceLicense;
	
	/** operatingPermit 经营许可证*/
	private String foodServiceLicenseImgUrl;	
	@NotNull
	/** cityId 城市id*/
	private Integer cityId;
	@NotNull
	/** bankId 银行id*/
	private Integer bankId;
	@NotEmpty
	/** accountHolder 开户人*/
	private String accountHolder;
	@NotEmpty
	/** sourceBank 银行支行*/
	private String sourceBank;
	@NotEmpty
	/** bankCardNo 银行卡号*/
	private String bankCardNo;
	@NotEmpty
	/** bankCardFrontImgUrl 银行卡正面照*/
	private String bankCardFrontImgUrl;
	@NotEmpty
	/** contractImgUrls 合同照片*/
	private String contractImgUrls;
	
	// @NotNull
	/**二维码id*/
	private Integer qrcodeLibraryId;
	
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getMerchantSource() {
		return merchantSource;
	}

	public void setMerchantSource(Integer merchantSource) {
		this.merchantSource = merchantSource;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSupportSaleType() {
		return supportSaleType;
	}

	public void setSupportSaleType(Integer supportSaleType) {
		this.supportSaleType = supportSaleType;
	}	

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getShopkeeper() {
		return shopkeeper;
	}

	public void setShopkeeper(String shopkeeper) {
		this.shopkeeper = shopkeeper;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getIdCardFrontImgUrl() {
		return idCardFrontImgUrl;
	}

	public void setIdCardFrontImgUrl(String idCardFrontImgUrl) {
		this.idCardFrontImgUrl = idCardFrontImgUrl;
	}

	public String getIdCardBackImgUrl() {
		return idCardBackImgUrl;
	}

	public void setIdCardBackImgUrl(String idCardBackImgUrl) {
		this.idCardBackImgUrl = idCardBackImgUrl;
	}

	public String getMerchantImgUrl() {
		return merchantImgUrl;
	}

	public void setMerchantImgUrl(String merchantImgUrl) {
		this.merchantImgUrl = merchantImgUrl;
	}	

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getBusinessLicenseImgUrl() {
		return businessLicenseImgUrl;
	}

	public void setBusinessLicenseImgUrl(String businessLicenseImgUrl) {
		this.businessLicenseImgUrl = businessLicenseImgUrl;
	}

	public String getFoodServiceLicense() {
		return foodServiceLicense;
	}

	public void setFoodServiceLicense(String foodServiceLicense) {
		this.foodServiceLicense = foodServiceLicense;
	}

	public String getFoodServiceLicenseImgUrl() {
		return foodServiceLicenseImgUrl;
	}

	public void setFoodServiceLicenseImgUrl(String foodServiceLicenseImgUrl) {
		this.foodServiceLicenseImgUrl = foodServiceLicenseImgUrl;
	}	

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getSourceBank() {
		return sourceBank;
	}

	public void setSourceBank(String sourceBank) {
		this.sourceBank = sourceBank;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardFrontImgUrl() {
		return bankCardFrontImgUrl;
	}

	public void setBankCardFrontImgUrl(String bankCardFrontImgUrl) {
		this.bankCardFrontImgUrl = bankCardFrontImgUrl;
	}

	public String getContractImgUrls() {
		return contractImgUrls;
	}

	public void setContractImgUrls(String contractImgUrls) {
		this.contractImgUrls = contractImgUrls;
	}

	public Integer getQrcodeLibraryId() {
		return qrcodeLibraryId;
	}

	public void setQrcodeLibraryId(Integer qrcodeLibraryId) {
		this.qrcodeLibraryId = qrcodeLibraryId;
	}

	// public String getRemark() {
	// return remark;
	// }
	//
	// public void setRemark(String remark) {
	// this.remark = remark;
	// }
}

