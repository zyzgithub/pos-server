package com.dianba.pos.order.vo;
import com.dianba.pos.common.util.StringUtil;
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
    private Integer countMap;

    public Integer getCountMap() {
        return countMap;
    }

    public void setCountMap(Integer countMap) {
        this.countMap = countMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        if(StringUtil.isEmpty(itemName)){
            return "扫码支付";
        }else{
            return itemName;
        }

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
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getCount() {

        if(StringUtil.isEmpty(itemName)){
            return 1;
        }else {
            return count;
        }

    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
