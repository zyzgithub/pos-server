package com.dianba.pos.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantOrderIncomeVo implements Serializable {

    public static final String INCOME_BALANCE = "已到余额";
    public static final String NOT_SETTLEMENT = "未结算";
    public static final String INCOME = "收入";

    private Integer id;
    private String time;
    private String transType;
    private String transSequence;
    private BigDecimal amount;
    private String title;
    private String settlementTitle;
    @JsonIgnore
    private String paymentType;

    private String realName;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransSequence() {
        return transSequence;
    }

    public void setTransSequence(String transSequence) {
        this.transSequence = transSequence;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSettlementTitle() {
        return settlementTitle;
    }

    public void setSettlementTitle(String settlementTitle) {
        this.settlementTitle = settlementTitle;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
