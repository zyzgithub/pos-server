package com.wm.entity.opendevelopuser;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "open_developer_user", schema = "")
@SuppressWarnings("serial")
public class OpenDeveloperUserEntity implements Serializable{
	private  static final long serialVersionUID = -4698105615729612997L;
	private Long id;
	private String appId;
	private String mchId;
	private String userId;
	private String secretKey;
	private String notifyUrl;
	private String securePayAddress;
	private Integer confType;
	private Date createTime;
	private Date updateTime;
	private Integer creater;
	private Integer status;
	private String configDesc;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=20,scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "mch_id")
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "secret_key")
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	@Column(name = "notify_url")
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	@Column(name = "secure_pay_address")
	public String getSecurePayAddress() {
		return securePayAddress;
	}
	public void setSecurePayAddress(String securePayAddress) {
		this.securePayAddress = securePayAddress;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Column(name = "app_id")
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@Column(name = "conf_type")
	public Integer getConfType() {
		return confType;
	}
	public void setConfType(Integer confType) {
		this.confType = confType;
	}
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "creater")
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "config_desc")
	public String getConfigDesc() {
		return configDesc;
	}
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}
	
}