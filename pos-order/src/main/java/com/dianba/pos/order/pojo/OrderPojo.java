package com.dianba.pos.order.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderPojo implements Serializable{

    private String id;
    private Long passportId;
    private BigDecimal actualPrice;
    private BigDecimal totalPrice;
    private String createTime;
    private String paymentTime;
    private List<OrderItemPojo> itemSnapshots;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public List<OrderItemPojo> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<OrderItemPojo> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
