package com.wm.dto.order;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wm.util.JacksonUtil;

public class ConfirmPayRequest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Long id;
	private String payId;
	private Integer userId;
	private String type;
	private BigDecimal origin;
	private String payPassword;
	private String weixinReturnCallUrl;
	private String payType; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getOrigin() {
		return origin;
	}

	public void setOrigin(BigDecimal origin) {
		this.origin = origin;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getWeixinReturnCallUrl() {
		return weixinReturnCallUrl;
	}

	public void setWeixinReturnCallUrl(String weixinReturnCallUrl) {
		this.weixinReturnCallUrl = weixinReturnCallUrl;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Override
	public String toString() {
		try {
			return JacksonUtil.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.info("序列化时出错", e);
			return super.toString();
		}
	}

}
