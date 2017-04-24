package com.wm.dto.merchant;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantSupplySaleOrderDto {
	
	private Integer orderId; //订单id
	private String payId;  //订单号
	private Integer userId; //供应商的userId
	private BigDecimal money;	//实际收入
	private BigDecimal origin; //订单金额
	private Date completeTime; //完成时间
	
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getOrigin() {
		return origin;
	}
	public void setOrigin(BigDecimal origin) {
		this.origin = origin;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	
}
