package com.dianba.pos.order.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderPojo implements Serializable{

    private Long passportId;
    private Double actualPrice;
    private Double totalPrice;
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

    public List<OrderItemPojo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemPojo> items) {
        this.items = items;
    }
}
