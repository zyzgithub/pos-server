package com.wm.entity.order;

public class OrderTimerVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer orderId;
	private Integer merchantId;
	private Integer createTime;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
	
}
