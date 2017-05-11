package com.dianba.pos.extended.vo;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
public class ProductChargeDto {

    private String orderNo;
    private String merOrderNo;
    private String mobile;
    private Integer flowValue;
    private Double payPrice;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getFlowValue() {
        return flowValue;
    }

    public void setFlowValue(Integer flowValue) {
        this.flowValue = flowValue;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
}
