package com.dianba.pos.box.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class BoxItemVo implements Serializable {

    private Long itemId;
    private String itemName;
    private Integer itemQuantity;
    private BigDecimal itemPrice;
    //0,未支付，1已支付，3包含未支付商品
    private Integer isPaid;
    private String showPaidName;
    private String rfids;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public String getShowPaidName() {
        return showPaidName;
    }

    public void setShowPaidName(String showPaidName) {
        this.showPaidName = showPaidName;
    }

    public String getRfids() {
        return rfids;
    }

    public void setRfids(String rfids) {
        this.rfids = rfids;
    }

}
