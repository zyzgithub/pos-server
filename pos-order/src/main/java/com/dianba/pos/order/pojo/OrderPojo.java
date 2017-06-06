package com.dianba.pos.order.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderPojo implements Serializable{

    private String id;
    private Long passportId;
    private Double actualPrice;
    private Double totalPrice;
    private String createTime;
    private String paymenTime;
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

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaymenTime() {
        return paymenTime;
    }

    public void setPaymenTime(String paymenTime) {
        this.paymenTime = paymenTime;
    }

    public List<OrderItemPojo> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<OrderItemPojo> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
