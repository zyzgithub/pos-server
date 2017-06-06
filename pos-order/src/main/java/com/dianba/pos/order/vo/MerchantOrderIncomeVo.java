package com.dianba.pos.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantOrderIncomeVo implements Serializable{

    private Integer id;
    private String time;
    private String transType;
    private String transSequence;
    private BigDecimal amount;
    private String title;

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
}
