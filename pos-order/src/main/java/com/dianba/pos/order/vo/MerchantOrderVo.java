package com.dianba.pos.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public class MerchantOrderVo implements Serializable{

    @JsonIgnore
    private Long orderId;
    private String showName;
    private String sequenceNumber;
    private String createTime;
    private Long totalPrice;
    private String status;
    private List<MerchantOrderItemSnapshotVo> itemSnapshots;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MerchantOrderItemSnapshotVo> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<MerchantOrderItemSnapshotVo> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
