package com.wm.entity.pos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
public class PurchaseDetail {
    @Id
    private Integer orderId;

    private Integer userId;
    private String payId;

    private Double origin;
    private Integer merchantId;
    /**
     * 1.supply
     * 2.
     */
    private Integer orderType;
    /**
     * 0/null : 不需要处理. 即 下单未支付时的备份数据
     * 1. 未处理
     * 2. 已处理
     */
    private Integer state;
    /**
     * 单位 秒
     */
    private Long createTime;

    /**
     * 采购 数量
     */
    private Map<String, Integer> barcodePurchase = new HashMap<>(0);

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Map<String, Integer> getBarcodePurchase() {
        return barcodePurchase;
    }

    public void setBarcodePurchase(Map<String, Integer> barcodePurchase) {
        this.barcodePurchase = barcodePurchase;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

	public Double getOrigin() {
		return origin;
	}

	public void setOrigin(Double origin) {
		this.origin = origin;
	}
    
}