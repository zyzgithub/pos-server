package com.wm.service.impl.pay;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class PlatformBarcodePayResponse implements Serializable{
	
	private Map<String, Object> body;
	
	private Integer orderId;

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	
}
