package com.ucf.common;

import java.io.Serializable;

public class BaseParam implements Serializable {

	private static final long serialVersionUID = 7576971733008610498L;

	/** 接口名称，固定值：REQ_WITHDRAW **/
	private String service;
	
	/** 签名算法，固定值：RSA **/
	private String secId = "RSA";
	
	/** 接口版本，固定值：3.0.0 **/
	private String version = "3.0.0";
	
	/** 商户号 **/
	private String merchantId;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSecId() {
		return secId;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	/** 接口类型 **/
	public static final class Service {
		// 提现
		public static final String WITHDRAW = "REQ_WITHDRAW";
	}

}
