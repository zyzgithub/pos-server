package com.dianba.pos.order.vo;

import java.io.Serializable;
import java.util.List;

public class OrderVo implements Serializable{

    private String showName;
    private String sequenceNumber;
    private String createTime;
    private Long totalPrice;
    private String status;
    private List<OrderItemSnapshotVo> itemSnapshots;

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

    public List<OrderItemSnapshotVo> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<OrderItemSnapshotVo> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }
}
