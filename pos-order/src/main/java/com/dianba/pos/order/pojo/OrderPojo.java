package com.dianba.pos.order.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderPojo implements Serializable{

    private Long passportId;
    private Double actualPrice;
    private Double totalPrice;
    private String createTime;
    private String completeTime;
    private List<OrderItemPojo> items;

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

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public List<OrderItemPojo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemPojo> items) {
        this.items = items;
    }
}
