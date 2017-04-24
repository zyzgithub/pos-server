package com.wm.controller.user.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
public class CashierVo implements Serializable{
	/**主键*/
	private Integer id;
	/**姓名*/
	@NotEmpty
	private String name;
	@NotNull
	/**身份证*/
	@Pattern (regexp="^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$", message="不是有效的身份证号码")
	private String idCard;
	/**手机号码*/
	@Pattern (regexp="^((13[0-9])|(145,147)|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$", message="不是有效的手机号码")
	private String mobile;
	/**登录密码*/
	@NotEmpty
	private String password;
	/**商家ID*/
	@NotNull
	private Integer merchantId;
	/**创建时间*/
	private Integer createTime;
	
	@NotEmpty
	private String headImageUrl; 
	
	/**收银员类型   1.营业员   2.店长*/
	@NotNull
	private Integer cashierType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public String getHeadImageUrl() {
		return headImageUrl;
	}
	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}
	public Integer getCashierType() {
		return cashierType;
	}
	public void setCashierType(Integer cashierType) {
		this.cashierType = cashierType;
	}
	
	
}
