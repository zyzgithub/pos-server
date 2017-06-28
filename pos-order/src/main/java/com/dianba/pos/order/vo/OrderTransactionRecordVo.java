package com.dianba.pos.order.vo;
import java.math.BigDecimal;

/**
 * Created by zhangyong on 2017/6/28.
 */
public class OrderTransactionRecordVo {
    private Long id;
    private String itemName;
    private String sequenceNumber;
    private String transType;
    private String paymentTime;
    private BigDecimal totalPrice;
    private BigDecimal actualPrice;
    private Integer count;

    private BigDecimal a=new BigDecimal(100);
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.divide(a,2,BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice.divide(a,2,BigDecimal.ROUND_HALF_UP);
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
