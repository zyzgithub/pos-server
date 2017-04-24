package com.wm.entity.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wm.entity.user.WUserEntity;

/**   
 * @Title: Entity
 * @Description: bank_card
 * @author wuyong
 * @date 2015-01-07 09:49:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "bank_card", schema = "")
public class BankcardEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**id*/
	private java.lang.Integer id;
	/**userId*/
	private WUserEntity wuser;
	/**bankId*/
	private BankEntity bank; //银行
	/**cardNo*/
	private java.lang.String cardNo; //卡号
	/**default*/
	private java.lang.String isDefault;

	private java.lang.String sourceBank; //开户行
	/** 持卡人姓名 */
	private java.lang.String name; //开户人
	
	private String phone; //开户手机号
	
	private String bankImgUrl;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=19,scale=0)
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	public WUserEntity getWuser() {
		return wuser;
	}

	public void setWuser(WUserEntity wuser) {
		this.wuser = wuser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_ID", nullable = true)
	public BankEntity getBank() {
		return bank;
	}

	public void setBank(BankEntity bank) {
		this.bank = bank;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  cardNo
	 */
	@Column(name ="CARD_NO",nullable=true,length=50)
	public java.lang.String getCardNo(){
		return this.cardNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  cardNo
	 */
	public void setCardNo(java.lang.String cardNo){
		this.cardNo = cardNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  default
	 */
	@Column(name ="DEFAULT",nullable=true,length=1)
	public java.lang.String getIsDefault() {
		return isDefault;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  default
	 */
	public void setIsDefault(java.lang.String isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name="source_bank",nullable=true)
	public java.lang.String getSourceBank() {
		return sourceBank;
	}

	public void setSourceBank(java.lang.String sourceBank) {
		this.sourceBank = sourceBank;
	}

	@Column(name = "name", nullable = true)
	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="bank_img_url")
	public String getBankImgUrl() {
		return bankImgUrl;
	}

	public void setBankImgUrl(String bankImgUrl) {
		this.bankImgUrl = bankImgUrl;
	}
	
}
