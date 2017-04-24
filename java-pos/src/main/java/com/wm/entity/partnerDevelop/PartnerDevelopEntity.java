package com.wm.entity.partnerDevelop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 合作商开发表
 * @author wuyong
 * @date 2016-04-01 13:44:15
 * @version V1.0   
 *
 */
@Entity
@Table(name = "0085_partner_develop", schema = "")
@SuppressWarnings("serial")
public class PartnerDevelopEntity implements java.io.Serializable {
	/**开拓合作商id*/
	private java.lang.Integer developId;
	/**业务员id*/
	private java.lang.Integer userId;
	/**合作商名称*/
	private java.lang.String partnerName;
	/**电话号码*/
	private java.lang.String phone;
	/**商务电话*/
	private java.lang.String businessPhone;
	/**签约账号数量*/
	private java.lang.Integer accountAmount;
	/**省/地区*/
	private java.lang.String provCode;
	/**城市*/
	private java.lang.String cityCode;
	/**银行*/
	private java.lang.String bank;
	/**银行id*/
	private java.lang.String bankId;
	/**卡号*/
	private java.lang.String cardNo;
	/**开户行*/
	private java.lang.String bankOfDeposit;
	/**持卡人*/
	private java.lang.String cardHolder;
	/**真实姓名*/
	private java.lang.String realName;
	/**身份证号码*/
	private java.lang.String idCard;
	/**店主身份证正面照*/
	private java.lang.String idcardPhotoFront;
	/**店主身份证背面照*/
	private java.lang.String idcardPhotoBack;
	/**银行卡照片*/
	private java.lang.String bankcardPhoto;
	/**合同编号*/
	private java.lang.String contractNo;
	/**合同照片*/
	private java.lang.String contractPhoto;
	/**创建日期*/
	private java.util.Date createDate;
	/**更改日期*/
	private java.util.Date doneDate;
	/**审核状态：1.审核中 2. 审核通过 3.审核不通过*/
	private java.lang.Integer state;
	/**备注*/
	private java.lang.String remark;
	/**扩展字段*/
	private java.lang.String ext;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  开拓合作商id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="DEVELOP_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getDevelopId(){
		return this.developId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  开拓合作商id
	 */
	public void setDevelopId(java.lang.Integer developId){
		this.developId = developId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer   业务员id
	 */
	@Column(name ="USER_ID",nullable=false,precision=19,scale=0)
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务员id
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合作商名称
	 */
	@Column(name ="PARTNER_NAME",nullable=true,length=64)
	public java.lang.String getPartnerName(){
		return this.partnerName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合作商名称
	 */
	public void setPartnerName(java.lang.String partnerName){
		this.partnerName = partnerName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电话号码
	 */
	@Column(name ="PHONE",nullable=true,length=20)
	public java.lang.String getPhone(){
		return this.phone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电话号码
	 */
	public void setPhone(java.lang.String phone){
		this.phone = phone;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  商务电话号码
	 */
	@Column(name ="BUSINESS_PHONE",nullable=true,length=20)
	public java.lang.String getBusinessPhone(){
		return this.businessPhone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  商务电话号码
	 */
	public void setBusinessPhone(java.lang.String businessPhone){
		this.businessPhone = businessPhone;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  签约账号数量
	 */
	@Column(name ="ACCOUNT_AMOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getAccountAmount(){
		return this.accountAmount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  签约账号数量
	 */
	public void setAccountAmount(java.lang.Integer accountAmount){
		this.accountAmount = accountAmount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  省/地区
	 */
	@Column(name ="PROV_CODE",nullable=true,length=10)
	public java.lang.String getProvCode(){
		return this.provCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  省/地区
	 */
	public void setProvCode(java.lang.String provCode){
		this.provCode = provCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  城市
	 */
	@Column(name ="CITY_CODE",nullable=true,length=10)
	public java.lang.String getCityCode(){
		return this.cityCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  城市
	 */
	public void setCityCode(java.lang.String cityCode){
		this.cityCode = cityCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行
	 */
	@Column(name ="BANK",nullable=true,length=20)
	public java.lang.String getBank(){
		return this.bank;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行
	 */
	public void setBankId(java.lang.String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行
	 */
	@Column(name ="BANK_ID",nullable=true,length=20)
	public java.lang.String getBankId(){
		return this.bankId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行
	 */
	public void setBank(java.lang.String bank){
		this.bank = bank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  卡号
	 */
	@Column(name ="CARD_NO",nullable=true,length=20)
	public java.lang.String getCardNo(){
		return this.cardNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  卡号
	 */
	public void setCardNo(java.lang.String cardNo){
		this.cardNo = cardNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户行
	 */
	@Column(name ="BANK_OF_DEPOSIT",nullable=true,length=64)
	public java.lang.String getBankOfDeposit(){
		return this.bankOfDeposit;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行
	 */
	public void setBankOfDeposit(java.lang.String bankOfDeposit){
		this.bankOfDeposit = bankOfDeposit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  持卡人
	 */
	@Column(name ="CARD_HOLDER",nullable=true,length=20)
	public java.lang.String getCardHolder(){
		return this.cardHolder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  持卡人
	 */
	public void setCardHolder(java.lang.String cardHolder){
		this.cardHolder = cardHolder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  真实姓名
	 */
	@Column(name ="REAL_NAME",nullable=true,length=20)
	public java.lang.String getRealName(){
		return this.realName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  真实姓名
	 */
	public void setRealName(java.lang.String realName){
		this.realName = realName;
	}
	/**
	 *方法: 取得java.lang.Object
	 *@return: java.lang.Object  身份证号码
	 */
	@Column(name ="ID_CARD",nullable=true,length=20)
	public java.lang.String getIdCard(){
		return this.idCard;
	}

	/**
	 *方法: 设置java.lang.Object
	 *@param: java.lang.Object  身份证号码
	 */
	public void setIdCard(java.lang.String idCard){
		this.idCard = idCard;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店主身份证正面照
	 */
	@Column(name ="IDCARD_PHOTO_FRONT",nullable=true,length=64)
	public java.lang.String getIdcardPhotoFront(){
		return this.idcardPhotoFront;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店主身份证正面照
	 */
	public void setIdcardPhotoFront(java.lang.String idcardPhotoFront){
		this.idcardPhotoFront = idcardPhotoFront;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  店主身份证背面照
	 */
	@Column(name ="IDCARD_PHOTO_BACK",nullable=true,length=64)
	public java.lang.String getIdcardPhotoBack(){
		return this.idcardPhotoBack;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  店主身份证背面照
	 */
	public void setIdcardPhotoBack(java.lang.String idcardPhotoBack){
		this.idcardPhotoBack = idcardPhotoBack;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行卡照片
	 */
	@Column(name ="BANKCARD_PHOTO",nullable=true,length=64)
	public java.lang.String getBankcardPhoto(){
		return this.bankcardPhoto;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行卡照片
	 */
	public void setBankcardPhoto(java.lang.String bankcardPhoto){
		this.bankcardPhoto = bankcardPhoto;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同编号
	 */
	@Column(name ="CONTRACT_NO",nullable=true,length=64)
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
	 *@return: java.lang.String  合同照片
	 */
	@Column(name ="CONTRACT_PHOTO",nullable=true,length=256)
	public java.lang.String getContractPhoto(){
		return this.contractPhoto;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同照片
	 */
	public void setContractPhoto(java.lang.String contractPhoto){
		this.contractPhoto = contractPhoto;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更改日期
	 */
	@Column(name ="DONE_DATE",nullable=true)
	public java.util.Date getDoneDate(){
		return this.doneDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更改日期
	 */
	public void setDoneDate(java.util.Date doneDate){
		this.doneDate = doneDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态：1.审核中 2. 审核通过 3.审核不通过
	 */
	@Column(name ="STATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态：1.审核中 2. 审核通过 3.审核不通过
	 */
	public void setState(java.lang.Integer state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="REMARK",nullable=true,length=128)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  扩展字段
	 */
	@Column(name ="EXT",nullable=true,length=10)
	public java.lang.String getExt(){
		return this.ext;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  扩展字段
	 */
	public void setExt(java.lang.String ext){
		this.ext = ext;
	}
}
